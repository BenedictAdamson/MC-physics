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

import java.util.Set;

import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.FunctionNWithGradient;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.MinN;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.TimeVaryingVector3;
import uk.badamson.mc.physics.solver.mapper.HarmonicVector3Mapper;

/**
 * <p>
 * A {@linkplain FunctionNWithGradient functor} that calculates the physical
 * modelling error,which has dimensions of energy, of a
 * {@linkplain HarmonicVector3 functor for a time varying 3D vector property
 * that can have damped harmonic variation}.
 * </p>
 */
@Immutable
public final class HarmonicVector3EnergyErrorFunction implements EnergyErrorFunction {

    /**
     * <p>
     * An error, and its rates of change with respect to the parameters of a  {@linkplain HarmonicVector3 functor}.
     * </p>
     */
    @Immutable
    public static final class ErrorValueAndGradients {
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
         *            @throws NullPointerException
         *            <ul>
         *            <li>If {@code dedf0} is null</li>
         *            <li>If {@code dedf1} is null</li>
         *            <li>If {@code dedf2} is null</li>
         *            <li>If {@code dedfc} is null</li>
         *            <li>If {@code dedfs} is null</li>
         *            </ul>
         */
        public ErrorValueAndGradients(double e, ImmutableVector3 dedf0, ImmutableVector3 dedf1, ImmutableVector3 dedf2,
                ImmutableVector3 dedfc, ImmutableVector3 dedfs, double dedwe, double dedwh) {
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
         * The energy error value.
         * </p>
         * 
         * @return the error value.
         */
        public final double getE() {
            return e;
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

    }// class

    /**
     * <p>
     * A contributor to the {@linkplain HarmonicVector3EnergyErrorFunction physical
     * modelling error of a functor for a time varying 3D vector property that can
     * have damped harmonic variation}.
     * </p>
     * <p>
     * The term calculates an error value that has dimensions of energy.
     * </p>
     */
    @Immutable
    public interface Term {

        /**
         * <p>
         * Calculate the value of this error term for a given functor,
         *  and its rates of change with respect to the parameters of the functor
     * </p>
         *
         * @param f
         *            The functor for which the error term is to be computed.
         * @return the value, which has dimensions of energy.
         *
         * @throws NullPointerException
         *             If {@code f} is null.
         */
        public ErrorValueAndGradients evaluate(HarmonicVector3 f);

    }// interface

    private final HarmonicVector3Mapper mapper;
    private final Set<Term> terms;
    
    

    /**
     * @param mapper
     * The Strategy for mapping from the {@linkplain HarmonicVector3 object
     * representation} of the time varying 3D vector property to (part of) a
     * state-space representation, and vice versa.
     * @param terms
     * The contributors to this function
     */
    public HarmonicVector3EnergyErrorFunction(HarmonicVector3Mapper mapper, Set<Term> terms) {
        this.mapper = mapper;
        this.terms = terms;
    }

    /**
     * <p>
     * The contributors to this function
     * </p>
     * <ul>
     * <li>Always have a (non null) set of terms.</li>
     * <li>The set of terms does not contain a null term.</li>
     * <li>The set of terms is not modifiable.</li>
     * </ul>
     */
    public final Set<Term> getTerms() {
        return terms;
    }

    /**
     * <p>
     * The Strategy for mapping from the {@linkplain HarmonicVector3 object
     * representation} of the time varying 3D vector property to (part of) a
     * state-space representation, and vice versa.
     * </p>
     * 
     * @return the mapper; not null
     */
    public final HarmonicVector3Mapper getMapper() {
        return mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @param state
     *            {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public FunctionNWithGradientValue value(ImmutableVectorN state) {
        // TODO Auto-generated method stub
        return null;
    }

}
