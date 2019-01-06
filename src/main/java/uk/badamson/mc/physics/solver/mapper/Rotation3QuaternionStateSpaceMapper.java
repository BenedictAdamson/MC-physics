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
import uk.badamson.mc.math.Rotation3Quaternion;

/**
 * <p>
 * A Strategy for mapping from an object representation of a
 * {@linkplain Rotation3Quaternion 3D rotation} to (part of) a state-space
 * representation, and vice versa.
 * </p>
 * <p>
 * The mapper maps 4 contiguous components of the state-space vector to the
 * components of the quaternion.
 * </p>
 */
@Immutable
public final class Rotation3QuaternionStateSpaceMapper implements ObjectStateSpaceMapper<Rotation3Quaternion> {

    @NonNull
    private final QuaternionStateSpaceMapper quaternionMapper;

    /**
     * @param quaternionMapper
     *            A Strategy for mapping from an object representation of a
     *            {@linkplain Quaternion quaternion} to (part of) a state-space
     *            representation, and vice versa. Used to map to and from the
     *            representation of teh rotation as a quaternion.
     * @throws NullPointerException
     *             If {@code quaternionMapper} is null
     */
    public Rotation3QuaternionStateSpaceMapper(@NonNull final QuaternionStateSpaceMapper quaternionMapper) {
        this.quaternionMapper = Objects.requireNonNull(quaternionMapper, "quaternionMapper");
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
    public final void fromObject(@NonNull final double[] state, @NonNull final Rotation3Quaternion object) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(object, "object");
        quaternionMapper.fromObject(state, object.getVersor());
    }

    @Override
    public final boolean isValidForDimension(final int n) {
        return quaternionMapper.isValidForDimension(n);
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
    public final @NonNull Rotation3Quaternion toObject(@NonNull final ImmutableVectorN state) {
        return Rotation3Quaternion.valueOf(quaternionMapper.toObject(state));
    }

}
