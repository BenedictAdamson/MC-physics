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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.solver.mapper.HarmonicVector3Mapper;
import uk.badamson.mc.physics.solver.mapper.HarmonicVector3MapperTest;

/**
 * <p>
 * Units tests and auxiliary test code for the class
 * {@link HarmonicVector3EnergyErrorFunction}
 * </p>
 */
public class HarmonicVector3EnergyErrorFunctionTest {

    @Nested
    public class Apply {

        @Nested
        public class One {
            @Test
            public void a() {
                test(term1, v1);
            }

            @Test
            public void b() {
                test(term1, v2);
            }

            @Test
            public void c() {
                test(term2, v1);
            }

            @Test
            public void d() {
                test(term2, v2);
            }

            private void test(final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> term,
                    final HarmonicVector3 v) {
                final HarmonicVector3EnergyErrorValueAndGradients termValue = term.apply(v);
                final HarmonicVector3EnergyErrorFunction function = new HarmonicVector3EnergyErrorFunction(mapper1,
                        List.of(term));

                final HarmonicVector3EnergyErrorValueAndGradients value = apply(function, v);

                assertEquals(termValue, value, "Delegates to the sole term");
            }
        }// class

        @Nested
        public class Two {
            @Test
            public void a() {
                test(term1, term2, v1);
            }

            @Test
            public void b() {
                test(term2, term2, v1);
            }

            private void test(final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> term1,
                    final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> term2,
                    final HarmonicVector3 v) {
                final HarmonicVector3EnergyErrorValueAndGradients value1 = term1.apply(v);
                final HarmonicVector3EnergyErrorValueAndGradients value2 = term2.apply(v);
                final HarmonicVector3EnergyErrorFunction function = new HarmonicVector3EnergyErrorFunction(mapper1,
                        List.of(term1, term2));

                final HarmonicVector3EnergyErrorValueAndGradients value = apply(function, v);

                assertEquals(HarmonicVector3EnergyErrorValueAndGradients.sum(value1, value2), value,
                        "The total error is the sum of the errors computed for each of the terms.");
            }
        }// class

        private HarmonicVector3EnergyErrorValueAndGradients apply(final HarmonicVector3EnergyErrorFunction function,
                final HarmonicVector3 v) {
            final HarmonicVector3EnergyErrorValueAndGradients value = function.apply(v);

            assertNotNull(value, "Not null, result");
            HarmonicVector3EnergyErrorValueAndGradientsTest.assertInvariants(value);

            return value;
        }

        @Test
        public void empty() {
            final HarmonicVector3EnergyErrorFunction function = new HarmonicVector3EnergyErrorFunction(mapper1,
                    Collections.emptyList());

            final HarmonicVector3EnergyErrorValueAndGradients value = apply(function, v1);

            assertEquals(HarmonicVector3EnergyErrorValueAndGradients.ZERO, value, "value is zero");
        }
    }// class

    @Nested
    public class Constructor {

        @Test
        public void a() {
            test(mapper1, Collections.emptyList());
        }

        @Test
        public void b() {
            test(mapper2, List.of(term1, term2));
        }

        private HarmonicVector3EnergyErrorFunction test(final HarmonicVector3Mapper mapper,
                final List<Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>> terms) {
            final HarmonicVector3EnergyErrorFunction function = new HarmonicVector3EnergyErrorFunction(mapper, terms);

            assertInvariants(function);
            assertSame(mapper, function.getMapper(), "The mapper of this is the given mapper.");
            assertEquals(terms, function.getTerms(), "The terms of this contains equals the given terms.");

            return function;
        }
    }// class

    private static HarmonicVector3Mapper mapper1;
    private static HarmonicVector3Mapper mapper2;
    private static Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> term1;
    private static Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> term2;
    private static HarmonicVector3 v1;

    private static HarmonicVector3 v2;

    public static void assertInvariants(final HarmonicVector3EnergyErrorFunction f) {
        ObjectTest.assertInvariants(f);// inherited
        EnergyErrorFunctionTest.assertInvariants(f);// inherited

        final HarmonicVector3Mapper mapper = f.getMapper();
        final List<Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>> terms = f.getTerms();
        assertNotNull(mapper, "Not null, mapper");// guard
        assertNotNull(terms, "Always have a collection of terms.");// guard

        HarmonicVector3MapperTest.assertInvariants(mapper);

        assertEquals(mapper.getMinimumStateSpaceDimension(), f.getDimension(),
                "The dimension of the function equals the minimum space dimension of the mapper.");
        for (final var term : terms) {
            assertNotNull(term, "The collection of terms does not contain a null term.");
        }
    }

    public static void assertInvariants(final HarmonicVector3EnergyErrorFunction f1,
            final HarmonicVector3EnergyErrorFunction f2) {
        ObjectTest.assertInvariants(f1, f2);// inherited
        EnergyErrorFunctionTest.assertInvariants(f1, f2);// inherited
    }

    @BeforeAll
    public static void setUp() {
        mapper1 = new HarmonicVector3Mapper(0, Duration.ofSeconds(1));
        mapper2 = new HarmonicVector3Mapper(13, Duration.ofMillis(1));
        term1 = (f) -> HarmonicVector3EnergyErrorValueAndGradients.ZERO;
        term2 = (f) -> new HarmonicVector3EnergyErrorValueAndGradients(1, ImmutableVector3.I, ImmutableVector3.J,
                ImmutableVector3.K, ImmutableVector3.I, ImmutableVector3.J, 2, 3);
        v1 = new HarmonicVector3(Duration.ofSeconds(2), ImmutableVector3.J, ImmutableVector3.K, ImmutableVector3.I,
                ImmutableVector3.J, ImmutableVector3.K, 3, 4);
        v2 = new HarmonicVector3(Duration.ofSeconds(3), ImmutableVector3.K, ImmutableVector3.I, ImmutableVector3.J,
                ImmutableVector3.K, ImmutableVector3.I, 5, 7);
    }
}
