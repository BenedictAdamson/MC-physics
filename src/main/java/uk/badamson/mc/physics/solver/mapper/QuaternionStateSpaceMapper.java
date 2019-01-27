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
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Quaternion;

/**
 * <p>
 * A Strategy for mapping from an object representation of a
 * {@linkplain Quaternion quaternion} to (part of) a state-space representation,
 * and vice versa.
 * </p>
 * <p>
 * The mapper maps 4 contiguous components of the state-space vector to the
 * components of the quaternion.
 * </p>
 */
@Immutable
public final class QuaternionStateSpaceMapper implements ObjectStateSpaceMapper<Quaternion> {

    private final int index0;

    /**
     * @param index0
     *            The position in the state-space vector of the components that map
     *            to the components of the quaternion. If the state-space is
     *            {@linkplain ImmutableVectorN vector} <var>v</var>,
     *            {@linkplain ImmutableVectorN#get(int) component}
     *            <var>v</var><sub>index0</sub> is the real component of the
     *            quaternion, <var>v</var><sub>index0+1</sub> is the <b>i</b>
     *            component, <var>v</var><sub>index0+2</sub> is the <b>j</b>
     *            component, and <var>v</var><sub>index0+3</sub> is the <b>k</b>
     *            component.
     * @throws IllegalArgumentException
     *             If {@code index0} is negative
     */
    public QuaternionStateSpaceMapper(final int index0) {
        if (index0 < 0) {
            throw new IllegalArgumentException("index0 " + index0);
        }
        this.index0 = index0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             {@inheritDoc}
     * @throws RuntimeException
     *             {@inheritDoc}
     */
    @Override
    public final void fromObject(@NonNull final double[] state, @NonNull final Quaternion object) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(object, "object");
        state[index0] += object.getA();
        state[index0 + 1] += object.getB();
        state[index0 + 2] += object.getC();
        state[index0 + 3] += object.getD();
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
        return index0 + 3 < n;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             {@inheritDoc}
     * @throws RuntimeException
     *             {@inheritDoc}
     */
    @Override
    public final @NonNull Quaternion toObject(@NonNull final ImmutableVectorN state) {
        Objects.requireNonNull(state, "state");
        return Quaternion.create(state.get(index0), state.get(index0 + 1), state.get(index0 + 2),
                state.get(index0 + 3));
    }

}
