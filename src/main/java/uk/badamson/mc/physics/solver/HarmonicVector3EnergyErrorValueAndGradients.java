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

import java.util.Objects;

import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;

/**
 * <p>
 * An error, and its rates of change with respect to the parameters of a
 * {@linkplain HarmonicVector3 functor}.
 * </p>
 */
@Immutable
public final class HarmonicVector3EnergyErrorValueAndGradients {

    /**
     * <p>
     * A value that has zero values for all its attributes.
     * </p>
     */
    public static final HarmonicVector3EnergyErrorValueAndGradients ZERO = new HarmonicVector3EnergyErrorValueAndGradients(
            0, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
            ImmutableVector3.ZERO, 0, 0);

    /**
     * <p>
     * A value that is equivalent to the sum of several values.
     * </p>
     * <ul>
     * <li>Always return a (non null) sum.</li>
     * <li>The sum of an empty sequence of values is {@linkplain #ZERO zero}.</li>
     * <li>The sum of a singleton sequence of values is that sole value.</li>
     * <li>The sum of a multi component sequence has attributes equal to the sum of
     * the corresponding attributes of the sequence.</li>
     * </ul>
     *
     * @param values
     *            The values to sum
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code values} is null.</li>
     *             <li>If {@code values} contains a null.</li>
     *             </ul>
     */
    public static HarmonicVector3EnergyErrorValueAndGradients sum(
            final HarmonicVector3EnergyErrorValueAndGradients... values) {
        Objects.requireNonNull(values, "values");
        if (values.length == 0) {
            // Optimization
            return HarmonicVector3EnergyErrorValueAndGradients.ZERO;
        } else if (values.length == 1) {
            // Optimization
            return values[0];
        } else {
            double e = 0;
            double dedwe = 0;
            double dedwh = 0;
            final ImmutableVector3[] dedf0Array = new ImmutableVector3[values.length];
            final ImmutableVector3[] dedf1Array = new ImmutableVector3[values.length];
            final ImmutableVector3[] dedf2Array = new ImmutableVector3[values.length];
            final ImmutableVector3[] dedfcArray = new ImmutableVector3[values.length];
            final ImmutableVector3[] dedfsArray = new ImmutableVector3[values.length];
            for (int v = 0; v < values.length; v++) {
                final HarmonicVector3EnergyErrorValueAndGradients value = values[v];
                e += value.getE();
                dedf0Array[v] = value.getDedf0();
                dedf1Array[v] = value.getDedf1();
                dedf2Array[v] = value.getDedf2();
                dedfcArray[v] = value.getDedfc();
                dedfsArray[v] = value.getDedfs();
                dedwe += value.getDedwe();
                dedwh += value.getDedwh();
            } // for
            final ImmutableVector3 dedf0 = ImmutableVector3.sum(dedf0Array);
            final ImmutableVector3 dedf1 = ImmutableVector3.sum(dedf1Array);
            final ImmutableVector3 dedf2 = ImmutableVector3.sum(dedf2Array);
            final ImmutableVector3 dedfc = ImmutableVector3.sum(dedfcArray);
            final ImmutableVector3 dedfs = ImmutableVector3.sum(dedfsArray);
            return new HarmonicVector3EnergyErrorValueAndGradients(e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);
        }
    }

    private final double e;
    private final ImmutableVector3 dedf0;
    private final ImmutableVector3 dedf1;
    private final ImmutableVector3 dedf2;
    private final ImmutableVector3 dedfc;
    private final ImmutableVector3 dedfs;
    private final double dedwe;
    private final double dedwh;

    /**
     * <p>
     * Construct a value with given attributes.
     * </p>
     *
     * @param e
     *            The energy error value.
     * @param dedf0
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the components of the
     *            {@linkplain HarmonicVector3#getF0() the constant term} of the
     *            {@linkplain HarmonicVector3 functor}.
     * @param dedf1
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the components of the
     *            {@linkplain HarmonicVector3#getF1() the linear term} of the
     *            {@linkplain HarmonicVector3 functor}.
     * @param dedf2
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the components of the
     *            {@linkplain HarmonicVector3#getF2() the quadratic term} of the
     *            {@linkplain HarmonicVector3 functor}.
     * @param dedfc
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the components of the
     *            {@linkplain HarmonicVector3#getFc() the cosine term} of the
     *            {@linkplain HarmonicVector3 functor}.
     * @param dedfs
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the components of the
     *            {@linkplain HarmonicVector3#getFs() the sine term} of the
     *            {@linkplain HarmonicVector3 functor}.
     * @param dedwe
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the {@linkplain HarmonicVector3#getWe() exponential
     *            frequency term} of the {@linkplain HarmonicVector3 functor}.
     * @param dedwh
     *            The rate of change of the {@linkplain #getE() error value} with
     *            respect to the {@linkplain HarmonicVector3#getWh() harmonic
     *            frequency term} of the {@linkplain HarmonicVector3 functor}.
     *
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code dedf0} is null</li>
     *             <li>If {@code dedf1} is null</li>
     *             <li>If {@code dedf2} is null</li>
     *             <li>If {@code dedfc} is null</li>
     *             <li>If {@code dedfs} is null</li>
     *             </ul>
     */
    public HarmonicVector3EnergyErrorValueAndGradients(final double e, final ImmutableVector3 dedf0,
            final ImmutableVector3 dedf1, final ImmutableVector3 dedf2, final ImmutableVector3 dedfc,
            final ImmutableVector3 dedfs, final double dedwe, final double dedwh) {
        this.e = e;
        this.dedf0 = dedf0;
        this.dedf1 = dedf1;
        this.dedf2 = dedf2;
        this.dedfc = dedfc;
        this.dedfs = dedfs;
        this.dedwe = dedwe;
        this.dedwh = dedwh;
    }

    /**
     * <p>
     * Whether this is <dfn>equivalent</dfn> to another object.
     * </p>
     * <p>
     * The {@link ErrorValueAndGradients} class has <i>value semantics</i>: this is
     * equivalent to another object if, and only if, the other object is also a
     * {@link ErrorValueAndGradients} and the two have equivalent attributes.
     * </p>
     *
     * @param obj
     *            The other object.
     * @return whether this and the other object are equivalent.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HarmonicVector3EnergyErrorValueAndGradients)) {
            return false;
        }
        final HarmonicVector3EnergyErrorValueAndGradients other = (HarmonicVector3EnergyErrorValueAndGradients) obj;
        return Double.doubleToLongBits(e) == Double.doubleToLongBits(other.e)
                && Double.doubleToLongBits(dedwe) == Double.doubleToLongBits(other.dedwe)
                && Double.doubleToLongBits(dedwh) == Double.doubleToLongBits(other.dedwh) && dedf0.equals(other.dedf0)
                && dedf1.equals(other.dedf1) && dedf2.equals(other.dedf2) && dedfc.equals(other.dedfc)
                && dedfs.equals(other.dedfs);
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the components of the {@linkplain HarmonicVector3#getF0() the constant term}
     * of the {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final ImmutableVector3 getDedf0() {
        return dedf0;
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the components of the {@linkplain HarmonicVector3#getF1() the linear term} of
     * the {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final ImmutableVector3 getDedf1() {
        return dedf1;
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the components of the {@linkplain HarmonicVector3#getF2() the quadratic term}
     * of the {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final ImmutableVector3 getDedf2() {
        return dedf2;
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the components of the {@linkplain HarmonicVector3#getFc() the cosine term} of
     * the {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final ImmutableVector3 getDedfc() {
        return dedfc;
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the components of the {@linkplain HarmonicVector3#getFs() the sine term} of
     * the {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final ImmutableVector3 getDedfs() {
        return dedfs;
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the {@linkplain HarmonicVector3#getWe() exponential frequency term} of the
     * {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final double getDedwe() {
        return dedwe;
    }

    /**
     * <p>
     * The rate of change of the {@linkplain #getE() error value} with respect to
     * the {@linkplain HarmonicVector3#getWh() harmonic frequency term} of the
     * {@linkplain HarmonicVector3 functor}.
     * </p>
     *
     * @return the rate of change; not null.
     */
    public final double getDedwh() {
        return dedwh;
    }

    /**
     * <p>
     * The energy error value.
     * </p>
     *
     * @return the error value.
     */
    public final double getE() {
        return e;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(dedwe);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(dedwh);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(e);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + dedf0.hashCode();
        result = prime * result + dedf1.hashCode();
        result = prime * result + dedf2.hashCode();
        result = prime * result + dedfc.hashCode();
        result = prime * result + dedfs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ErrorValueAndGradients [e=" + e + ", dedf0=" + dedf0 + ", dedf1=" + dedf1 + ", dedf2=" + dedf2
                + ", dedfc=" + dedfc + ", dedfs=" + dedfs + ", dedwe=" + dedwe + ", dedwh=" + dedwh + "]";
    }

}// class