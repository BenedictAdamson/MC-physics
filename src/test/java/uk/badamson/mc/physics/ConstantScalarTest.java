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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * Unit tests and auxiliary test code for the {@link ConstantScalar} class.
 * </p>
 */
public class ConstantScalarTest {

    @Nested
    public class At {

        @Test
        public void a() {
            test(0.0, T_1);
        }

        @Test
        public void b() {
            test(-1.125, T_2);
        }

        private void test(final double value, final Duration t) {
            final ConstantScalar s = new ConstantScalar(value);
            at(s, t);
            assertInvariants(s, t);
        }
    }// class

    @Nested
    public class Constructor {

        @Test
        public void a() {
            test(0.0);
        }

        @Test
        public void b() {
            test(-1.125);
        }

        private ConstantScalar test(final double value) {
            final ConstantScalar s = new ConstantScalar(value);

            assertInvariants(s);
            assertEquals(Double.doubleToLongBits(value), Double.doubleToLongBits(s.getValue()),
                    "The value of this scalar is the given value.");

            return s;
        }

    }// class

    private static final Duration T_1 = TimeVaryingScalarTest.T_1;

    private static final Duration T_2 = TimeVaryingScalarTest.T_2;

    public static double applyAsDouble(final ConstantScalar s, final Duration value) {
        final double result = AbstractTimeVaryingScalarTest.applyAsDouble(s, value);

        assertInvariants(s);

        return result;
    }

    public static void assertInvariants(final ConstantScalar s) {
        AbstractTimeVaryingScalarTest.assertInvariants(s);// inherited
    }

    public static void assertInvariants(final ConstantScalar s1, final ConstantScalar s2) {
        AbstractTimeVaryingScalarTest.assertInvariants(s1, s2);// inherited
    }

    public static void assertInvariants(final ConstantScalar s, final Duration t) {
        AbstractTimeVaryingScalarTest.assertInvariants(s, t);// inherited
        assertEquals(Double.doubleToLongBits(s.getValue()), Double.doubleToLongBits(s.at(t)),
                "The value at all points in time is the constant value of this function.");
    }

    public static double at(final ConstantScalar s, final Duration t) {
        final double result = AbstractTimeVaryingScalarTest.at(s, t);// inherited

        assertInvariants(s);
        assertEquals(Double.doubleToLongBits(s.getValue()), Double.doubleToLongBits(result),
                "The value at all points in time is the constant value of this function.");

        return result;
    }
}
