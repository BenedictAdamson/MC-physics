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
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * A Strategy for mapping from an object representation of a {@link Duration} to
 * (part of) a state-space representation, and vice versa.
 * </p>
 */
@Immutable
public final class DurationMapper implements ObjectStateSpaceMapper<Duration> {

    private final int index0;
    private final Duration scale;

    /**
     * <p>
     * Construct a mapper object that maps a {@link Duration} to a contiguous
     * sequence of state vector components.
     * </p>
     * <ul>
     * <li>This mapper {@linkplain #isValidForDimension(int) is valid for a state
     * space dimension vector} if, and only if, the dimension is at least 1 more
     * than the given index position origin.</li>
     * </ul>
     *
     * @param index0
     *            The index position origin: the position in the state-space vector
     *            of the components that map to the components of the Duration.
     * @param scale
     *            The time scale to use for converting durations to and from a real
     *            number
     * @throws NullPointerException
     *             If {@code scale} is null.
     * @throws IllegalArgumentException
     *             <ul>
     *             <li>If {@code index0} is negative.</li>
     *             <li>If {@code scale} is {@linkplain Duration#ZERO}.</li>
     *             </ul>
     */
    public DurationMapper(final int index0, @NonNull final Duration scale) {
        Objects.requireNonNull(scale, "scale");
        if (index0 < 0) {
            throw new IllegalArgumentException("index0 " + index0);
        }
        if (Duration.ZERO.equals(scale)) {
            throw new IllegalArgumentException("scale is zero");
        }

        this.index0 = index0;
        this.scale = scale;
    }

    @Override
    public final void fromObject(@NonNull final double[] state, @NonNull final Duration object) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(object, "object");
        if (!isValidForDimension(state.length)) {
            throw new IllegalArgumentException("state.length " + state.length + " index0 " + index0);
        }

        state[index0] += object.dividedBy(scale);
    }

    @Override
    public final boolean isValidForDimension(final int n) {
        return index0 < n;
    }

    @Override
    public final @NonNull Duration toObject(@NonNull final ImmutableVectorN state) {
        Objects.requireNonNull(state, "state");
        return scale.multipliedBy((long) state.get(index0));
    }

}
