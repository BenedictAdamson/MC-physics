package uk.badamson.mc.physics;
/*
 * © Copyright Benedict Adamson 2018-19.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.umd.cs.findbugs.annotations.NonNull;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * Unit tests for the {@link HarmonicVector3} class.
 * </p>
 */
public class HarmonicVector3Test {

    @Nested
    public class At {

        @Nested
        public class Constant {

            @Test
            public void a() {
                final Duration t0 = T_1;
                final ImmutableVector3 f0 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = T_2;
                testConstant(t0, f0, we, t);
            }

            @Test
            public void b() {
                final Duration t0 = T_2;
                final ImmutableVector3 f0 = ImmutableVector3.J;
                final double we = 2.0;
                final Duration t = T_3;
                testConstant(t0, f0, we, t);
            }

            private void testConstant(@NonNull final Duration t0, final ImmutableVector3 f0, final double we,
                    final Duration t) {
                final ImmutableVector3 fc = ImmutableVector3.ZERO;
                final ImmutableVector3 fs = ImmutableVector3.ZERO;
                final ImmutableVector3 f1 = ImmutableVector3.ZERO;
                final ImmutableVector3 f2 = ImmutableVector3.ZERO;
                final ImmutableVector3 f3 = ImmutableVector3.ZERO;
                final double wh = 0.0;
                final ImmutableVector3 expected = f0;
                final double precision = precisionFor(expected);

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Cos {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fc.scale(Math.cos(1.0));

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = t0;
                final ImmutableVector3 expected = fc;

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I.scale(2.0);
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fc.scale(Math.cos(1.0));

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double wh = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fc.scale(Math.cos(2.0));

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final ImmutableVector3 expected = fc.scale(Math.cos(2.0));

                testCos(t0, fc, wh, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final ImmutableVector3 expected = fc.scale(Math.cos(dt));

                testCos(t0, fc, wh, t, expected);
            }

            private void testCos(@NonNull final Duration t0, final ImmutableVector3 fc, final double wh,
                    final Duration t, final ImmutableVector3 expected) {
                final ImmutableVector3 fs = ImmutableVector3.ZERO;
                final ImmutableVector3 f0 = ImmutableVector3.ZERO;
                final ImmutableVector3 f1 = ImmutableVector3.ZERO;
                final ImmutableVector3 f2 = ImmutableVector3.ZERO;
                final ImmutableVector3 f3 = ImmutableVector3.ZERO;
                final double we = 0.0;

                final double precision = precisionFor(expected);

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }

        }// class

        @Nested
        public class Exp {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fc.scale(Math.exp(1.0));

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = t0;
                final ImmutableVector3 expected = fc;

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I.scale(2.0);
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fc.scale(Math.exp(1.0));

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fc.scale(Math.exp(2.0));

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final ImmutableVector3 expected = fc.scale(Math.exp(2.0));

                testExp(t0, fc, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fc = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final ImmutableVector3 expected = fc.scale(Math.exp(dt));

                testExp(t0, fc, we, t, expected);
            }

            private void testExp(@NonNull final Duration t0, final ImmutableVector3 fc, final double we,
                    final Duration t, final ImmutableVector3 expected) {
                final ImmutableVector3 fs = ImmutableVector3.ZERO;
                final ImmutableVector3 f0 = ImmutableVector3.ZERO;
                final ImmutableVector3 f1 = ImmutableVector3.ZERO;
                final ImmutableVector3 f2 = ImmutableVector3.ZERO;
                final ImmutableVector3 f3 = ImmutableVector3.ZERO;
                final double wh = 0.0;

                final double precision = precisionFor(expected);

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Linear {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(0);
                final ImmutableVector3 f1 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(1);
                final ImmutableVector3 expected = f1;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(0);
                final ImmutableVector3 f1 = ImmutableVector3.I.scale(2.0);
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(1);
                final ImmutableVector3 expected = f1;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(0);
                final ImmutableVector3 f1 = ImmutableVector3.I;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(1);
                final ImmutableVector3 expected = f1.scale(2.0);

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(0);
                final ImmutableVector3 f1 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = f1.scale(2.0);

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 f1 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = t0;
                final ImmutableVector3 expected = ImmutableVector3.ZERO;

                testLinear(t0, f1, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(0);
                final ImmutableVector3 f1 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(1000000001);
                final ImmutableVector3 expected = f1.scale(1.000000001);

                testLinear(t0, f1, we, t, expected);
            }

            private void testLinear(@NonNull final Duration t0, final ImmutableVector3 f1, final double we,
                    final Duration t, final ImmutableVector3 expected) {
                final ImmutableVector3 fc = ImmutableVector3.ZERO;
                final ImmutableVector3 fs = ImmutableVector3.ZERO;
                final ImmutableVector3 f0 = ImmutableVector3.ZERO;
                final ImmutableVector3 f2 = ImmutableVector3.ZERO;
                final ImmutableVector3 f3 = ImmutableVector3.ZERO;
                final double wh = 0.0;

                final double precision = precisionFor(expected);

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Quadratic {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 f2 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = f2;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final ImmutableVector3 f2 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = t0;
                final ImmutableVector3 expected = ImmutableVector3.ZERO;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 f2 = ImmutableVector3.I.scale(2.0);
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = f2;

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 f2 = ImmutableVector3.I;
                final double we = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = f2.scale(4.0);

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 f2 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final ImmutableVector3 expected = f2.scale(4.0);

                testQuadratic(t0, f2, we, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 f2 = ImmutableVector3.I;
                final double we = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final ImmutableVector3 expected = f2.scale(dt * dt);

                testQuadratic(t0, f2, we, t, expected);
            }

            private void testQuadratic(@NonNull final Duration t0, final ImmutableVector3 f2, final double we,
                    final Duration t, final ImmutableVector3 expected) {
                final ImmutableVector3 fc = ImmutableVector3.ZERO;
                final ImmutableVector3 fs = ImmutableVector3.ZERO;
                final ImmutableVector3 f0 = ImmutableVector3.ZERO;
                final ImmutableVector3 f1 = ImmutableVector3.ZERO;
                final ImmutableVector3 f3 = ImmutableVector3.ZERO;
                final double wh = 0.0;

                final double precision = precisionFor(expected);

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }
        }// class

        @Nested
        public class Sin {

            @Test
            public void a() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fs = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fs.scale(Math.sin(1.0));

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void b() {
                final Duration t0 = Duration.ofSeconds(2);
                final ImmutableVector3 fs = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = t0;
                final ImmutableVector3 expected = ImmutableVector3.ZERO;

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void c() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fs = ImmutableVector3.I.scale(2.0);
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fs.scale(Math.sin(1.0));

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void d() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fs = ImmutableVector3.I;
                final double wh = 2.0;
                final Duration t = Duration.ofSeconds(2);
                final ImmutableVector3 expected = fs.scale(Math.sin(2.0));

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void e() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fs = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = Duration.ofSeconds(3);
                final ImmutableVector3 expected = fs.scale(Math.sin(2.0));

                testSin(t0, fs, wh, t, expected);
            }

            @Test
            public void f() {
                final Duration t0 = Duration.ofSeconds(1);
                final ImmutableVector3 fs = ImmutableVector3.I;
                final double wh = 1.0;
                final Duration t = Duration.ofNanos(2000000001);
                final double dt = 1.000000001;
                final ImmutableVector3 expected = fs.scale(Math.sin(dt));

                testSin(t0, fs, wh, t, expected);
            }

            private void testSin(@NonNull final Duration t0, final ImmutableVector3 fs, final double wh,
                    final Duration t, final ImmutableVector3 expected) {
                final ImmutableVector3 f0 = ImmutableVector3.ZERO;
                final ImmutableVector3 f1 = ImmutableVector3.ZERO;
                final ImmutableVector3 f2 = ImmutableVector3.ZERO;
                final ImmutableVector3 f3 = ImmutableVector3.ZERO;
                final ImmutableVector3 fc = ImmutableVector3.ZERO;
                final double we = 0.0;

                final double precision = precisionFor(expected);

                test(t0, f0, f1, f2, f3, fc, fs, we, wh, t, expected, precision);
            }

        }// class

        private void test(@NonNull final Duration t0, final ImmutableVector3 f0, final ImmutableVector3 f1,
                final ImmutableVector3 f2, final ImmutableVector3 f3, final ImmutableVector3 fc,
                final ImmutableVector3 fs, final double we, final double wh, final Duration t,
                final ImmutableVector3 expected, final double precision) {
            final HarmonicVector3 v = new HarmonicVector3(t0, f0, f1, f2, fc, fs, we, wh);

            final ImmutableVector3 value = at(v, t);

            assertTrue(value.minus(expected).magnitude() <= precision, "Close to the expected value");
        }
    }// class

    @Nested
    public class Constructor {

        @Nested
        public class ConstantAcceleration {

            @Test
            public void a() {
                test(T_1, V_1, V_2, V_3, 13, 17);
            }

            @Test
            public void b() {
                test(T_2, V_2, V_3, V_4, 17, 19);
            }

            private HarmonicVector3 test(final Duration t0, final ImmutableVector3 f0, final ImmutableVector3 f1,
                    final ImmutableVector3 f2, final double we, final double wh) {
                final HarmonicVector3 expectedDxdt = new HarmonicVector3(t0, f1, f2.scale(2), ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);
                final HarmonicVector3 expectedD2dt2 = new HarmonicVector3(t0, f2.scale(2), ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                final HarmonicVector3 x = Constructor.this.test(t0, f0, f1, f2, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, we, wh);

                final HarmonicVector3 dxdt = x.getTimeDerivative();
                assertEquals(expectedDxdt, dxdt, "velocity");
                assertEquals(expectedD2dt2, dxdt.getTimeDerivative(), "acceleration");

                return x;
            }
        }// class

        @Nested
        public class ConstantVelocity {

            @Test
            public void a() {
                test(T_1, V_1, V_2, 13, 17);
            }

            @Test
            public void b() {
                test(T_2, V_2, V_3, 17, 19);
            }

            private HarmonicVector3 test(final Duration t0, final ImmutableVector3 f0, final ImmutableVector3 f1,
                    final double we, final double wh) {
                final HarmonicVector3 expectedDxdt = new HarmonicVector3(t0, f1, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);
                final HarmonicVector3 zero = new HarmonicVector3(t0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                final HarmonicVector3 x = Constructor.this.test(t0, f0, f1, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                final HarmonicVector3 dxdt = x.getTimeDerivative();
                assertEquals(expectedDxdt, dxdt, "time derivative");
                assertEquals(zero, dxdt.getTimeDerivative(), "zero second derivative");

                return x;
            }
        }// class

        @Nested
        public class Cosine {

            @Test
            public void a() {
                test(T_1, V_1, 13);
            }

            @Test
            public void b() {
                test(T_2, V_2, 17);
            }

            private HarmonicVector3 test(final Duration t0, final ImmutableVector3 x0, final double w) {
                final HarmonicVector3 expectedDxdt = new HarmonicVector3(t0, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        x0.scale(-w).plus(ImmutableVector3.ZERO), 0, w);

                final HarmonicVector3 x = Constructor.this.test(t0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, x0, ImmutableVector3.ZERO, 0, w);

                assertEquals(expectedDxdt, x.getTimeDerivative(), "time derivative");
                return x;
            }
        }// class

        @Nested
        public class Different {

            @Test
            public void f0() {
                final double we = 13;
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_6, V_2, V_3, V_4, V_5, we, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void f1() {
                final double we = 13;
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_1, V_6, V_3, V_4, V_5, we, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void f2() {
                final double we = 13;
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_1, V_3, V_6, V_4, V_5, we, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void fc() {
                final double we = 13;
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_1, V_3, V_3, V_6, V_5, we, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void fs() {
                final double we = 13;
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_1, V_3, V_3, V_4, V_6, we, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void t0() {
                final double we = 13;
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_2, V_1, V_2, V_3, V_4, V_5, we, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void we() {
                final double wh = 17;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, 13, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_1, V_3, V_3, V_4, V_5, 15, wh);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void wh() {
                final double we = 13;
                final HarmonicVector3 v1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, we, 17);
                final HarmonicVector3 v2 = new HarmonicVector3(T_1, V_1, V_3, V_3, V_4, V_5, we, 19);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }
        }// class

        @Nested
        public class Equivalent {

            @Test
            public void a() {
                test(T_1, V_1, V_2, V_3, V_4, V_5, V_6, 7, 8);
            }

            @Test
            public void t0_2() {
                test(T_2, V_6, V_5, V_4, V_3, V_2, V_1, 2, 1);
            }

            private void test(@NonNull final Duration t0, final ImmutableVector3 f0, final ImmutableVector3 fc,
                    final ImmutableVector3 fs, final ImmutableVector3 f1, final ImmutableVector3 f2,
                    final ImmutableVector3 f3, final double we, final double wh) {
                final HarmonicVector3 v1 = new HarmonicVector3(t0, f0, f1, f2, fc, fs, we, wh);
                final HarmonicVector3 v2 = new HarmonicVector3(t0, f0, f1, f2, fc, fs, we, wh);

                assertInvariants(v1, v2);
                assertEquals(v1, v2, "equivalent");
            }

        }// class

        @Nested
        public class Exponential {

            @Test
            public void a() {
                test(T_1, V_1, 13);
            }

            @Test
            public void b() {
                test(T_2, V_2, 17);
            }

            private HarmonicVector3 test(final Duration t0, final ImmutableVector3 x0, final double w) {
                final HarmonicVector3 expectedDxdt = new HarmonicVector3(t0, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, x0.scale(w), ImmutableVector3.ZERO, w, 0);

                final HarmonicVector3 x = Constructor.this.test(t0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, x0, ImmutableVector3.ZERO, w, 0);

                assertEquals(expectedDxdt, x.getTimeDerivative(), "time derivative");
                return x;
            }
        }// class

        @Nested
        public class Sine {

            @Test
            public void a() {
                test(T_1, V_1, 13);
            }

            @Test
            public void b() {
                test(T_2, V_2, 17);
            }

            private HarmonicVector3 test(final Duration t0, final ImmutableVector3 x0, final double w) {
                final HarmonicVector3 expectedDxdt = new HarmonicVector3(t0, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, x0.scale(w), ImmutableVector3.ZERO, 0, w);

                final HarmonicVector3 x = Constructor.this.test(t0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, x0, 0, w);

                assertEquals(expectedDxdt, x.getTimeDerivative(), "time derivative");
                return x;
            }
        }// class

        @Nested
        public class Stationary {

            @Test
            public void a() {
                final double we = 0.0;
                final double wh = 0.0;
                test(T_1, V_1, we, wh);
            }

            @Test
            public void b() {
                final double we = 5.0;
                final double wh = 7.0;
                test(T_2, V_2, we, wh);
            }

            private HarmonicVector3 test(final Duration t0, final ImmutableVector3 x, final double we,
                    final double wh) {
                final HarmonicVector3 zero = new HarmonicVector3(t0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                final HarmonicVector3 vector = Constructor.this.test(t0, x, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                assertEquals(zero, vector.getTimeDerivative(), "zero time derivative");
                assertEquals(zero, vector.getTimeDerivative().getTimeDerivative(), "zero second derivative");
                return vector;
            }
        }// class

        @Test
        public void a() {
            test(T_1, V_1, V_2, V_3, V_4, V_5, 7, 8);
        }

        @Test
        public void t0_2() {
            test(T_2, V_6, V_5, V_4, V_3, V_2, 2, 1);
        }

        private HarmonicVector3 test(@NonNull final Duration t0, final ImmutableVector3 f0, final ImmutableVector3 f1,
                final ImmutableVector3 f2, final ImmutableVector3 fc, final ImmutableVector3 fs, final double we,
                final double wh) {
            final HarmonicVector3 v = new HarmonicVector3(t0, f0, f1, f2, fc, fs, we, wh);

            assertInvariants(v);
            assertSame(t0, v.getT0(), "t0");
            assertSame(f0, v.getF0(), "f0");
            assertSame(f1, v.getF1(), "f1");
            assertSame(f2, v.getF2(), "f2");
            assertSame(fc, v.getFc(), "fc");
            assertSame(fs, v.getFs(), "fs");
            Assertions.assertEquals(we, v.getWe(), "we");
            Assertions.assertEquals(wh, v.getWh(), "wh");

            return v;
        }
    }// class

    private static final Duration T_1 = TimeVaryingScalarTest.T_1;

    private static final Duration T_2 = TimeVaryingScalarTest.T_2;

    private static final Duration T_3 = TimeVaryingScalarTest.T_3;

    private static final ImmutableVector3 V_1 = ImmutableVector3.I;

    private static final ImmutableVector3 V_2 = ImmutableVector3.J;

    private static final ImmutableVector3 V_3 = ImmutableVector3.K;

    private static final ImmutableVector3 V_4 = ImmutableVector3.create(1, 2, 3);

    private static final ImmutableVector3 V_5 = ImmutableVector3.create(4, 3, 2);

    private static final ImmutableVector3 V_6 = ImmutableVector3.create(3, 4, 5);

    public static ImmutableVector3 apply(final HarmonicVector3 s, final Duration value) {
        final ImmutableVector3 result = AbstractTimeVaryingVector3Test.apply(s, value);

        assertInvariants(s);

        return result;
    }

    public static void assertEquals(final HarmonicVector3 expected, final HarmonicVector3 actual,
            final String message) {
        Assertions.assertEquals(expected.getF0(), actual.getF0(), message + ",f0");
        Assertions.assertEquals(expected.getF1(), actual.getF1(), message + ",f1");
        Assertions.assertEquals(expected.getF2(), actual.getF2(), message + ",f2");
        Assertions.assertEquals(expected.getFc(), actual.getFc(), message + ",fc");
        Assertions.assertEquals(expected.getFs(), actual.getFs(), message + ",fs");
        Assertions.assertEquals(expected.getT0(), actual.getT0(), message + ",t0");
        Assertions.assertEquals(expected.getWe(), actual.getWe(), message + ",we");
        Assertions.assertEquals(expected.getWh(), actual.getWh(), message + ",wh");
        Assertions.assertEquals(expected, actual, "equals");
    }

    public static void assertInvariants(final HarmonicVector3 v) {
        AbstractTimeVaryingVector3Test.assertInvariants(v);// inherited

        assertNotNull(v.getT0(), "Not null, t0");
        assertTimeDerivativeInvariants(v);
    }

    public static void assertInvariants(final HarmonicVector3 s, final Duration t) {
        AbstractTimeVaryingVector3Test.assertInvariants(s, t);// inherited
    }

    public static void assertInvariants(final HarmonicVector3 v1, final HarmonicVector3 v2) {
        AbstractTimeVaryingVector3Test.assertInvariants(v1, v2);// inherited

        final boolean equal = v1.equals(v2);
        assertFalse(equal && !v1.getF0().equals(v2.getF0()), "Equality requires equivalent attributes (f0)");
        assertFalse(equal && !v1.getF1().equals(v2.getF1()), "Equality requires equivalent attributes (f1)");
        assertFalse(equal && !v1.getF2().equals(v2.getF2()), "Equality requires equivalent attributes (f2)");
        assertFalse(equal && !v1.getFc().equals(v2.getFc()), "Equality requires equivalent attributes (fc)");
        assertFalse(equal && !v1.getFs().equals(v2.getFs()), "Equality requires equivalent attributes (fs)");

        assertFalse(equal && !v1.getT0().equals(v2.getT0()), "Equality requires equivalent attributes (t0)");

        assertFalse(equal && v1.getWe() != v2.getWe(), "Equality requires equivalent attributes (we)");
        assertFalse(equal && v1.getWh() != v2.getWh(), "Equality requires equivalent attributes (wh)");
    }

    private static HarmonicVector3 assertTimeDerivativeInvariants(final HarmonicVector3 v) {
        final HarmonicVector3 derivative = v.getTimeDerivative();

        assertNotNull(derivative, "Not null, timeDerivative");// guard
        assertSame(v.getT0(), derivative.getT0(),
                "The time derivative of this time varying vector has the same time origin as this vector.");
        Assertions.assertEquals(v.getWe(), derivative.getWe(),
                "The time derivative of this time varying vector has an exponential frequency term equal to the value of this vector.");
        Assertions.assertEquals(v.getWh(), derivative.getWh(),
                "The time derivative of this time varying vector has a harmonic frequency term equal to the value of this vector.");

        return derivative;
    }

    public static ImmutableVector3 at(final HarmonicVector3 s, final Duration t) {
        final ImmutableVector3 result = AbstractTimeVaryingVector3Test.at(s, t);// inherited

        assertInvariants(s);

        return result;
    }

    private static double precisionFor(final ImmutableVector3 expected) {
        final double m = expected.magnitude();
        return Math.nextUp(m) - m;
    }
}
