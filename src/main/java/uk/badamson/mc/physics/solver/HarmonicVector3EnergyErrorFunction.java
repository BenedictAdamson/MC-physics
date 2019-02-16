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

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.FunctionNWithGradient;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.MinN;
import uk.badamson.mc.math.PoorlyConditionedFunctionException;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.solver.mapper.HarmonicVector3Mapper;

/**
 * <p>
 * A {@linkplain FunctionNWithGradient functor} that calculates the physical
 * modelling error,which has dimensions of energy, of a
 * {@linkplain HarmonicVector3 functor for a time varying 3D vector property
 * that can have damped harmonic variation}.
 * </p>
 * <p>
 * This also acts as a Composite of a collection of function terms.
 * </p>
 */
@Immutable
public final class HarmonicVector3EnergyErrorFunction
        implements EnergyErrorFunction, Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> {

    private final HarmonicVector3Mapper mapper;
    private final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>[] terms;

    /**
     * <p>
     * Construct an object with given associations.
     * </p>
     * <ul>
     * <li>The {@linkplain #getMapper() mapper} of this is the given mapper.</li>
     * <li>The {@linkplain #getTerms() terms} of this
     * {@linkplain List#equals(Object) equals} the given terms.</li>
     * </ul>
     *
     * @param mapper
     *            The Strategy for mapping from the {@linkplain HarmonicVector3
     *            object representation} of the time varying 3D vector property to
     *            (part of) a state-space representation, and vice versa.
     * @param terms
     *            The contributors to this function
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code mapper} is null.</li>
     *             <li>If {@code terms} is null.</li>
     *             <li>If {@code terms} contains a null.</li>
     *             </ul>
     */
    public HarmonicVector3EnergyErrorFunction(@NonNull final HarmonicVector3Mapper mapper,
            @NonNull final List<Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>> terms) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        @SuppressWarnings("unchecked")
        final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>[] termsArray = new Function[0];
        this.terms = Objects.requireNonNull(terms, "terms").toArray(termsArray);
        // Check after copy to avoid race hazards
        for (final var term : this.terms) {
            Objects.requireNonNull(term, "term");
        }
    }

    /**
     * <p>
     * The total {@linkplain HarmonicVector3EnergyErrorValueAndGradients error} of
     * the {@linkplain #getTerms() terms}, for a given functor.
     * </p>
     * <p>
     * This method provides the class with the functionality of a Composite.
     * </p>
     * <ul>
     * <li>The total error is the
     * {@linkplain HarmonicVector3EnergyErrorValueAndGradients#sum(HarmonicVector3EnergyErrorValueAndGradients...)
     * sum} of the errors computed for each of the {@linkplain #getTerms()
     * terms}.</li>
     * </ul>
     *
     * @param v
     *            The functor for which to compute the total error.
     *
     * @return The error value and its gradients; not null.
     * @throws NullPointerException
     *             If {@code v} is null.
     */
    @Override
    public final HarmonicVector3EnergyErrorValueAndGradients apply(final HarmonicVector3 v) {
        Objects.requireNonNull(v, "v");
        final HarmonicVector3EnergyErrorValueAndGradients[] values = new HarmonicVector3EnergyErrorValueAndGradients[terms.length];
        for (int i = 0; i < terms.length; i++) {
            final var term = terms[i];
            values[i] = term.apply(v);
        }
        return HarmonicVector3EnergyErrorValueAndGradients.sum(values);
    }

    @NonNull
    private ImmutableVectorN convertToDeDx(
            @NonNull final HarmonicVector3EnergyErrorValueAndGradients errorAndGradients) {
        final double[] dedx = new double[mapper.getMinimumStateSpaceDimension()];
        dedx[mapper.getWeIndex()] = errorAndGradients.getDedwe();
        dedx[mapper.getWhIndex()] = errorAndGradients.getDedwh();
        for (int i = 0; i < 3; ++i) {
            dedx[mapper.getF0Mapper().getComponentIndex(i)] = errorAndGradients.getDedf0().get(i);
            dedx[mapper.getF1Mapper().getComponentIndex(i)] = errorAndGradients.getDedf1().get(i);
            dedx[mapper.getF2Mapper().getComponentIndex(i)] = errorAndGradients.getDedf2().get(i);
            dedx[mapper.getFcMapper().getComponentIndex(i)] = errorAndGradients.getDedfc().get(i);
            dedx[mapper.getFsMapper().getComponentIndex(i)] = errorAndGradients.getDedfs().get(i);
        }
        return ImmutableVectorN.create(dedx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The dimension of the function equals the
     * {@linkplain HarmonicVector3Mapper#getMinimumStateSpaceDimension() minimum
     * space dimension} of the {@linkplain #getMapper() mapper}.
     * </p>
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getDimension() {
        return mapper.getMinimumStateSpaceDimension();
    }

    /**
     * <p>
     * The associated Strategy for mapping from the {@linkplain HarmonicVector3
     * object representation} of the time varying 3D vector property to (part of) a
     * state-space representation, and vice versa.
     * </p>
     *
     * @return the mapper; not null
     */
    public final HarmonicVector3Mapper getMapper() {
        return mapper;
    }

    /**
     * <p>
     * The associated contributors to this function
     * </p>
     * <ul>
     * <li>Always have a (non null) collection of terms.</li>
     * <li>The collection of terms does not contain a null term.</li>
     * <li>The collection of terms is not modifiable.</li>
     * <li>The collection of terms may be a newly created object.</li>
     * </ul>
     */
    public final List<Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>> getTerms() {
        return List.of(terms);
    }

    /**
     * <p>
     * Find the {@linkplain HarmonicVector3 time varying 3D vector property} that
     * minimizes this energy error function.
     * </p>
     * <p>
     * This performs an iterative computation using the Polak-Ribere's modification
     * of the Fletcher-Reeves conjugate gradient algorithm.
     * </p>
     *
     * @param f0
     *            An initial guess for the time varying 3D vector property
     * @param tolerance
     *            The convergence tolerance fir the iterative procedure.
     * @return the time varying 3D vector property that minimizes this energy error
     *         function; not null.
     *
     * @throws NullPointerException
     *             If {@code f0} is null.
     * @throws IllegalArgumentException
     *             If {@code tolerance} is not in the range (0.0, 1.0).
     * @throws PoorlyConditionedFunctionException
     *             <ul>
     *             <li>If this does not have a minimum</li>
     *             <li>If this has a minimum, but it is impossible to find using
     *             {@code f0} because this has an odd-powered high order term that
     *             causes the iterative procedure to diverge.</li>
     *             </ul>
     */
    public final @NonNull HarmonicVector3 minimiseEnergyError(@NonNull final HarmonicVector3 f0, final double tolerance)
            throws PoorlyConditionedFunctionException {
        final double[] terms = new double[mapper.getMinimumStateSpaceDimension()];
        mapper.fromObject(terms, f0);
        final ImmutableVectorN x0 = ImmutableVectorN.create(terms);
        final FunctionNWithGradientValue minState = MinN.findFletcherReevesPolakRibere(this, x0, tolerance);
        return mapper.toObject(minState.getX());
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     *            {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public FunctionNWithGradientValue value(final ImmutableVectorN state) {
        final HarmonicVector3 vector = mapper.toObject(state);
        final HarmonicVector3EnergyErrorValueAndGradients errorAndGradients = apply(vector);
        final ImmutableVectorN dedx = convertToDeDx(errorAndGradients);
        return new FunctionNWithGradientValue(state, errorAndGradients.getE(), dedx);
    }
}
