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

            @Test
            public void a() {
                test(0, V_1, V_2, V_3, V_4, V_5, 13, 17);
            }

            @Test
            public void b() {
                test(2, V_2, V_3, V_4, V_5, V_6, 17, 19);
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

        public static void assertInvariants(final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients e) {
            ObjectTest.assertInvariants(e);// inherited
        }

        public static void assertInvariants(final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients e1,
                final HarmonicVector3EnergyErrorFunction.ErrorValueAndGradients e2) {
            ObjectTest.assertInvariants(e1, e2);// inherited
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
