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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * Units tests and auxiliary test code for the class
 * {@link HarmonicVector3EnergyErrorFunction}
 * </p>
 */
public class HarmonicVector3EnergyErrorFunctionTest {

    public static class ErrorValueAndGradientsTest {

        @Nested
        public class Constructor {

            @Nested
            public class Equivalent {

                @Test
                public void a() {
                    test(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                }

                @Test
                public void b() {
                    test(2, V_2, V_3, V_4, V_5, V_6, 17, 19);
                }

                private void test(final double e, final ImmutableVector3 dedf0, final ImmutableVector3 dedf1,
                        final ImmutableVector3 dedf2, final ImmutableVector3 dedfc, final ImmutableVector3 dedfs,
                        final double dedwe, final double dedwh) {
                    final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                            e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);
                    final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                            e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);

                    assertInvariants(value1, value2);
                    assertEquals(value1, value2);
                }
            }// class

            @Test
            public void a() {
                test(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            }

            @Test
            public void b() {
                test(2, V_2, V_3, V_4, V_5, V_6, 17, 19);
            }

            @Test
            public void different_dedf0() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_6, V_2, V_3, V_4, V_5, 13, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_dedf1() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_6, V_3, V_4, V_5, 13, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_dedf2() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_6, V_4, V_5, 13, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_dedfc() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_6, V_5, 13, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_dedfs() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_6, 13, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_dedwe() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 19, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_dedwh() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 19);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            @Test
            public void different_e() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value1 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        0, V_1, V_2, V_3, V_4, V_5, 13, 17);
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value2 = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        1, V_1, V_2, V_3, V_4, V_5, 13, 17);

                assertInvariants(value1, value2);
                assertNotEquals(value1, value2);
            }

            private HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients test(final double e,
                    final ImmutableVector3 dedf0, final ImmutableVector3 dedf1, final ImmutableVector3 dedf2,
                    final ImmutableVector3 dedfc, final ImmutableVector3 dedfs, final double dedwe,
                    final double dedwh) {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value = new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(
                        e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);

                assertInvariants(value);
                assertEquals(e, value.getE(), "e");
                assertSame(dedf0, value.getDedf0(), "dedf0");
                assertSame(dedf1, value.getDedf1(), "dedf1");
                assertSame(dedf2, value.getDedf2(), "dedf2");
                assertSame(dedfc, value.getDedfc(), "dedfc");
                assertSame(dedfs, value.getDedfs(), "dedfs");
                assertEquals(dedwe, value.getDedwe(), "dedwe");
                assertEquals(dedwh, value.getDedwh(), "dedwh");

                return value;
            }
        }// class

        @Nested
        public class Sum {

            @Nested
            public class One {
                @Test
                public void a() {
                    test(new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13,
                            17));
                }

                @Test
                public void b() {
                    test(new HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients(2, V_2, V_3, V_4, V_5, V_6, 17,
                            19));
                }

                private void test(final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients value) {
                    final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients total = Sum.this.test(value);
                    assertEquals(value, total);
                }
            }// class

            private HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients test(
                    final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients... values) {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients total = HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients
                        .sum(values);
                assertNotNull(total, "Always returns a sum");// guard
                assertInvariants(total);
                for (final var value : values) {
                    assertInvariants(total, value);
                }
                return total;
            }

            @Test
            public void zero() {
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients total = test();
                assertEquals(HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients.ZERO, total, "zero");
            }
        }// class

        public static void assertInvariants(final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients e) {
            ObjectTest.assertInvariants(e);// inherited

            assertNotNull(e.getDedf0(), "Not null, dedf0");
            assertNotNull(e.getDedf1(), "Not null, dedf1");
            assertNotNull(e.getDedf2(), "Not null, dedf2");
            assertNotNull(e.getDedfc(), "Not null, dedfc");
            assertNotNull(e.getDedfs(), "Not null, dedfs");
        }

        public static void assertInvariants(final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients e1,
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients e2) {
            ObjectTest.assertInvariants(e1, e2);// inherited

            final boolean equals = e1.equals(e2);
            assertFalse(equals && !(e1.getE() == e2.getE()), "Value semantics, e");
            assertFalse(equals && !(e1.getDedwe() == e2.getDedwe()), "Value semantics, dedwe");
            assertFalse(equals && !(e1.getDedwh() == e2.getDedwh()), "Value semantics, dedwh");
            assertFalse(equals && !e1.getDedf0().equals(e2.getDedf0()), "Value semantics, dedf0");
            assertFalse(equals && !e1.getDedf1().equals(e2.getDedf1()), "Value semantics, dedf1");
            assertFalse(equals && !e1.getDedf2().equals(e2.getDedf2()), "Value semantics, dedf2");
            assertFalse(equals && !e1.getDedfc().equals(e2.getDedfc()), "Value semantics, dedfc");
            assertFalse(equals && !e1.getDedfs().equals(e2.getDedfs()), "Value semantics, dedfs");
        }
    }// class

    private static ImmutableVector3 V_1 = ImmutableVector3.I;
    private static ImmutableVector3 V_2 = ImmutableVector3.J;
    private static ImmutableVector3 V_3 = ImmutableVector3.K;
    private static ImmutableVector3 V_4 = ImmutableVector3.create(1, 2, 3);
    private static ImmutableVector3 V_5 = ImmutableVector3.create(5, 7, 11);

    private static ImmutableVector3 V_6 = ImmutableVector3.create(13, 17, 19);

    public static void assertInvariants(final HarmonicVector3EnergyErrorFunction f) {
        ObjectTest.assertInvariants(f);// inherited
        EnergyErrorFunctionTest.assertInvariants(f);// inherited
    }

    public static void assertInvariants(final HarmonicVector3EnergyErrorFunction f1,
            final HarmonicVector3EnergyErrorFunction f2) {
        ObjectTest.assertInvariants(f1, f2);// inherited
        EnergyErrorFunctionTest.assertInvariants(f1, f2);// inherited
    }
}
