package uk.badamson.mc.physics.solver.mapper;
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

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.physics.HarmonicVector3;

/**
 * <p>
 * A Strategy for mapping from an {@linkplain HarmonicVector3 object
 * representation of a time varying 3D vector property that can have damped
 * harmonic variation} to (part of) a state-space representation, and vice
 * versa.
 * </p>
 */
@Immutable
public final class HarmonicVector3Mapper implements ObjectStateSpaceMapper<HarmonicVector3> {

    private final int index0;

    private final DurationMapper t0Mapper;

    private final ImmutableVector3StateSpaceMapper f0Mapper;

    private final ImmutableVector3StateSpaceMapper f1Mapper;

    private final ImmutableVector3StateSpaceMapper f2Mapper;

    private final ImmutableVector3StateSpaceMapper fcMapper;

    private final ImmutableVector3StateSpaceMapper fsMapper;

    /**
     * <p>
     * Construct a mapper object that maps a {@link ImmutableVector3} to a
     * contiguous sequence of state vector components.
     * </p>
     * <ul>
     * <li>This mapper {@linkplain #getMinimumStateSpaceDimension(int) is valid for
     * a state space dimension vector} if, and only if, the dimension is greater
     * than 19 more than the given index position origin.</li>
     * </ul>
     *
     * @param index0
     *            The index position origin: the position in the state-space vector
     *            of the representation of the {@linkplain HarmonicVector3 time
     *            varying 3D vector}. If the state-space is
     *            {@linkplain ImmutableVectorN vector} <var>v</var>,
     *            {@linkplain ImmutableVectorN#get(int) component}
     *            <var>v</var><sub>index0</sub> through
     *            <var>v</var><sub>index0+20</sub> holdd the representation.
     * @param scale
     *            The time scale to use for converting durations to and from a real
     *            number
     * @throws NullPointerException
     *             If {@code scale} is null.
     * @throws IllegalArgumentException
     *             If {@code index0} is negative
     */
    public HarmonicVector3Mapper(final int index0, @NonNull final Duration scale) {
        if (index0 < 0) {
            throw new IllegalArgumentException("index0 " + index0);
        }
        this.index0 = index0;
        t0Mapper = new DurationMapper(index0 + 2, scale);
        f0Mapper = new ImmutableVector3StateSpaceMapper(t0Mapper.getIndex() + t0Mapper.getSize());
        f1Mapper = new ImmutableVector3StateSpaceMapper(f0Mapper.getIndex0() + f0Mapper.getSize());
        f2Mapper = new ImmutableVector3StateSpaceMapper(f1Mapper.getIndex0() + f1Mapper.getSize());
        fcMapper = new ImmutableVector3StateSpaceMapper(f2Mapper.getIndex0() + f2Mapper.getSize());
        fsMapper = new ImmutableVector3StateSpaceMapper(fcMapper.getIndex0() + fcMapper.getSize());
    }

    @Override
    public final void fromObject(@NonNull final double[] state, @NonNull final HarmonicVector3 object) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(object, "object");

        state[getWeIndex()] = object.getWe();
        state[getWhIndex()] = object.getWh();
        t0Mapper.fromObject(state, object.getT0());
        f0Mapper.fromObject(state, object.getF0());
        f1Mapper.fromObject(state, object.getF1());
        f2Mapper.fromObject(state, object.getF2());
        fcMapper.fromObject(state, object.getFc());
        fsMapper.fromObject(state, object.getFs());
    }

    /**
     * <p>
     * The mapper for the {@linkplain HarmonicVector3#getF0() constant term} of the
     * functor.
     * </p>
     * <ul>
     * <li>Always have a (non null) mapper for the constant term of the
     * functor.</li>
     * <li>This mapper maps values contiguously, so the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() index origin} of the
     * mapper for the constant term of the functor is greater than or equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * <li>The mapper for the constant term maps to positions just after the
     * {@linkplain DurationMapper#getIndex position mapped to by} the
     * {@linkplain #getT0Mapper() duration term mapper}.</li>
     * </ul>
     *
     * @return the mapper
     */
    public final ImmutableVector3StateSpaceMapper getF0Mapper() {
        return f0Mapper;
    }

    /**
     * <p>
     * The mapper for the {@linkplain HarmonicVector3#getF1() linear term} of the
     * functor.
     * </p>
     * <ul>
     * <li>Always have a (non null) mapper for the linear term of the functor.</li>
     * <li>This mapper maps values contiguously, so the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() index origin} of the
     * mapper for the linear term of the functor is greater than or equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * <li>The mapper for the linear term maps to positions just after the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() positions} mapped to
     * by the {@linkplain #getF0Mapper() constant term mapper}.</li>
     * </ul>
     *
     * @return the mapper
     */
    public final ImmutableVector3StateSpaceMapper getF1Mapper() {
        return f1Mapper;
    }

    /**
     * <p>
     * The mapper for the {@linkplain HarmonicVector3#getF2() quadratic term} of the
     * functor.
     * </p>
     * <ul>
     * <li>Always have a (non null) mapper for the quadratic term of the
     * functor.</li>
     * <li>This mapper maps values contiguously, so the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() index origin} of the
     * mapper for the quadratic term of the functor is greater than or equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * <li>The mapper for the quadratic term maps to positions just after the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() positions} mapped to
     * by the {@linkplain #getF1Mapper() linear term mapper}.</li>
     * </ul>
     *
     * @return the mapper
     */
    public final ImmutableVector3StateSpaceMapper getF2Mapper() {
        return f2Mapper;
    }

    /**
     * <p>
     * The mapper for the {@linkplain HarmonicVector3#getFc() cosine term} of the
     * functor.
     * </p>
     * <ul>
     * <li>Always have a (non null) mapper for the cosine term of the functor.</li>
     * <li>This mapper maps values contiguously, so the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() index origin} of the
     * mapper for the cosine term of the functor is greater than or equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * <li>The mapper for the cosine term maps to positions just after the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() positions} mapped to
     * by the {@linkplain #getF2Mapper() quadratic term mapper}.</li>
     * </ul>
     *
     * @return the mapper
     */
    public final ImmutableVector3StateSpaceMapper getFcMapper() {
        return fcMapper;
    }

    /**
     * <p>
     * The mapper for the {@linkplain HarmonicVector3#getFs() sine term} of the
     * functor.
     * </p>
     * <ul>
     * <li>Always have a (non null) mapper for the sine term of the functor.</li>
     * <li>This mapper maps values contiguously, so the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() index origin} of the
     * mapper for the sine term of the functor is greater than or equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * <li>The mapper for the sine term maps to positions just after the
     * {@linkplain ImmutableVector3StateSpaceMapper#getIndex0() positions} mapped to
     * by the {@linkplain #getFcMapper() cosine term mapper}.</li>
     * </ul>
     *
     * @return the mapper
     */
    public final ImmutableVector3StateSpaceMapper getFsMapper() {
        return fsMapper;
    }

    /**
     * <p>
     * The index origin: the position in the state-space vector of the first
     * component of the functor.
     * </p>
     * <ul>
     * <li>The index origin is not negative.</li>
     * </ul>
     *
     * @return the index of the first component.
     */
    public final int getIndex0() {
        return index0;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The mapper maps to contiguous components, so the minimum state space
     * dimension is equal to the {@linkplain #getIndex0() index origin} plus the
     * {@linkplain #getSize() size}.</li>
     * </ul>
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getMinimumStateSpaceDimension() {
        return index0 + 18;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The size is 18.</li>
     * </ul>
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getSize() {
        return 2 + t0Mapper.getSize() + f0Mapper.getSize() + f1Mapper.getSize() + f2Mapper.getSize()
                + fcMapper.getSize() + fsMapper.getSize();
    }

    /**
     * <p>
     * The mapper for the {@linkplain HarmonicVector3#getT0() time origin} of the
     * functor.
     * </p>
     * <ul>
     * <li>Always have a (non null) mapper for the time origin of the functor.</li>
     * <li>This mapper maps values contiguously, so the
     * {@linkplain DurationMapper#getIndex() index} of the mapper for the time
     * origin of the functor is greater than or equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * <li>The mapper for the time origin maps to positions just after the
     * {@linkplain #getWhIndex() position of the harmonic frequency term}.</li>
     * </ul>
     *
     * @return the mapper
     */
    public final DurationMapper getT0Mapper() {
        return t0Mapper;
    }

    /**
     * <p>
     * The position in the state vector representation to and from which this maps
     * the {@linkplain HarmonicVector3#getWe() exponential frequency term}.
     * </p>
     * <ul>
     * <li>The index of the exponential frequency term is an index, and thus not
     * negative.</li>
     * <li>This mapper maps values contiguously, so the index of the exponential
     * frequency term is greater than or equal to the {@linkplain #getIndex0() index
     * origin} of this mapper.</li>
     * <li>The index of the exponential frequency term is equal to the
     * {@linkplain #getIndex0() index origin} of this mapper.</li>
     * </ul>
     *
     * @return the index
     */
    public final int getWeIndex() {
        return index0;
    }

    /**
     * <p>
     * The position in the state vector representation to and from which this maps
     * the {@linkplain HarmonicVector3#getWh() harmonic frequency term}.
     * </p>
     * <ul>
     * <li>The index of the harmonic frequency term is an index, and thus not
     * negative.</li>
     * <li>This mapper maps values contiguously, so the index of the harmonic
     * frequency term is greater than or equal to the {@linkplain #getIndex0() index
     * origin} of this mapper.</li>
     * <li>This maps the harmonic frequency term to the position just after the
     * {@linkplain #getWeIndex() exponential frequency term}.</li>
     * </ul>
     *
     * @return the index
     */
    public final int getWhIndex() {
        return index0 + 1;
    }

    @Override
    public final @NonNull HarmonicVector3 toObject(@NonNull final ImmutableVectorN state) {
        Objects.requireNonNull(state, "state");

        final double fe = state.get(index0);
        final double fh = state.get(index0 + 1);
        final Duration t0 = t0Mapper.toObject(state);
        final ImmutableVector3 f0 = f0Mapper.toObject(state);
        final ImmutableVector3 f1 = f1Mapper.toObject(state);
        final ImmutableVector3 f2 = f2Mapper.toObject(state);
        final ImmutableVector3 fc = fcMapper.toObject(state);
        final ImmutableVector3 fs = fsMapper.toObject(state);

        return new HarmonicVector3(t0, f0, f1, f2, fc, fs, fe, fh);
    }
}
