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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector3;

/**
 * <p>
 * Units tests and auxiliary test code for the class
 * {@link HarmonicVector3EnergyErrorValueAndGradients}
 * </p>
 */
public class HarmonicVector3EnergyErrorValueAndGradientsTest {

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
                final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(e, dedf0, dedf1, dedf2, dedfc, dedfs,
                        dedwe, dedwh);
                final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(e, dedf0, dedf1, dedf2, dedfc, dedfs,
                        dedwe, dedwh);

                assertInvariants(value1, value2);
                Assertions.assertEquals(value1, value2);
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
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_6, V_2, V_3, V_4, V_5, 13, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_dedf1() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_6, V_3, V_4, V_5, 13, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_dedf2() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_6, V_4, V_5, 13, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_dedfc() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_6, V_5, 13, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_dedfs() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_6, 13, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_dedwe() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 19, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_dedwh() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 19);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        @Test
        public void different_e() {
            final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(1, V_1, V_2, V_3, V_4, V_5, 13, 17);

            assertInvariants(value1, value2);
            assertNotEquals(value1, value2);
        }

        private HarmonicVector3EnergyErrorValueAndGradients test(final double e, final ImmutableVector3 dedf0,
                final ImmutableVector3 dedf1, final ImmutableVector3 dedf2, final ImmutableVector3 dedfc,
                final ImmutableVector3 dedfs, final double dedwe, final double dedwh) {
            final var value = new HarmonicVector3EnergyErrorValueAndGradients(e, dedf0, dedf1, dedf2, dedfc, dedfs,
                    dedwe, dedwh);

            assertInvariants(value);
            Assertions.assertEquals(e, value.getE(), "e");
            assertSame(dedf0, value.getDedf0(), "dedf0");
            assertSame(dedf1, value.getDedf1(), "dedf1");
            assertSame(dedf2, value.getDedf2(), "dedf2");
            assertSame(dedfc, value.getDedfc(), "dedfc");
            assertSame(dedfs, value.getDedfs(), "dedfs");
            Assertions.assertEquals(dedwe, value.getDedwe(), "dedwe");
            Assertions.assertEquals(dedwh, value.getDedwh(), "dedwh");

            return value;
        }
    }// class

    @Nested
    public class Sum {

        @Nested
        public class One {
            @Test
            public void a() {
                test(new HarmonicVector3EnergyErrorValueAndGradients(0, V_1, V_2, V_3, V_4, V_5, 13, 17));
            }

            @Test
            public void b() {
                test(new HarmonicVector3EnergyErrorValueAndGradients(2, V_2, V_3, V_4, V_5, V_6, 17, 19));
            }

            private void test(final HarmonicVector3EnergyErrorValueAndGradients value) {
                final HarmonicVector3EnergyErrorValueAndGradients total = Sum.this.test(value);
                Assertions.assertEquals(value, total);
            }
        }// class

        @Nested
        public class Two {

            @Test
            public void a() {
                test(1, V_1, V_2, V_3, V_4, V_5, 2, 3, 4, V_5, V_4, V_3, V_2, V_1, 6, 7);
            }

            @Test
            public void b() {
                test(3, V_1, V_2, V_3, V_4, V_5, 5, 7, 11, V_2, V_3, V_4, V_5, V_6, 13, 17);
            }

            private void test(final double e1, final ImmutableVector3 dedf01, final ImmutableVector3 dedf11,
                    final ImmutableVector3 dedf21, final ImmutableVector3 dedfc1, final ImmutableVector3 dedfs1,
                    final double dedwe1, final double dedwh1, final double e2, final ImmutableVector3 dedf02,
                    final ImmutableVector3 dedf12, final ImmutableVector3 dedf22, final ImmutableVector3 dedfc2,
                    final ImmutableVector3 dedfs2, final double dedwe2, final double dedwh2) {
                final var value1 = new HarmonicVector3EnergyErrorValueAndGradients(e1, dedf01, dedf11, dedf21, dedfc1,
                        dedfs1, dedwe1, dedwh1);
                final var value2 = new HarmonicVector3EnergyErrorValueAndGradients(e2, dedf02, dedf12, dedf22, dedfc2,
                        dedfs2, dedwe2, dedwh2);

                final var total = Sum.this.test(value1, value2);

                assertInvariants(value1, total);
                assertInvariants(value2, total);

                Assertions.assertEquals(e1 + e2, total.getE(), "e");
                Assertions.assertEquals(dedf01.plus(dedf02), total.getDedf0(), "dedf0");
                Assertions.assertEquals(dedf11.plus(dedf12), total.getDedf1(), "dedf1");
                Assertions.assertEquals(dedf21.plus(dedf22), total.getDedf2(), "dedf2");
                Assertions.assertEquals(dedfc1.plus(dedfc2), total.getDedfc(), "dedfc");
                Assertions.assertEquals(dedfs1.plus(dedfs2), total.getDedfs(), "dedfs");
                Assertions.assertEquals(dedwe1 + dedwe2, total.getDedwe(), "dedwe");
                Assertions.assertEquals(dedwh1 + dedwh2, total.getDedwh(), "dedwh");
            }
        }// class

        private HarmonicVector3EnergyErrorValueAndGradients test(
                final HarmonicVector3EnergyErrorValueAndGradients... values) {
            final HarmonicVector3EnergyErrorValueAndGradients total = HarmonicVector3EnergyErrorValueAndGradients
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
            final HarmonicVector3EnergyErrorValueAndGradients total = test();
            Assertions.assertEquals(HarmonicVector3EnergyErrorValueAndGradients.ZERO, total, "zero");
        }

    }// class

    private static ImmutableVector3 V_1 = ImmutableVector3.I;

    private static ImmutableVector3 V_2 = ImmutableVector3.J;

    private static ImmutableVector3 V_3 = ImmutableVector3.K;
    private static ImmutableVector3 V_4 = ImmutableVector3.create(1, 2, 3);
    private static ImmutableVector3 V_5 = ImmutableVector3.create(5, 7, 11);
    private static ImmutableVector3 V_6 = ImmutableVector3.create(13, 17, 19);

    public static void assertInvariants(final HarmonicVector3EnergyErrorValueAndGradients e) {
        ObjectTest.assertInvariants(e);// inherited

        assertNotNull(e.getDedf0(), "Not null, dedf0");
        assertNotNull(e.getDedf1(), "Not null, dedf1");
        assertNotNull(e.getDedf2(), "Not null, dedf2");
        assertNotNull(e.getDedfc(), "Not null, dedfc");
        assertNotNull(e.getDedfs(), "Not null, dedfs");
    }

    public static void assertInvariants(final HarmonicVector3EnergyErrorValueAndGradients e1,
            final HarmonicVector3EnergyErrorValueAndGradients e2) {
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

    public static void assertEquals(HarmonicVector3EnergyErrorValueAndGradients actual,
            HarmonicVector3EnergyErrorValueAndGradients expected, double delta, double wScale, double fScale, String message) {
        final double deltaDeDw = delta/wScale;
        final double deltaDeDf = delta/fScale;
        Assertions.assertEquals(expected.getE(), actual.getE(), delta, message + " e");
        Assertions.assertEquals(expected.getDedwe(), actual.getDedwe(), deltaDeDw, message + " dedwe");
        Assertions.assertEquals(expected.getDedwh(), actual.getDedwh(), deltaDeDw, message + " dedwh");
        Assertions.assertEquals(0.0, actual.getDedf0().minus(expected.getDedf0()).magnitude(), deltaDeDf, message + " dedf0");
        Assertions.assertEquals(0.0, actual.getDedf1().minus(expected.getDedf1()).magnitude(), deltaDeDf, message + " dedf1");
        Assertions.assertEquals(0.0, actual.getDedf2().minus(expected.getDedf2()).magnitude(), deltaDeDf, message + " dedf2");
        Assertions.assertEquals(0.0, actual.getDedfc().minus(expected.getDedfc()).magnitude(), deltaDeDf, message + " dedfc");
        Assertions.assertEquals(0.0, actual.getDedfs().minus(expected.getDedfs()).magnitude(), deltaDeDf, message + " dedfs");
    }
}
