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
import uk.badamson.mc.math.Rotation3AxisAngle;

/**
 * <p>
 * A Strategy for mapping from an object representation of a
 * {@linkplain Rotation3AxisAngle 3D rotation} to (part of) a state-space
 * representation, and vice versa.
 * </p>
 * <p>
 * The mapper maps components of the state-space vector to the rotation axis and
 * angle of the rotation
 * </p>
 */
@Immutable
public final class Rotation3AxisAngleStateSpaceMapper implements ObjectStateSpaceMapper<Rotation3AxisAngle> {

    private final int rotationIndex;
    @NonNull
    private final ImmutableVector3StateSpaceMapper axisMapper;

    /**
     * @param rotationIndex
     *            The position in the state-space vector that is the rotation angle.
     * @throws NullPointerException
     *             If {@code axisMapper} is null
     * @throws IllegalArgumentException
     *             if {@code rotationIndex} is negative.
     */
    public Rotation3AxisAngleStateSpaceMapper(final int rotationIndex,
            @NonNull final ImmutableVector3StateSpaceMapper axisMapper) {
        if (rotationIndex < 0) {
            throw new IllegalArgumentException("rotationIndex " + rotationIndex);
        }
        this.rotationIndex = rotationIndex;
        this.axisMapper = Objects.requireNonNull(axisMapper, "axisMapper");
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
    public final void fromObject(@NonNull final double[] state, @NonNull final Rotation3AxisAngle object) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(object, "object");
        state[rotationIndex] += object.getAngle();
        axisMapper.fromObject(state, object.getAxis());
    }

    @Override
    public final boolean isValidForDimension(final int n) {
        return axisMapper.isValidForDimension(n) && rotationIndex < n;
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
    public final @NonNull Rotation3AxisAngle toObject(@NonNull final ImmutableVectorN state) {
        return Rotation3AxisAngle.valueOfAxisAngle(axisMapper.toObject(state), state.get(rotationIndex));
    }

}
