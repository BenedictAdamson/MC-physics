package uk.badamson.mc.physics;
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
import java.util.Arrays;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * A {@linkplain TimeVaryingVector3 functor for a time varying 3D vector
 * property} that can have <i>damped harmonic</i> variation.
 * </p>
 * <p>
 * The class allows for a constant acceleration (second derivative), and thus a
 * quadratic variation in its value.
 * </p>
 * <p>
 * It is tempting to allow for a linear variation of the acceleration (constant
 * <dfn>jerk</dfn>), and thus a cubic variation in its value. However, as for
 * all higher order polynomials, that would produce a variation that can become
 * infinitely large with time. That would be unrealistic, because in practice
 * all forces (accelerations) are self limiting. So the variation deliberately
 * does not allow for a cubic variation.
 * </p>
 * <p>
 * The class allows for <dfn>damped harmonic</dfn> variation; that is, a
 * decaying sinusoidal variation of its value. That also allows for
 * exponentially decaying and growing variation as a special case of zero
 * frequency of the sinusoidal variation.
 * </p>
 * <p>
 * The timewise variation of the 3D vector is given by the expression
 * </p>
 * <p>
 * f(t) = f<sub>0</sub> + f<sub>1</sub>&tau; + f<sub>2</sub>&tau;<sup>2</sup> +
 * e<sup>&tau;</sup>(f<sub>c</sub>cos &alpha; + f<sub>s</sub>sin &alpha;)
 * </p>
 * <p>
 * where &tau; = &omega;<sub>e</sub>(t - t<sub>0</sub>), and &alpha; =
 * &omega;<sub>h</sub>(t - t<sub>0</sub>), with t<sub>0</sub>, f<sub>0</sub>,
 * f<sub>c</sub>, f<sub>s</sub>, f<sub>1</sub>, f<sub>2</sub>, f<sub>3</sub>,
 * &omega;<sub>e</sub> and &omega;<sub>h</sub> being constant parameters.
 * </p>
 *
 * @see HarmonicScalar
 */
@Immutable
public final class HarmonicVector3 extends AbstractTimeVaryingVector3 {

    private final Duration t0;
    private final ImmutableVector3[] termsArray;
    private final double we;
    private final double wh;

    /**
     * <p>
     * Construct a functor with given parameters.
     * </p>
     *
     * @param t0
     *            The t<sub>0</sub> parameter; the time origin.
     * @param f0
     *            The f<sub>0</sub> parameter; the constant term. This has the same
     *            units as the function value.
     * @param f1
     *            The f<sub>1</sub> parameter; the linear or velocity term. This has
     *            the same units as the function value.
     * @param f2
     *            The f<sub>2</sub> parameter; the quadratic or acceleration term.
     *            This has the same units as the function value.
     * @param fc
     *            The f<sub>c</sub> parameter; the cosine term. This has the same
     *            units as the function value.
     * @param fs
     *            The f<sub>s</sub> parameter; the sine term. This has the same
     *            units as the function value.
     * @param we
     *            The &omega;<sub>e</sub> parameter; the exponential frequency term.
     *            This has units of Herz (Hz, s<sup>-1</sup>).
     * @param wh
     *            The &omega;<sub>h</sub> parameter; the harmonic frequency term.
     *            This has units of Herz (Hz, s<sup>-1</sup>).
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code t0} is null.</li>
     *             <li>If {@code f0} is null.</li>
     *             <li>If {@code f1} is null.</li>
     *             <li>If {@code f2} is null.</li>
     *             <li>If {@code fc} is null.</li>
     *             <li>If {@code fs} is null.</li>
     *             </ul>
     */
    public HarmonicVector3(@NonNull final Duration t0, final ImmutableVector3 f0, final ImmutableVector3 f1,
            final ImmutableVector3 f2, final ImmutableVector3 fc, final ImmutableVector3 fs, final double we,
            final double wh) {
        this.t0 = Objects.requireNonNull(t0, "t0");
        Objects.requireNonNull(f0, "f0");
        Objects.requireNonNull(fc, "fc");
        Objects.requireNonNull(fs, "fs");
        Objects.requireNonNull(f1, "f1");
        Objects.requireNonNull(f2, "f2");
        this.we = we;
        this.wh = wh;
        termsArray = new ImmutableVector3[] { f0, f1, f2, fc, fs };
    }

    @Override
    public final ImmutableVector3 at(@NonNull final Duration t) {
        Objects.requireNonNull(t, "t");
        final Duration tr = t.minus(t0);
        final double ts = tr.getSeconds() + 1E-9 * tr.getNano();
        final double tau = we * ts;
        final double alpha = wh * ts;
        final double exp = Math.exp(tau);
        final double tau2 = tau * tau;
        final double weights[] = { 1.0, tau, tau2, exp * Math.cos(alpha), exp * Math.sin(alpha) };
        return ImmutableVector3.weightedSum(weights, termsArray);
    }

    /**
     * <p>
     * Whether this is <dfn>equivalent</dfn> to another object.
     * </p>
     * <p>
     * The {@link HarmonicVector3} class has <i>value semantics</i>: this object is
     * equivalent to another if, only only if, the other object is also a
     * {@code HarmonicVector3}, and the two have equivaletn attributes.
     * </p>
     *
     * @param obj
     *            The other object
     * @return Whether equivalent.
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HarmonicVector3)) {
            return false;
        }
        final HarmonicVector3 other = (HarmonicVector3) obj;
        return Double.doubleToLongBits(we) == Double.doubleToLongBits(other.we)
                && Double.doubleToLongBits(wh) == Double.doubleToLongBits(other.wh) && t0.equals(other.t0)
                && Arrays.equals(termsArray, other.termsArray);
    }

    /**
     * <p>
     * The f<sub>0</sub> parameter; the constant term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the constant term; not null.
     */
    public final ImmutableVector3 getF0() {
        return termsArray[0];
    }

    /**
     * <p>
     * The f<sub>1</sub> parameter; the linear or velocity term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the velocity term; not null.
     */
    public final ImmutableVector3 getF1() {
        return termsArray[1];
    }

    /**
     * <p>
     * The f<sub>2</sub> parameter; the quadratic or acceleration term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the acceleration term; not null.
     */
    public final ImmutableVector3 getF2() {
        return termsArray[2];
    }

    /**
     * <p>
     * The f<sub>c</sub> parameter; the cosine term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the cosine term; not null.
     */
    public final ImmutableVector3 getFc() {
        return termsArray[3];
    }

    /**
     * <p>
     * The f<sub>s</sub> parameter; the sine term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the sine term; not null.
     */
    public final ImmutableVector3 getFs() {
        return termsArray[4];
    }

    /**
     * <p>
     * The t<sub>0</sub> parameter; the time origin.
     * </p>
     *
     * @return the the time origin; not null.
     */
    public final Duration getT0() {
        return t0;
    }

    /**
     * <p>
     * Compute the time derivative of this time varying vector.
     * </p>
     * <ul>
     * <li>Always have a (non null) time derivative.</li>
     * <li>The time derivative of this time varying vector has the same
     * {@linkplain #getT0() time origin} as this vector.</li>
     * <li>The time derivative of this time varying vector has an
     * {@linkplain #getWe() exponential frequency term} equal to the value of this
     * vector.</li>
     * <li>The time derivative of this time varying vector has a
     * {@linkplain #getWh() harmonic frequency term} equal to the value of this
     * vector.</li>
     * </ul>
     *
     * @return the derivative.
     */
    @NonNull
    public final HarmonicVector3 getTimeDerivative() {
        return new HarmonicVector3(t0, getF1(), getF2().scale(2), ImmutableVector3.ZERO,
                ImmutableVector3.sum(getFc().scale(we), getFs().scale(wh)),
                ImmutableVector3.sum(getFc().scale(-wh), getFs().scale(we)), we, wh);
    }

    /**
     * <p>
     * The &omega;<sub>e</sub> parameter; the exponential frequency term.
     * </p>
     * <p>
     * This has units of Herz (Hz, s<sup>-1</sup>).
     * </p>
     *
     * @return the exponential frequency term
     */
    public final double getWe() {
        return we;
    }

    /**
     * <p>
     * The &omega;<sub>h</sub> parameter; the harmonic frequency term.
     * </p>
     * <p>
     * This has units of Herz (Hz, s<sup>-1</sup>).
     * </p>
     *
     * @return the harmonic frequency term
     */
    public final double getWh() {
        return wh;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + t0.hashCode();
        result = prime * result + Arrays.hashCode(termsArray);
        long temp;
        temp = Double.doubleToLongBits(we);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(wh);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }
}
