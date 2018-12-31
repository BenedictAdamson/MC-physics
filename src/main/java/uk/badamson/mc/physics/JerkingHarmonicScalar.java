package uk.badamson.mc.physics;
/*
 * © Copyright Benedict Adamson 2018.
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

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;

/**
 * <p>
 * A {@linkplain TimeVaryingScalar functor for a time varying scalar property}
 * that can have <i>jerk</i> and <i>damped harmonic</i> variation.
 * </p>
 * <p>
 * The class allows for a linear <dfn>jerk</dfn>; that is, a linear variation of
 * its acceleration (second derivative), and thus a cubic variation in its
 * value.
 * </p>
 * <p>
 * The class allows for <dfn>damped harmonic</dfn> variation; that is, a
 * decaying sinusoidal variation of its value. That also allows for
 * exponentially decaying and growing variation as a special case of zero
 * frequency of the sinusoidal variation.
 * </p>
 * <p>
 * The timewise variation of the scalar is given by the expression
 * </p>
 * <p>
 * f(t) = f<sub>0</sub> + f<sub>1</sub>&tau; + f<sub>2</sub>&tau;<sup>2</sup> +
 * f<sub>3</sub>&tau;<sup>3</sup> + e<sup>&tau;</sup>(f<sub>c</sub>cos &alpha; +
 * f<sub>s</sub>sin &alpha;)
 * </p>
 * <p>
 * where &tau; = &omega;<sub>e</sub>(t - t<sub>0</sub>), and &alpha; =
 * &omega;<sub>h</sub>(t - t<sub>0</sub>), with t<sub>0</sub>, f<sub>0</sub>,
 * f<sub>c</sub>, f<sub>s</sub>, f<sub>1</sub>, f<sub>2</sub>, f<sub>3</sub>,
 * &omega;<sub>e</sub> and &omega;<sub>h</sub> being constant parameters.
 * </p>
 * 
 * @see JerkingHarmonicVector3
 */
@Immutable
public final class JerkingHarmonicScalar extends AbstractTimeVaryingScalar {

    private final Duration t0;
    private final double f0;
    private final double fc;
    private final double fs;
    private final double f1;
    private final double f2;
    private final double f3;
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
     * @param f3
     *            The f<sub>3</sub> parameter; the cubic or jerk term. This has the
     *            same units as the function value.
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
     *             If {@code t0} is null.
     */
    public JerkingHarmonicScalar(@NonNull final Duration t0, final double f0, final double f1, final double f2,
            final double f3, final double fc, final double fs, final double we, final double wh) {
        this.t0 = Objects.requireNonNull(t0, "t0");
        this.f0 = f0;
        this.fc = fc;
        this.fs = fs;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.we = we;
        this.wh = wh;
    }

    @Override
    public final double at(@NonNull final Duration t) {
        Objects.requireNonNull(t, "t");
        final Duration tr = t.minus(t0);
        final double ts = tr.getSeconds() + 1E-9 * tr.getNano();
        final double tau = we * ts;
        final double alpha = wh * ts;
        final double tau2 = tau * tau;
        return f0 + f1 * tau + f2 * tau2 + f3 * tau2 * tau
                + Math.exp(tau) * (fc * Math.cos(alpha) + fs * Math.sin(alpha));
    }

    /**
     * <p>
     * The f<sub>0</sub> parameter; the constant term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the constant term
     */
    public final double getF0() {
        return f0;
    }

    /**
     * <p>
     * The f<sub>1</sub> parameter; the linear or velocity term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the velocity term
     */
    public final double getF1() {
        return f1;
    }

    /**
     * <p>
     * The f<sub>2</sub> parameter; the quadratic or acceleration term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the acceleration term
     */
    public final double getF2() {
        return f2;
    }

    /**
     * <p>
     * The f<sub>3</sub> parameter; the cubic or jerk term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the jerk term
     */
    public final double getF3() {
        return f3;
    }

    /**
     * <p>
     * The f<sub>c</sub> parameter; the cosine term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the cosine term
     */
    public final double getFc() {
        return fc;
    }

    /**
     * <p>
     * The f<sub>s</sub> parameter; the sine term.
     * </p>
     * <p>
     * This has the same units as the function value.
     * </p>
     *
     * @return the sine term.
     */
    public final double getFs() {
        return fs;
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

}
