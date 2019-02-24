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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * Unit tests and auxiliary test code for the {@link ConstantVector3} class.
 * </p>
 */
public class ConstantVector3Test {

    @Nested
    public class At {

        @Test
        public void a() {
            test(V_1, T_1);
        }

        @Test
        public void b() {
            test(V_2, T_2);
        }

        private void test(final ImmutableVector3 value, final Duration t) {
            final ConstantVector3 v = new ConstantVector3(value);
            at(v, t);
            assertInvariants(v, t);
        }
    }// class

    @Nested
    public class Constructor {

        @Test
        public void a() {
            test(V_1);
        }

        @Test
        public void b() {
            test(V_2);
        }

        private ConstantVector3 test(final ImmutableVector3 value) {
            final ConstantVector3 v = new ConstantVector3(value);

            assertInvariants(v);
            assertSame(value, v.getValue(), "The value of this scalar is the given value.");

            return v;
        }

    }// class

    private static final Duration T_1 = TimeVaryingScalarTest.T_1;

    private static final Duration T_2 = TimeVaryingScalarTest.T_2;

    private static final ImmutableVector3 V_1 = ImmutableVector3.create(1, 2, 3);

    private static final ImmutableVector3 V_2 = ImmutableVector3.create(4, 3, 2);

    public static ImmutableVector3 apply(final ConstantVector3 v, final Duration value) {
        final ImmutableVector3 result = AbstractTimeVaryingVector3Test.apply(v, value);

        assertInvariants(v);

        return result;
    }

    public static void assertInvariants(final ConstantVector3 v) {
        AbstractTimeVaryingVector3Test.assertInvariants(v);// inherited

        assertNotNull(v.getValue(), "Not null, value");
    }

    public static void assertInvariants(final ConstantVector3 v1, final ConstantVector3 v2) {
        AbstractTimeVaryingVector3Test.assertInvariants(v1, v2);// inherited
    }

    public static void assertInvariants(final ConstantVector3 v, final Duration t) {
        AbstractTimeVaryingVector3Test.assertInvariants(v, t);// inherited
        assertSame(v.getValue(), v.at(t), "The value at all points in time is the constant value of this function.");
    }

    public static ImmutableVector3 at(final ConstantVector3 v, final Duration t) {
        final ImmutableVector3 result = AbstractTimeVaryingVector3Test.at(v, t);// inherited

        assertInvariants(v);
        assertSame(v.getValue(), result, "The value at all points in time is the constant value of this function.");

        return result;
    }
}
