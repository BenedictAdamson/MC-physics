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
 * The mapper maps 3 contiguous components of the state-space vector to the
 * components of the 3D vector.
 * </p>
 */
@Immutable
public final class ImmutableVector3StateSpaceMapper implements VectorStateSpaceMapper<ImmutableVector3> {

    private final int index0;

    /**
     * @param index0
     *            The position in the state-space vector of the components that map
     *            to the components of the 3D vector. If the state-space is
     *            {@linkplain ImmutableVectorN vector} <var>v</var>,
     *            {@linkplain ImmutableVectorN#get(int) component}
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
        if (!isValidForDimension(state.length)) {
            throw new IllegalArgumentException("state.length " + state.length + " index0 " + index0);
        }
        state[index0] += vector.get(0);
        state[index0 + 1] += vector.get(1);
        state[index0 + 2] += vector.get(2);
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
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             {@inheritDoc}
     */
    @Override
    public final boolean isValidForDimension(final int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n " + n);
        }
        return index0 + 2 <= n;
    }

    @Override
    public final @NonNull ImmutableVector3 toObject(@NonNull final ImmutableVectorN state) {
        Objects.requireNonNull(state, "state");
        if (!isValidForDimension(state.getDimension())) {
            throw new IllegalArgumentException("state.dimension " + state.getDimension() + " index " + index0);
        }
        return ImmutableVector3.create(state.get(index0), state.get(index0 + 1), state.get(index0 + 2));
    }
}
