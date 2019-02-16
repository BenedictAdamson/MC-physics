package uk.badamson.mc.physics.solver;
/*
 * © Copyright Benedict Adamson 2018-19.
 *
 * This file is part of MC-physics.
 *
 * MC-physics is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MC-physics is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MC-physics.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.NonNull;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;

/**
 * <p>
 * Functors and functor factory methods for {@linkplain EnergyErrorFunction
 * Energy error function} terms (contributions) that can be used in a
 * {@linkplain HarmonicVector3EnergyErrorFunction energy error function that
 * calculates the physical modelling error for a time varying 3D vector
 * property}.
 * </p>
 * <p>
 * This class provides
 * {@code Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>}
 * functors. The {@link Function#apply(Object)} method of all the functors
 * provided by this class expect a non-null {@link HarmonicVector3} and return a
 * non-null {@link HarmonicVector3EnergyErrorValueAndGradients}.
 * </p>
 */
public final class HarmonicVector3EnergyErrorFunctionTerms {

    /**
     * <p>
     * A term that is always
     * {@linkplain HarmonicVector3EnergyErrorValueAndGradients.ZERO zero}.
     * </p>
     */
    public static final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> ZERO = (
            v) -> HarmonicVector3EnergyErrorValueAndGradients.ZERO;

    /**
     * <p>
     * Create a term that is tends to cause the {@linkplain HarmonicVector3 time
     * varying 3D vector property} to have a given value for a given point in time.
     * </p>
     * <ul>
     * <li>Always returns a (non null) term.</li>
     * <li>The {@linkplain HarmonicVector3EnergyErrorValueAndGradients#getE()
     * energy} computed by the term is equal to the given scale multiplied by the
     * {@linkplain ImmutableVector3#magnitude2() square of the magnitude} of the
     * {@linkplain ImmutableVector3#minus(ImmutableVector3) difference} between the
     * desired value and the actual value.</li>
     * </ul>
     *
     * @param scale
     *            The scaling factor for converting the square of a value difference
     *            to an energy. Larger values more tightly constrain the vector
     *            property. Values should usually be positive; negative values force
     *            the vector property to infinity.
     * @param t
     *            The point in time of where the property ought to have the given
     *            value.
     * @param f
     *            The value that the property ought to have at the given point in
     *            time.
     * @return the term functor.
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@linkplain t} is null.</li>
     *             <li>If {@linkplain f} is null.</li>
     *             </ul>
     */
    public static Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> createValueTerm(
            final double scale, @NonNull final Duration t, @NonNull final ImmutableVector3 f) {
        Objects.requireNonNull(t, "t");
        Objects.requireNonNull(f, "f");
        final double scale2 = 2.0 * scale;
        return (actual) -> {
            /*
             * Inline calculation of fa = actual.at(t), so can reuse the intermediate terms.
             */
            final Duration tr = t.minus(actual.getT0());
            final double ts = tr.getSeconds() + 1E-9 * tr.getNano();
            final ImmutableVector3 fc = actual.getFc();
            final ImmutableVector3 fs = actual.getFs();
            final double we = actual.getWe();
            final double wh = actual.getWh();

            final double tau = we * ts;
            final double tau2 = tau * tau;
            final double alpha = wh * ts;
            final double exp = Math.exp(tau);
            final double expCos = exp * Math.cos(alpha);
            final double expSin = exp * Math.sin(alpha);

            final double fWeights[] = { 1.0, tau, tau2, expCos, expSin };
            final ImmutableVector3 termsArray[] = { actual.getF0(), actual.getF1(), actual.getF2(), fc, fs };
            final ImmutableVector3 fa = ImmutableVector3.weightedSum(fWeights, termsArray);

            final ImmutableVector3 fe = fa.minus(f);
            final double e = scale * fe.magnitude2();

            final ImmutableVector3 dedf0 = fe.scale(scale2);
            final ImmutableVector3 dedf1 = fe.scale(scale2 * tau);
            final ImmutableVector3 dedf2 = fe.scale(scale2 * tau2);
            final ImmutableVector3 dedfc = fe.scale(scale2 * expCos);
            final ImmutableVector3 dedfs = fe.scale(scale2 * expSin);
            final double dedweWeights[] = { 0, ts, 2.0 * tau * ts, expCos * we * ts, expSin * we * ts };
            final ImmutableVector3 dfdwe = ImmutableVector3.weightedSum(dedweWeights, termsArray);
            final double dedwe = scale2 * fe.dot(dfdwe);
            final ImmutableVector3 dfdwh = fc.scale(-wh * expSin).plus(fs.scale(wh * expCos));
            final double dedwh = scale2 * fe.dot(dfdwh);

            return new HarmonicVector3EnergyErrorValueAndGradients(e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);
        };
    }
}
