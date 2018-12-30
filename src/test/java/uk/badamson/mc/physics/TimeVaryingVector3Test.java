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

import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * Auxiliary test code for classes that implement the {@link TimeVaryingVector3}
 * interface.
 * </p>
 */
public class TimeVaryingVector3Test {

    static final Duration T_1 = Duration.ofSeconds(0);
    static final Duration T_2 = Duration.ofSeconds(2);
    static final Duration T_3 = Duration.ofSeconds(3);
    static final Duration T_4 = Duration.ofSeconds(5);

    public static void assertInvariants(final TimeVaryingVector3 v) {
        // Do nothing.
    }

    public static void assertInvariants(final TimeVaryingVector3 v, final Duration t) {
        // Do nothing.
    }

    public static void assertInvariants(final TimeVaryingVector3 v1, final TimeVaryingVector3 v2) {
        // Do nothing.
    }

    public static ImmutableVector3 at(final TimeVaryingVector3 v, final Duration t) {
        final ImmutableVector3 result = v.at(t);

        assertInvariants(v);

        return result;
    }
}
