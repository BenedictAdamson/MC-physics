package uk.badamson.mc.physics;
/*
 * © Copyright Benedict Adamson 2018.
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
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * A skeleton implementation of a functor for a time varying 3D vector property.
 * </p>
 * 
 * @see AbstractTimeVaryingScalar
 */
@Immutable
public abstract class AbstractTimeVaryingVector3 implements TimeVaryingVector3, Function<Duration, ImmutableVector3> {

    protected AbstractTimeVaryingVector3() {
        // Do nothing
    }

    /**
     * <p>
     * Applies this function to the given argument.
     * </p>
     * <ul>
     * <li>The {@link #apply(Duration)} method simply delegates to the
     * {@link #at(Duration)} method.</li>
     * </ul>
     *
     * @param value
     *            the function argument
     * @return the function result.
     * @throws NullPointerException
     *             If {@code value} is null.
     */
    @Override
    public final @NonNull ImmutableVector3 apply(@NonNull final Duration value) {
        return at(value);
    }
}
