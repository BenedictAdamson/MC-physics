package uk.badamson.mc.physics.solver;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.umd.cs.findbugs.annotations.NonNull;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.HarmonicVector3Test;
import uk.badamson.mc.physics.solver.mapper.HarmonicVector3Mapper;

/**
 * <p>
 * Unit tests for the class {@link HarmonicVector3EnergyErrorFunctionTerms}
 * </p>
 */
public class HarmonicVector3EnergyErrorFunctionTermsTest {

    @Nested
    public class ValueTerm {

        @Nested
        public class Constant {

            @Test
            public void a() {
                test(1, T_1, F_1, T_2, F_2);
            }

            @Test
            public void b() {
                test(2, T_2, F_3, T_3, F_4);
            }

            @Test
            public void c() {
                test(1, T_2, F_1, T_1, F_2);
            }

            private void test(final double scale, @NonNull final Duration t, final ImmutableVector3 wanted,
                    @NonNull final Duration t0, final ImmutableVector3 f0) {
                final HarmonicVector3 actual = new HarmonicVector3(t0, f0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, 0, 0);
                final double wScale = Double.MIN_NORMAL;
                final double fScale = Math.max(Math.max(f0.magnitude(), wanted.magnitude()), Double.MIN_NORMAL);
                final double delta = Math.nextUp(1.0) - 1;
                final ImmutableVector3 fe = actual.apply(t).minus(wanted);
                final double eExpected = fe.magnitude2() * scale;
                final ImmutableVector3 dedf0Expected = fe.scale(2.0 * scale);
                final ImmutableVector3 dedfcExpected = fe.scale(2.0 * scale);
                final HarmonicVector3EnergyErrorValueAndGradients expectedErrorAndGradients = new HarmonicVector3EnergyErrorValueAndGradients(
                        eExpected, dedf0Expected, ImmutableVector3.ZERO, ImmutableVector3.ZERO, dedfcExpected,
                        ImmutableVector3.ZERO, 0, 0);

                ValueTerm.this.test(scale, t, wanted, actual, expectedErrorAndGradients, delta, wScale, fScale);
            }
        }// class

        @Nested
        public class HasWanted {

            @Nested
            public class AtOrigin {

                @Test
                public void constantA() {
                    test(1, T_1, F_1, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                            ImmutableVector3.ZERO, 0, 0);
                }

                @Test
                public void constantB() {
                    test(2, T_2, F_2, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                            ImmutableVector3.ZERO, 0, 0);
                }

                @Test
                public void cosine() {
                    test(1, T_1, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, F_1,
                            ImmutableVector3.ZERO, 0, 2);
                }

                @Test
                public void exponential() {
                    test(1, T_1, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, F_1,
                            ImmutableVector3.ZERO, 2, 0);
                }

                @Test
                public void linear() {
                    test(1, T_1, ImmutableVector3.ZERO, F_1, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                            ImmutableVector3.ZERO, 0, 0);
                }

                @Test
                public void quadratic() {
                    test(1, T_1, ImmutableVector3.ZERO, ImmutableVector3.ZERO, F_1, ImmutableVector3.ZERO,
                            ImmutableVector3.ZERO, 0, 0);
                }

                @Test
                public void sine() {
                    test(1, T_1, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                            ImmutableVector3.ZERO, F_1, 0, 2);
                }

                private void test(final double scale, @NonNull final Duration t, final ImmutableVector3 f0,
                        final ImmutableVector3 f1, final ImmutableVector3 f2, final ImmutableVector3 fc,
                        final ImmutableVector3 fs, final double we, final double wh) {
                    final Duration t0 = t;
                    final double wScale = Math.min(we, wh);
                    final double fScale = Math.max(Math.max(f0.magnitude(), fc.magnitude()), Double.MIN_NORMAL);
                    final HarmonicVector3 actual = new HarmonicVector3(t0, f0, f1, f2, fc, fs, we, wh);

                    HasWanted.this.test(scale, t, actual, wScale, fScale);
                }
            }// class

            private void test(final double scale, @NonNull final Duration t, final HarmonicVector3 actual,
                    final double wScale, final double fScale) {
                final ImmutableVector3 wanted = actual.apply(t);
                final var expectedErrorAndGradients = HarmonicVector3EnergyErrorValueAndGradients.ZERO;
                final double delta = Math.nextUp(1.0) - 1;

                ValueTerm.this.test(scale, t, wanted, actual, expectedErrorAndGradients, delta, wScale, fScale);
            }
        }// class

        @Nested
        @Disabled
        public class MinimiseEnergyError {

            @Test
            public void decay() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = Duration.ofSeconds(1);
                final ImmutableVector3 wanted1 = ImmutableVector3.I.scale(16);
                final Duration t2 = Duration.ofSeconds(2);
                final ImmutableVector3 wanted2 = ImmutableVector3.I.scale(8);
                final Duration t3 = Duration.ofSeconds(3);
                final ImmutableVector3 wanted3 = ImmutableVector3.I.scale(4);
                final Duration t4 = Duration.ofSeconds(4);
                final ImmutableVector3 wanted4 = ImmutableVector3.I.scale(2);
                final Duration t5 = Duration.ofSeconds(5);
                final ImmutableVector3 wanted5 = ImmutableVector3.I;
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }

            @Test
            public void exponentialGrowth() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = Duration.ofSeconds(1);
                final ImmutableVector3 wanted1 = ImmutableVector3.I;
                final Duration t2 = Duration.ofSeconds(2);
                final ImmutableVector3 wanted2 = ImmutableVector3.I.scale(2);
                final Duration t3 = Duration.ofSeconds(3);
                final ImmutableVector3 wanted3 = ImmutableVector3.I.scale(4);
                final Duration t4 = Duration.ofSeconds(4);
                final ImmutableVector3 wanted4 = ImmutableVector3.I.scale(8);
                final Duration t5 = Duration.ofSeconds(5);
                final ImmutableVector3 wanted5 = ImmutableVector3.I.scale(16);
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }

            @Test
            public void linear() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = Duration.ofSeconds(1);
                final ImmutableVector3 wanted1 = ImmutableVector3.I;
                final Duration t2 = Duration.ofSeconds(2);
                final ImmutableVector3 wanted2 = ImmutableVector3.I.scale(2);
                final Duration t3 = Duration.ofSeconds(3);
                final ImmutableVector3 wanted3 = ImmutableVector3.I.scale(3);
                final Duration t4 = Duration.ofSeconds(4);
                final ImmutableVector3 wanted4 = ImmutableVector3.I.scale(4);
                final Duration t5 = Duration.ofSeconds(5);
                final ImmutableVector3 wanted5 = ImmutableVector3.I.scale(5);
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }

            @Test
            public void oscillateA() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = Duration.ofSeconds(1);
                final ImmutableVector3 wanted1 = ImmutableVector3.I;
                final Duration t2 = Duration.ofSeconds(2);
                final ImmutableVector3 wanted2 = ImmutableVector3.ZERO;
                final Duration t3 = Duration.ofSeconds(3);
                final ImmutableVector3 wanted3 = ImmutableVector3.I.scale(-1);
                final Duration t4 = Duration.ofSeconds(4);
                final ImmutableVector3 wanted4 = ImmutableVector3.ZERO;
                final Duration t5 = Duration.ofSeconds(5);
                final ImmutableVector3 wanted5 = ImmutableVector3.I;
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }

            @Test
            public void oscillateB() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = Duration.ofSeconds(1);
                final ImmutableVector3 wanted1 = ImmutableVector3.ZERO;
                final Duration t2 = Duration.ofSeconds(2);
                final ImmutableVector3 wanted2 = ImmutableVector3.I;
                final Duration t3 = Duration.ofSeconds(3);
                final ImmutableVector3 wanted3 = ImmutableVector3.ZERO;
                final Duration t4 = Duration.ofSeconds(4);
                final ImmutableVector3 wanted4 = ImmutableVector3.I.scale(-1);
                final Duration t5 = Duration.ofSeconds(5);
                final ImmutableVector3 wanted5 = ImmutableVector3.ZERO;
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }

            @Test
            public void quadratic() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = Duration.ofSeconds(1);
                final ImmutableVector3 wanted1 = ImmutableVector3.I;
                final Duration t2 = Duration.ofSeconds(2);
                final ImmutableVector3 wanted2 = ImmutableVector3.I.scale(4);
                final Duration t3 = Duration.ofSeconds(3);
                final ImmutableVector3 wanted3 = ImmutableVector3.I.scale(9);
                final Duration t4 = Duration.ofSeconds(4);
                final ImmutableVector3 wanted4 = ImmutableVector3.I.scale(16);
                final Duration t5 = Duration.ofSeconds(5);
                final ImmutableVector3 wanted5 = ImmutableVector3.I.scale(25);
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }

            private void test(final Duration timeScale, final double energyScale, final Duration t1,
                    final ImmutableVector3 wanted1, final Duration t2, final ImmutableVector3 wanted2,
                    final Duration t3, final ImmutableVector3 wanted3, final Duration t4,
                    final ImmutableVector3 wanted4, final Duration t5, final ImmutableVector3 wanted5,
                    final HarmonicVector3 f0, final double delta) {
                final Double deltaDouble = Double.valueOf(delta);
                final HarmonicVector3Mapper mapper = new HarmonicVector3Mapper(0, timeScale);
                final var term1 = HarmonicVector3EnergyErrorFunctionTerms.createValueTerm(energyScale, t1, wanted1);
                final var term2 = HarmonicVector3EnergyErrorFunctionTerms.createValueTerm(energyScale, t2, wanted2);
                final var term3 = HarmonicVector3EnergyErrorFunctionTerms.createValueTerm(energyScale, t3, wanted3);
                final var term4 = HarmonicVector3EnergyErrorFunctionTerms.createValueTerm(energyScale, t4, wanted4);
                final var term5 = HarmonicVector3EnergyErrorFunctionTerms.createValueTerm(energyScale, t5, wanted5);
                final var errorFunction = new HarmonicVector3EnergyErrorFunction(mapper,
                        Arrays.asList(term1, term2, term3, term4, term5));
                final double tolerance = 1E-5;

                final HarmonicVector3 solution = HarmonicVector3EnergyErrorFunctionTest
                        .minimiseEnergyError(errorFunction, f0, tolerance);

                assertThat("Passes through point 1", Double.valueOf(wanted1.minus(solution.at(t1)).magnitude2()),
                        lessThanOrEqualTo(deltaDouble));
                assertThat("Passes through point 2", Double.valueOf(wanted2.minus(solution.at(t2)).magnitude2()),
                        lessThanOrEqualTo(deltaDouble));
                assertThat("Passes through point 3", Double.valueOf(wanted3.minus(solution.at(t3)).magnitude2()),
                        lessThanOrEqualTo(deltaDouble));
                assertThat("Passes through point 4", Double.valueOf(wanted4.minus(solution.at(t4)).magnitude2()),
                        lessThanOrEqualTo(deltaDouble));
                assertThat("Passes through point 5", Double.valueOf(wanted5.minus(solution.at(t5)).magnitude2()),
                        lessThanOrEqualTo(deltaDouble));
            }

            @Test
            public void zero() {
                final Duration timeScale = T_1;
                final double energyScale = 2;
                final Duration t1 = T_2;
                final ImmutableVector3 wanted1 = ImmutableVector3.ZERO;
                final Duration t2 = T_3;
                final ImmutableVector3 wanted2 = ImmutableVector3.ZERO;
                final Duration t3 = T_4;
                final ImmutableVector3 wanted3 = ImmutableVector3.ZERO;
                final Duration t4 = T_5;
                final ImmutableVector3 wanted4 = ImmutableVector3.ZERO;
                final Duration t5 = T_6;
                final ImmutableVector3 wanted5 = ImmutableVector3.ZERO;
                final HarmonicVector3 f0 = v1;
                final double delta = 1E-3;

                test(timeScale, energyScale, t1, wanted1, t2, wanted2, t3, wanted3, t4, wanted4, t5, wanted5, f0,
                        delta);
            }
        }// class

        private void test(final double scale, @NonNull final Duration t, @NonNull final ImmutableVector3 wanted,
                final HarmonicVector3 actual,
                final HarmonicVector3EnergyErrorValueAndGradients expectedErrorAndGradients, final double delta,
                final double wScale, final double fScale) {
            final var term = HarmonicVector3EnergyErrorFunctionTerms.createValueTerm(scale, t, wanted);
            assertNotNull(term, "Always returns a term.");// guard
            final var errorAndGradients = HarmonicVector3EnergyErrorFunctionTermsTest.apply(term, actual);
            HarmonicVector3EnergyErrorValueAndGradientsTest.assertEquals(expectedErrorAndGradients, errorAndGradients,
                    delta, wScale, fScale, "error");
        }

    }// class

    @Nested
    public class Zero {

        @Test
        public void a() {
            apply(v1);
        }

        private void apply(final HarmonicVector3 v) {
            final var result = HarmonicVector3EnergyErrorFunctionTermsTest
                    .apply(HarmonicVector3EnergyErrorFunctionTerms.ZERO, v);

            assertSame(HarmonicVector3EnergyErrorValueAndGradients.ZERO, result, "always zero.");
        }

        @Test
        public void b() {
            apply(v2);
        }
    }// class

    private static final ImmutableVector3 F_1 = ImmutableVector3.I;

    private static final ImmutableVector3 F_2 = ImmutableVector3.J;

    private static final ImmutableVector3 F_3 = ImmutableVector3.K;

    private static final ImmutableVector3 F_4 = ImmutableVector3.create(1, 2, 3);

    private static final ImmutableVector3 F_5 = ImmutableVector3.create(5, 7, 11);

    private static final Duration T_1 = Duration.ofSeconds(2);

    private static final Duration T_2 = Duration.ofSeconds(3);

    private static final Duration T_3 = Duration.ofSeconds(5);

    private static final Duration T_4 = Duration.ofSeconds(7);

    private static final Duration T_5 = Duration.ofSeconds(11);

    private static final Duration T_6 = Duration.ofSeconds(13);

    private static HarmonicVector3 v1;

    private static HarmonicVector3 v2;

    private static HarmonicVector3EnergyErrorValueAndGradients apply(
            final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> f, final HarmonicVector3 v) {
        final var result = f.apply(v);

        assertNotNull(result, "Not null, result");
        HarmonicVector3Test.assertInvariants(v);// check for side effects
        HarmonicVector3EnergyErrorValueAndGradientsTest.assertInvariants(result);

        return result;
    }

    @BeforeAll
    public static void setUp() {
        v1 = new HarmonicVector3(T_1, F_1, F_2, F_3, F_1, F_2, 3, 4);
        v2 = new HarmonicVector3(T_2, F_2, F_3, F_1, F_2, F_3, 5, 7);
    }
}
