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

/**
 * Unit tests and auxiliary test code for the {@link AbstractTimeVaryingScalar}
 * class.
 */
public class AbstractTimeVaryingScalarTest {

    public static double applyAsDouble(final AbstractTimeVaryingScalar s, final Duration value) {
        final double result = s.applyAsDouble(value);

        assertInvariants(s);
        assertEquals(Double.doubleToLongBits(s.at(value)), Double.doubleToLongBits(result),
                "The applyAsDouble(Duration) method simply delegates to the at(Duration) method.");

        return result;
    }

    public static void assertInvariants(final AbstractTimeVaryingScalar s) {
        ObjectTest.assertInvariants(s);// inherited
        TimeVaryingScalarTest.assertInvariants(s);// inherited
    }

    public static void assertInvariants(final AbstractTimeVaryingScalar s1, final AbstractTimeVaryingScalar s2) {
        ObjectTest.assertInvariants(s1, s2);// inherited
        TimeVaryingScalarTest.assertInvariants(s1, s2);// inherited
    }

    public static void assertInvariants(final AbstractTimeVaryingScalar s, final Duration t) {
        assertEquals(Double.doubleToLongBits(s.at(t)), Double.doubleToLongBits(s.applyAsDouble(t)),
                "The applyAsDouble(Duration) method simply delegates to the at(Duration) method.");
    }

    public static double at(final AbstractTimeVaryingScalar s, final Duration t) {
        final double result = TimeVaryingScalarTest.at(s, t);// inherited

        assertInvariants(s);

        return result;
    }
}
