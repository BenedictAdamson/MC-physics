package uk.badamson.mc.physics.solver.mapper;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;

/**
 * <p>
 * Unit tests and auxiliary test code for the {@link DurationMapper} class.
 * </p>
 */
public class DurationMapperTest {

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

        private DurationMapper test(final int index0, final Duration scale) {
            final DurationMapper mapper = new DurationMapper(index0, scale);

            assertInvariants(mapper);
            assertTrue(mapper.isValidForDimension(index0 + 1),
                    "This mapper is valid for a state space dimension vector if, and only if, "
                            + "the dimension is at least 1 more than the given index position origin (minimum).");

            return mapper;
        }
    }// class

    @Nested
    public class FromObject {

        @Test
        public void a() {
            test(0, SCALE_1, T_1);
        }

        @Test
        public void b() {
            test(5, SCALE_2, T_2);
        }

        private void test(final int index0, final Duration scale, final Duration object) {
            final DurationMapper mapper = new DurationMapper(index0, scale);
            final double[] state = new double[index0 + 17];

            fromObject(mapper, state, object);
        }
    }// class

    @Nested
    public class FromToObjectSymmetry {

        @Test
        public void a() {
            test(0, SCALE_1, T_1);
        }

        @Test
        public void b() {
            test(5, SCALE_2, T_2);
        }

        private void test(final int index0, final Duration scale, final Duration object) {
            final DurationMapper mapper = new DurationMapper(index0, scale);
            final double[] state = new double[index0 + 17];
            Arrays.fill(state, Double.NaN);

            fromToObjectSymmetry(mapper, state, object);
        }
    }// class

    private static final Duration SCALE_1 = Duration.ofSeconds(1);

    private static final Duration SCALE_2 = Duration.ofMillis(1);

    private static final Duration T_1 = Duration.ofSeconds(0);

    private static final Duration T_2 = Duration.ofSeconds(2);

    public static void assertInvariants(final DurationMapper mapper) {
        ObjectTest.assertInvariants(mapper);// inherited
        ObjectStateSpaceMapperTest.assertInvariants(mapper);// inherited
    }

    public static void assertInvariants(final DurationMapper mapper1, final DurationMapper mapper2) {
        ObjectTest.assertInvariants(mapper1, mapper2);// inherited
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static void fromObject(final DurationMapper mapper, final double[] state, final Duration object) {
        ObjectStateSpaceMapperTest.fromObject(mapper, state, object);// inherited

        assertInvariants(mapper);// check for side-effects
    }

    public static void fromToObjectSymmetry(final DurationMapper mapper, final double[] state,
            final Duration original) {
        ObjectStateSpaceMapperTest.fromToObjectSymmetry(mapper, state, original);// inherited
        assertInvariants(mapper);// check for side-effects
    }
}
