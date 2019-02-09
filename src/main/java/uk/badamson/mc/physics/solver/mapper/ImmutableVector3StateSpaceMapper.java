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
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * A Strategy for mapping from an object representation of a
 * {@linkplain ImmutableVector3 3D vector} to (part of) a state-space
 * representation, and vice versa.
 * </p>
 * <p>
 * The mapper maps 3 contiguous components of the state-space vector to
 * represent the components of the 3D vector.
 * </p>
 */
@Immutable
public final class ImmutableVector3StateSpaceMapper implements VectorStateSpaceMapper<ImmutableVector3> {

    private final int index0;

    /**
     * <p>
     * Construct a mapper object that maps a {@link ImmutableVector3} to a
     * contiguous sequence of state vector components.
     * </p>
     * <ul>
     * <li>The {@linkplain #getIndex0() index position origin} of this mapper is
     * equal to the given index position origin.</li>
     * </ul>
     *
     * @param index0
     *            The index position origin: the position in the state-space vector
     *            of the components that map to the components of the 3D vector. If
     *            the state-space is {@linkplain ImmutableVectorN vector}
     *            <var>v</var>, {@linkplain ImmutableVectorN#get(int) component}
     *            <var>v</var><sub>index0</sub> is the x component of the 3D vector,
     *            <var>v</var><sub>index0+1</sub> is the y component, and
     *            <var>v</var><sub>index0+2</sub> is the z component.
     * @throws IllegalArgumentException
     *             If {@code index0} is negative
     */
    public ImmutableVector3StateSpaceMapper(final int index0) {
        if (index0 < 0) {
            throw new IllegalArgumentException("index0 " + index0);
        }
        this.index0 = index0;
    }

    @Override
    public final void fromObject(@NonNull final double[] state, @NonNull final ImmutableVector3 object) {
        fromVector(state, object);
    }

    @Override
    public final void fromVector(@NonNull final double[] state, @NonNull final Vector vector) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(vector, "vector");
        if (state.length < getMinimumStateSpaceDimension()) {
            throw new IllegalArgumentException("state.length " + state.length + " index0 " + index0);
        }

        state[index0] = vector.get(0);
        state[index0 + 1] = vector.get(1);
        state[index0 + 2] = vector.get(2);
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The component index of row <var>i</var> is equal to the
     * {@linkplain #getIndex0() index origin} of this mapper plus <var>i</var>.</li>
     * </ul>
     *
     * @param i
     *            {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             <ul>
     *             <li>If {@code i} is negative.</li>
     *             <li>If {@code i} is greater than or equal to the number of
     *             {@linkplain #getDimension() dimensions} of this mapper.</li>
     *             </ul>
     */
    @Override
    public final int getComponentIndex(final int i) {
        Objects.checkIndex(i, getDimension());
        return index0 + i;
    }

    /**
     * {@inheritDoc}
     *
     * @param i
     *            {@inheritDoc}
     * @param j
     *            {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             <ul>
     *             <li>If {@code i} is negative.</li>
     *             <li>If {@code i} is greater than or equal to the number of
     *             {@linkplain #getDimension() dimensions} of this mapper.</li>
     *             <li>If {@code j} is not 0.</li>
     *             </ul>
     * @return {@inheritDoc}
     */
    @Override
    public final int getComponentIndex(final int i, final int j) {
        Objects.checkIndex(i, getDimension());
        Objects.checkIndex(j, 1);
        return index0 + i;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The dimension is 3.
     * </p>
     */
    @Override
    public final int getDimension() {
        return 3;
    }

    /**
     * <p>
     * The indexes origin: the position in the state-space vector of the first
     * component of the 3D vector.
     * </p>
     * <ul>
     * <li>The indexes origin is not negative.</li>
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
        return index0 + 3;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The size is 3.</li>
     * </ul>
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getSize() {
        return 3;
    }

    @Override
    public final @NonNull ImmutableVector3 toObject(@NonNull final ImmutableVectorN state) {
        Objects.requireNonNull(state, "state");
        if (state.getDimension() < getMinimumStateSpaceDimension()) {
            throw new IllegalArgumentException("state.dimension " + state.getDimension() + " index " + index0);
        }
        return ImmutableVector3.create(state.get(index0), state.get(index0 + 1), state.get(index0 + 2));
    }
}
