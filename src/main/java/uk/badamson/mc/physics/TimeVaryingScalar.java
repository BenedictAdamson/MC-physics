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

/**
 * <p>
 * A functor for a time varying scalar property.
 * </p>
 */
@FunctionalInterface
@Immutable
public interface TimeVaryingScalar {

    /**
     * <p>
     * The value of the scalar property for a given point in time.
     * </p>
     *
     * @param t
     *            The point in time, expressed as the duration since an (implied)
     *            epoch.
     * @return The value of the scalar property.
     * @throws NullPointerException
     *             If {@code t} is null.
     */
    public double at(@NonNull Duration t);
}
