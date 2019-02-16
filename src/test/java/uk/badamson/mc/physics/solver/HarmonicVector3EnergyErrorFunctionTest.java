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
import static org.junit.jupiter.api.Assertions.assertAll;
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

import edu.umd.cs.findbugs.annotations.NonNull;
import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.PoorlyConditionedFunctionException;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.HarmonicVector3Test;
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

    @Nested
    public class Value {

        @Test
        public void a() {
            final int index0 = 0;
            final Duration scale = Duration.ofSeconds(1);
            final double e = 2;
            final ImmutableVector3 dedf0 = ImmutableVector3.create(3, 5, 7);
            final ImmutableVector3 dedf1 = ImmutableVector3.create(11, 13, 17);
            final ImmutableVector3 dedf2 = ImmutableVector3.create(19, 23, 29);
            final ImmutableVector3 dedfc = ImmutableVector3.create(4, 6, 8);
            final ImmutableVector3 dedfs = ImmutableVector3.create(10, 12, 14);
            final double dedwe = 16;
            final double dedwh = 18;

            test(index0, scale, e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);
        }

        @Test
        public void b() {
            final int index0 = 0;
            final Duration scale = Duration.ofSeconds(1);
            final double e = 12;
            final ImmutableVector3 dedf0 = ImmutableVector3.create(13, 15, 17);
            final ImmutableVector3 dedf1 = ImmutableVector3.create(21, 23, 27);
            final ImmutableVector3 dedf2 = ImmutableVector3.create(29, 33, 39);
            final ImmutableVector3 dedfc = ImmutableVector3.create(14, 16, 18);
            final ImmutableVector3 dedfs = ImmutableVector3.create(20, 22, 24);
            final double dedwe = 26;
            final double dedwh = 28;

            test(index0, scale, e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);
        }

        @Test
        public void c() {
            final int index0 = 16;
            final Duration scale = Duration.ofMillis(1);
            final double e = 2;
            final ImmutableVector3 dedf0 = ImmutableVector3.create(3, 5, 7);
            final ImmutableVector3 dedf1 = ImmutableVector3.create(11, 13, 17);
            final ImmutableVector3 dedf2 = ImmutableVector3.create(19, 23, 29);
            final ImmutableVector3 dedfc = ImmutableVector3.create(4, 6, 8);
            final ImmutableVector3 dedfs = ImmutableVector3.create(10, 12, 14);
            final double dedwe = 16;
            final double dedwh = 18;

            test(index0, scale, e, dedf0, dedf1, dedf2, dedfc, dedfs, dedwe, dedwh);
        }

        private void test(final int index0, final Duration scale, final double e, final ImmutableVector3 dedf0,
                final ImmutableVector3 dedf1, final ImmutableVector3 dedf2, final ImmutableVector3 dedfc,
                final ImmutableVector3 dedfs, final double dedwe, final double dedwh) {
            final var valueAndGradient = new HarmonicVector3EnergyErrorValueAndGradients(e, dedf0, dedf1, dedf2, dedfc,
                    dedfs, dedwe, dedwh);
            final var mapper = new HarmonicVector3Mapper(index0, scale);
            final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> term = (
                    vector) -> valueAndGradient;
            final var f = new HarmonicVector3EnergyErrorFunction(mapper, List.of(term));
            final double[] stateElements = new double[mapper.getMinimumStateSpaceDimension()];
            mapper.fromObject(stateElements, v1);
            final ImmutableVectorN state = ImmutableVectorN.create(stateElements);

            final FunctionNWithGradientValue value = value(f, state);

            assertEquals(e, value.getF(), "Function e value");
            final ImmutableVectorN dfDx = value.getDfDx();
            assertEquals(dedwe, dfDx.get(mapper.getWeIndex()), "Function dedwe value");
            assertEquals(dedwh, dfDx.get(mapper.getWhIndex()), "Function dedwh value");
            for (int i = 0; i < 3; ++i) {
                final int component = i;
                assertAll("Function gradient value [" + i + "]",
                        () -> assertEquals(dedf0.get(component),
                                dfDx.get(mapper.getF0Mapper().getComponentIndex(component)), "dedf0 value"),
                        () -> assertEquals(dedf1.get(component),
                                dfDx.get(mapper.getF1Mapper().getComponentIndex(component)), "dedf1 value"),
                        () -> assertEquals(dedf2.get(component),
                                dfDx.get(mapper.getF2Mapper().getComponentIndex(component)), "dedf2 value"),
                        () -> assertEquals(dedfc.get(component),
                                dfDx.get(mapper.getFcMapper().getComponentIndex(component)), "dedfc value"),
                        () -> assertEquals(dedfs.get(component),
                                dfDx.get(mapper.getFsMapper().getComponentIndex(component)), "dedfs value"));
            } // for
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

    public static @NonNull HarmonicVector3 minimiseEnergyError(final HarmonicVector3EnergyErrorFunction f,
            @NonNull final HarmonicVector3 f0, final double tolerance) throws PoorlyConditionedFunctionException {
        final double e0 = f.apply(f0).getE();

        final HarmonicVector3 minimum = f.minimiseEnergyError(f0, tolerance);

        assertInvariants(f);// check for side-effects
        assertNotNull(minimum, "Not null, result");
        HarmonicVector3Test.assertInvariants(minimum);
        final double e = f.apply(minimum).getE();
        assertThat("Did not increase the the error", Double.valueOf(e), lessThanOrEqualTo(Double.valueOf(e0)));

        return minimum;
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

    public static FunctionNWithGradientValue value(final HarmonicVector3EnergyErrorFunction f,
            final ImmutableVectorN state) {
        final FunctionNWithGradientValue v = EnergyErrorFunctionTest.value(f, state);

        assertInvariants(f);

        return v;
    }

}
