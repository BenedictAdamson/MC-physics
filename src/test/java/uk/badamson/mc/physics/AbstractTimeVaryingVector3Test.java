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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * Unit tests and auxiliary test code for the {@link AbstractTimeVaryingVector3}
 * class.
 */
public class AbstractTimeVaryingVector3Test {

    public static ImmutableVector3 apply(final AbstractTimeVaryingVector3 v, final Duration value) {
        final ImmutableVector3 result = v.apply(value);

        assertInvariants(v);
        assertEquals(v.at(value), result, "The apply(Duration) method simply delegates to the at(Duration) method.");

        return result;
    }

    public static void assertInvariants(final AbstractTimeVaryingVector3 v) {
        ObjectTest.assertInvariants(v);// inherited
        TimeVaryingVector3Test.assertInvariants(v);// inherited
    }

    public static void assertInvariants(final AbstractTimeVaryingVector3 v1, final AbstractTimeVaryingVector3 v2) {
        ObjectTest.assertInvariants(v1, v2);// inherited
        TimeVaryingVector3Test.assertInvariants(v1, v2);// inherited
    }

    public static void assertInvariants(final AbstractTimeVaryingVector3 v, final Duration t) {
        assertEquals(v.at(t), v.apply(t), "The apply(Duration) method simply delegates to the at(Duration) method.");
    }

    public static ImmutableVector3 at(final AbstractTimeVaryingVector3 s, final Duration t) {
        final ImmutableVector3 result = TimeVaryingVector3Test.at(s, t);// inherited

        assertInvariants(s);

        return result;
    }
}
