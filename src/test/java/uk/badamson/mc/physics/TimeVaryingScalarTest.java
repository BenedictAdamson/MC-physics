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

/**
 * <p>
 * Auxiliary test code for classes that implement the {@link TimeVaryingScalar}
 * interface.
 * </p>
 */
public class TimeVaryingScalarTest {

    public static void assertInvariants(TimeVaryingScalar s) {
        // Do nothing.
    }

    public static void assertInvariants(TimeVaryingScalar s1, TimeVaryingScalar s2) {
        // Do nothing.
    }

    public static double at(TimeVaryingScalar s, Duration t) {
        final double result = s.at(t);

        assertInvariants(s);

        return result;
    }
}
