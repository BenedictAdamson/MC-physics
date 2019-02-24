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

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * A functor for a time varying 3D vector property.
 * </p>
 */
@FunctionalInterface
@Immutable
public interface TimeVaryingVector3 {

    /**
     * <p>
     * The value of the vector property for a given point in time.
     * </p>
     *
     * @param t
     *            The point in time, expressed as the duration since an (implied)
     *            epoch.
     * @return The value of the vector property; not null.
     * @throws NullPointerException
     *             If {@code t} is null.
     */
    public @NonNull ImmutableVector3 at(@NonNull Duration t);
}
