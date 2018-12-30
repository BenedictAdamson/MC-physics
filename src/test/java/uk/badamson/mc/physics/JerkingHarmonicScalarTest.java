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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * <p>
 * Unit tests for the {@link JerkingHarmonicScalar} class.
 * </p>
 */
public class JerkingHarmonicScalarTest {

    @Nested
    public class At {

        @Nested
        public class Constant {

            @Test
            public void a() {
                final Duration t0 = T_1;
                final double f0 = 1.0;
                final double we = 1.0;
                final Duration t = T_2;
                testConstant(t0, f0, we, t);
            }

            @Test
            public void b() {
                final Duration t0 = T_2;
                final double f0 = -1.125;
                final double we = 2.0;
                final Duration t = T_3;
                testConstant(t0, f0, we, t);
            }

            private void testConstant(@NonNull final Duration t0, final double f0, final double we, final Duration t) {
                final double fc = 0.0;
                final double fs = 0.0;
                final double f1 = 0.0;
                final double f2 = 0.0;
                final double f3 = 0.0;
                final double wh = 0.0;
                final double expected = f0;
                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Cos {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = Math.cos(1.0);

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final double fc = 1.0;
                final double wh = 1.0;
                final Duration t = t0;
                final double expected = 1.0;

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 2.0;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 2.0 * Math.cos(1.0);

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double wh = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = Math.cos(2.0);

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final double expected = Math.cos(2.0);

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double wh = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final double expected = Math.cos(dt);

                testCos(t0, fc, wh, t, expected);
            }

            private void testCos(@NonNull final Duration t0, final double fc, final double wh, final Duration t,
                    final double expected) {
                final double fs = 0.0;
                final double f0 = 0.0;
                final double f1 = 0.0;
                final double f2 = 0.0;
                final double f3 = 0.0;
                final double we = 0.0;

                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }

        }// class

        @Nested
        public class Cubic {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f3 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 1.0;

                testCubic(t0, f3, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final double f3 = 1.0;
                final double we = 1.0;
                final Duration t = t0;
                final double expected = 0.0;

                testCubic(t0, f3, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f3 = 2.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 2.0;

                testCubic(t0, f3, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f3 = 1.0;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 8.0;

                testCubic(t0, f3, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f3 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final double expected = 8.0;

                testCubic(t0, f3, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f3 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final double expected = dt * dt * dt;

                testCubic(t0, f3, we, t, expected);
            }

            private void testCubic(@NonNull final Duration t0, final double f3, final double we, final Duration t,
                    final double expected) {
                final double fc = 0.0;
                final double fs = 0.0;
                final double f0 = 0.0;
                final double f1 = 0.0;
                final double f2 = 0.0;
                final double wh = 0.0;

                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Exp {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = Math.exp(1.0);

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final double fc = 1.0;
                final double we = 1.0;
                final Duration t = t0;
                final double expected = 1.0;

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 2.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 2.0 * Math.exp(1.0);

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = Math.exp(2.0);

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final double expected = Math.exp(2.0);

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fc = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final double expected = Math.exp(dt);

                testExp(t0, fc, we, t, expected);
            }

            private void testExp(@NonNull final Duration t0, final double fc, final double we, final Duration t,
                    final double expected) {
                final double fs = 0.0;
                final double f0 = 0.0;
                final double f1 = 0.0;
                final double f2 = 0.0;
                final double f3 = 0.0;
                final double wh = 0.0;

                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Linear {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(0);
                final double f1 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(1);
                final double expected = 1.0;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(0);
                final double f1 = 2.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(1);
                final double expected = 2.0;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(0);
                final double f1 = 1.0;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(1);
                final double expected = 2.0;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(0);
                final double f1 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 2.0;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f1 = 1.0;
                final double we = 1.0;
                final Duration t = t0;
                final double expected = 0.0;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(0);
                final double f1 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(1000000001);
                final double expected = 1.000000001;

                testLinear(t0, f1, we, t, expected);
            }

            private void testLinear(@NonNull final Duration t0, final double f1, final double we, final Duration t,
                    final double expected) {
                final double fc = 0.0;
                final double fs = 0.0;
                final double f0 = 0.0;
                final double f2 = 0.0;
                final double f3 = 0.0;
                final double wh = 0.0;

                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Quadratic {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f2 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 1.0;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final double f2 = 1.0;
                final double we = 1.0;
                final Duration t = t0;
                final double expected = 0.0;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f2 = 2.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 2.0;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f2 = 1.0;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 4.0;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f2 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final double expected = 4.0;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final double f2 = 1.0;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final double expected = dt * dt;

                testQuadratic(t0, f2, we, t, expected);
            }

            private void testQuadratic(@NonNull final Duration t0, final double f2, final double we, final Duration t,
                    final double expected) {
                final double fc = 0.0;
                final double fs = 0.0;
                final double f0 = 0.0;
                final double f1 = 0.0;
                final double f3 = 0.0;
                final double wh = 0.0;

                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Sin {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fs = 1.0;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = Math.sin(1.0);

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final double fs = 1.0;
                final double wh = 1.0;
                final Duration t = t0;
                final double expected = 0.0;

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fs = 2.0;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = 2.0 * Math.sin(1.0);

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fs = 1.0;
                final double wh = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final double expected = Math.sin(2.0);

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fs = 1.0;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final double expected = Math.sin(2.0);

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final double fs = 1.0;
                final double wh = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final double expected = Math.sin(dt);

                testSin(t0, fs, wh, t, expected);
            }

            private void testSin(@NonNull final Duration t0, final double fs, final double wh, final Duration t,
                    final double expected) {
                final double f0 = 0.0;
                final double f1 = 0.0;
                final double f2 = 0.0;
                final double f3 = 0.0;
                final double fc = 0.0;
                final double we = 0.0;

                final double precision = Math.nextUp(expected) - expected;

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }

        }// class

        private void test(@NonNull final Duration t0, final double f0, final double f1, final double f2,
                final double f3, final double fc, final double fs, final double we, final double wh, final Duration t,
                final double expected, final double precision) {
            final JerkingHarmonicScalar s = new JerkingHarmonicScalar(t0, f0, f1, f2, f3, fc, fs, we, wh);

            final double value = at(s, t);

            assertEquals(expected, value, precision, "value");
        }
    }// class

    @Nested
    public class Constructor {

        @Test
        public void a() {
            test(T_1, 1, 2, 3, 4, 5, 6, 7, 8);
        }

        @Test
        public void t0_2() {
            test(T_2, 8, 7, 6, 5, 4, 3, 2, 1);
        }

        private JerkingHarmonicScalar test(@NonNull final Duration t0, final double f0, final double fc,
                final double fs, final double f1, final double f2, final double f3, final double we, final double wh) {
            final JerkingHarmonicScalar s = new JerkingHarmonicScalar(t0, f0, f1, f2, f3, fc, fs, we, wh);

            assertInvariants(s);
            assertEquals(t0, s.getT0(), "t0");
            assertEquals(f0, s.getF0(), "f0");
            assertEquals(f1, s.getF1(), "f1");
            assertEquals(f2, s.getF2(), "f2");
            assertEquals(f3, s.getF3(), "f3");
            assertEquals(fc, s.getFc(), "fc");
            assertEquals(fs, s.getFs(), "fs");
            assertEquals(we, s.getWe(), "we");
            assertEquals(wh, s.getWh(), "wh");

            return s;
        }

    }// class

    private static final Duration T_1 = TimeVaryingScalarTest.T_1;

    private static final Duration T_2 = TimeVaryingScalarTest.T_2;

    private static final Duration T_3 = TimeVaryingScalarTest.T_3;

    public static double applyAsDouble(final JerkingHarmonicScalar s, final Duration value) {
        final double result = AbstractTimeVaryingScalarTest.applyAsDouble(s, value);

        assertInvariants(s);

        return result;
    }

    public static void assertInvariants(final JerkingHarmonicScalar s) {
        AbstractTimeVaryingScalarTest.assertInvariants(s);// inherited

        assertNotNull(s.getT0(), "Not null, t0");
    }

    public static void assertInvariants(final JerkingHarmonicScalar s, final Duration t) {
        AbstractTimeVaryingScalarTest.assertInvariants(s, t);// inherited
    }

    public static void assertInvariants(final JerkingHarmonicScalar s1, final JerkingHarmonicScalar s2) {
        AbstractTimeVaryingScalarTest.assertInvariants(s1, s2);// inherited
    }

    public static double at(final JerkingHarmonicScalar s, final Duration t) {
        final double result = AbstractTimeVaryingScalarTest.at(s, t);// inherited

        assertInvariants(s);

        return result;
    }
}
