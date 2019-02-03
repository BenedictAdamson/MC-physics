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

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector1;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * A Strategy for mapping from an object representation of a
 * {@linkplain ImmutableVector1 3D vector} to (part of) a state-space
 * representation, and vice versa.
 * </p>
 * <p>
 * The mapper maps one component of the state-space vector to the component of
 * the 1D vector.
 * </p>
 */
@Immutable
public final class ImmutableVector1StateSpaceMapper implements VectorStateSpaceMapper<ImmutableVector1> {

    private final int index;

    /**
     * <p>
     * Construct a mapper with a given index.
     * </p>
     * <ul>
     * <li>The {@linkplain #getIndex() index} of this mapper is equal to the given
     * index.</li>
     * </ul>
     *
     * @param index
     *            The position in the state-space vector of the component that maps
     *            to the component of the 1D vector. If the state-space is
     *            {@linkplain ImmutableVectorN vector} <var>v</var>,
     *            {@linkplain ImmutableVectorN#get(int) component}
     *            <var>v</var><sub>index</sub> is the component of the 1D vector.
     * @throws IllegalArgumentException
     *             If {@code index} is negative
     */
    public ImmutableVector1StateSpaceMapper(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index " + index);
        }
        this.index = index;
    }

    @Override
    public final void fromObject(@NonNull final double[] state, @NonNull final ImmutableVector1 object) {
        fromVector(state, object);
    }

    @Override
    public final void fromVector(@NonNull final double[] state, @NonNull final Vector vector) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(vector, "vector");
        if (state.length < getMinimumStateSpaceDimension()) {
            throw new IllegalArgumentException("state.length " + state.length + " index " + index);
        }
        if (vector.getDimension() != 1) {
            throw new IllegalArgumentException("vector dimension " + vector.getDimension());
        }
        state[index] = vector.get(0);
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The component index of the (sole) component is equal to the
     * {@linkplain #getIndex() index} of this mapper.</li>
     * </ul>
     *
     * @param i
     *            {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             {@inheritDoc}
     */
    @Override
    public final int getComponentIndex(final int i) {
        Objects.checkIndex(i, 1);
        return index;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The component index of the (sole) component is equal to the
     * {@linkplain #getIndex() index} of this mapper.</li>
     * </ul>
     *
     * @param i
     *            {@inheritDoc}
     * @param j
     *            {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             <ul>
     *             <li>If {@code i} is not 0.</li>
     *             <li>If {@code j} is not 0.</li>
     *             </ul>
     * @return {@inheritDoc}
     */
    @Override
    public final int getComponentIndex(final int i, final int j) {
        Objects.checkIndex(i, 1);
        Objects.checkIndex(j, 1);
        return index;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The dimension is 1.
     * </p>
     */
    @Override
    public final int getDimension() {
        return 1;
    }

    /**
     * <p>
     * The position in the state-space vector of the component that maps to the
     * component of the 1D vector.
     * </p>
     * <p>
     * If the state-space is {@linkplain ImmutableVectorN vector} <var>v</var>,
     * {@linkplain ImmutableVectorN#get(int) component} <var>v</var><sub>index</sub>
     * is the component of the 1D vector.
     * </p>
     *
     * @return the index; not negative.
     */
    public final int getIndex() {
        return index;
    }

    @Override
    public final int getMinimumStateSpaceDimension() {
        return index + 1;
    }

    @Override
    public final ImmutableVector1 toObject(@NonNull final ImmutableVectorN state) {
        Objects.requireNonNull(state, "state");
        if (state.getDimension() < getMinimumStateSpaceDimension()) {
            throw new IllegalArgumentException("state.dimension " + state.getDimension() + " index " + index);
        }
        return ImmutableVector1.create(state.get(index));
    }

}
