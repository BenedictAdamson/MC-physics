package uk.badamson.mc.physics.solver.mapper;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.HarmonicVector3Test;

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

/**
 * <p>
 * Unit tests and auxiliary functions for the class
 * {@link HarmonicVector3Mapper}.
 * </p>
 */
public class HarmonicVector3MapperTest {

    @Nested
    public class Constructor {

        @Test
        public void a() {
            test(0, SCALE_1);
        }

        @Test
        public void b() {
            test(13, SCALE_2);
        }

        private HarmonicVector3Mapper test(final int index0, final Duration scale) {
            final HarmonicVector3Mapper mapper = new HarmonicVector3Mapper(index0, scale);

            assertInvariants(mapper);

            return mapper;
        }
    }// class

    @Nested
    public class FromObject {

        @Test
        public void a() {
            test(0, SCALE_1, f1);
        }

        @Test
        public void b() {
            test(5, SCALE_2, f2);
        }

        private void test(final int index0, final Duration scale, final HarmonicVector3 object) {
            final HarmonicVector3Mapper mapper = new HarmonicVector3Mapper(index0, scale);
            final double[] state = new double[index0 + 20];

            fromObject(mapper, state, object);
        }
    }// class

    @Nested
    public class FromToObjectSymmetry {

        @Test
        public void a() {
            test(0, SCALE_1, f1);
        }

        @Test
        public void b() {
            test(5, SCALE_2, f2);
        }

        private void test(final int index0, final Duration scale, final HarmonicVector3 object) {
            final HarmonicVector3Mapper mapper = new HarmonicVector3Mapper(index0, scale);
            final double[] state = new double[index0 + 20];

            fromToObjectSymmetry(mapper, state, object);
        }
    }// class

    private static final Duration SCALE_1 = Duration.ofSeconds(1);

    private static final Duration SCALE_2 = Duration.ofMillis(1);

    private static final Duration T_1 = Duration.ofSeconds(0);

    private static final Duration T_2 = Duration.ofSeconds(2);

    private static final ImmutableVector3 V_1 = ImmutableVector3.I;

    private static final ImmutableVector3 V_2 = ImmutableVector3.J;

    private static final ImmutableVector3 V_3 = ImmutableVector3.K;

    private static final ImmutableVector3 V_4 = ImmutableVector3.create(1, 2, 3);

    private static final ImmutableVector3 V_5 = ImmutableVector3.create(4, 3, 2);

    private static final ImmutableVector3 V_6 = ImmutableVector3.create(3, 4, 5);

    public static void assertInvariants(final HarmonicVector3Mapper mapper) {
        ObjectTest.assertInvariants(mapper);// inherited
        ObjectStateSpaceMapperTest.assertInvariants(mapper);// inherited
    }

    public static void assertInvariants(final HarmonicVector3Mapper mapper1, final HarmonicVector3Mapper mapper2) {
        ObjectTest.assertInvariants(mapper1, mapper2);// inherited
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static void fromObject(final HarmonicVector3Mapper mapper, final double[] state,
            final HarmonicVector3 object) {
        ObjectStateSpaceMapperTest.fromObject(mapper, state, object);// inherited

        assertInvariants(mapper);// check for side-effects
        HarmonicVector3Test.assertInvariants(object);// check for side-effects
    }

    public static void fromToObjectSymmetry(final HarmonicVector3Mapper mapper, final double[] state,
            final HarmonicVector3 original) {
        ObjectStateSpaceMapperTest.fromToObjectSymmetry(mapper, state, original);// inherited
        assertInvariants(mapper);// check for side-effects
    }

    private HarmonicVector3 f1;

    private HarmonicVector3 f2;

    @BeforeEach
    public void setUp() {
        f1 = new HarmonicVector3(T_1, V_1, V_2, V_3, V_4, V_5, 3, 5);
        f2 = new HarmonicVector3(T_2, V_2, V_3, V_4, V_5, V_6, 13, 17);
    }
}
