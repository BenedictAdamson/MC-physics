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

import uk.badamson.mc.ObjectTest;

/**
 * Unit tests and auxiliary test code for the {@link AbstractTimeVaryingScalar}
 * class.
 */
public class AbstractTimeVaryingScalarTest {

    public static void assertInvariants(AbstractTimeVaryingScalar s) {
        ObjectTest.assertInvariants(s);// inherited
        TimeVaryingScalarTest.assertInvariants(s);// inherited
    }

    public static void assertInvariants(AbstractTimeVaryingScalar s1, AbstractTimeVaryingScalar s2) {
        ObjectTest.assertInvariants(s1, s2);// inherited
        TimeVaryingScalarTest.assertInvariants(s1, s2);// inherited
    }

    public static double at(AbstractTimeVaryingScalar s, Duration t) {
        final double result = TimeVaryingScalarTest.at(s, t);// inherited

        assertInvariants(s);

        return result;
    }
}
