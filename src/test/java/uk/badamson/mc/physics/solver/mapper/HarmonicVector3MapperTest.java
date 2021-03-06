package uk.badamson.mc.physics.solver.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.Arrays;

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
            Arrays.fill(state, Double.NaN);

            fromToObjectSymmetry(mapper, state, object);
        }
    }// class

    private static final Integer ZERO = Integer.valueOf(0);

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

        final DurationMapper t0Mapper = mapper.getT0Mapper();
        final ImmutableVector3StateSpaceMapper f0Mapper = mapper.getF0Mapper();
        final ImmutableVector3StateSpaceMapper f1Mapper = mapper.getF1Mapper();
        final ImmutableVector3StateSpaceMapper f2Mapper = mapper.getF2Mapper();
        final ImmutableVector3StateSpaceMapper fcMapper = mapper.getFcMapper();
        final ImmutableVector3StateSpaceMapper fsMapper = mapper.getFsMapper();

        assertAll("Always have mappers for the terms of the functor", () -> assertNotNull(t0Mapper, "time origin."),
                () -> assertNotNull(f0Mapper, "constant term."), () -> assertNotNull(f1Mapper, "linear term."),
                () -> assertNotNull(f2Mapper, "quadratic term."), () -> assertNotNull(fcMapper, "cosine term."),
                () -> assertNotNull(fcMapper, "sine term"));

        DurationMapperTest.assertInvariants(t0Mapper);
        ImmutableVector3StateSpaceMapperTest.assertInvariants(f0Mapper);
        ImmutableVector3StateSpaceMapperTest.assertInvariants(f1Mapper);
        ImmutableVector3StateSpaceMapperTest.assertInvariants(f2Mapper);
        ImmutableVector3StateSpaceMapperTest.assertInvariants(fcMapper);
        ImmutableVector3StateSpaceMapperTest.assertInvariants(fsMapper);

        final int size = mapper.getSize();
        final int index0 = mapper.getIndex0();
        final Integer index0Integer = Integer.valueOf(index0);
        assertEquals(18, size, "size");
        assertThat("The index position origin is not negative.", index0Integer,
                greaterThanOrEqualTo(Integer.valueOf(0)));
        assertEquals(mapper.getMinimumStateSpaceDimension(), index0 + size,
                "The mapper maps to contiguous components (size).");
        assertWeIndexInvariants(mapper);
        assertWhIndexInvariants(mapper);
        final int t0MapperIndex = t0Mapper.getIndex();
        final int f0MapperIndex = f0Mapper.getIndex0();
        final int f1MapperIndex = f1Mapper.getIndex0();
        final int f2MapperIndex = f2Mapper.getIndex0();
        final int fcMapperIndex = fcMapper.getIndex0();
        final int fsMapperIndex = fsMapper.getIndex0();
        assertAll("This mapper maps values contiguously (index origins of component mappers)",
                () -> assertThat("time origin", Integer.valueOf(t0MapperIndex), greaterThanOrEqualTo(index0Integer)),
                () -> assertThat("constant term", Integer.valueOf(f0MapperIndex), greaterThanOrEqualTo(index0Integer)),
                () -> assertThat("linear term.", Integer.valueOf(f1MapperIndex), greaterThanOrEqualTo(index0Integer)),
                () -> assertThat("quadratic term", Integer.valueOf(f2MapperIndex), greaterThanOrEqualTo(index0Integer)),
                () -> assertThat("cosine term", Integer.valueOf(fcMapperIndex), greaterThanOrEqualTo(index0Integer)),
                () -> assertThat("sine term", Integer.valueOf(fsMapperIndex), greaterThanOrEqualTo(index0Integer)));
        assertAll("This mapper maps values contiguously (adjacency of component mappers)", () -> assertEquals(
                mapper.getWhIndex() + 1, t0MapperIndex,
                "The mapper for the time origin maps to positions just after the position of the harmonic frequency term."),
                () -> assertEquals(t0MapperIndex + t0Mapper.getSize(), f0MapperIndex,
                        "The mapper for the constant term maps to positions just after the position mapped to by the duration term mapper."),
                () -> assertEquals(f0MapperIndex + f0Mapper.getSize(), f1MapperIndex,
                        "The mapper for the linear term maps to positions just after the position mapped to by the constant term mapper."),
                () -> assertEquals(f1MapperIndex + f1Mapper.getSize(), f2MapperIndex,
                        "The mapper for the quadratic term maps to positions just after the position mapped to by the linear term mapper."),
                () -> assertEquals(f2MapperIndex + f2Mapper.getSize(), fcMapperIndex,
                        "The mapper for the cosine term maps to positions just after the position mapped to by the quadratic term mapper."),
                () -> assertEquals(fcMapperIndex + fcMapper.getSize(), fsMapperIndex,
                        "The mapper for the sine term maps to positions just after the position mapped to by the cosine term mapper."));
    }

    public static void assertInvariants(final HarmonicVector3Mapper mapper1, final HarmonicVector3Mapper mapper2) {
        ObjectTest.assertInvariants(mapper1, mapper2);// inherited
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    private static int assertWeIndexInvariants(final HarmonicVector3Mapper mapper) {
        final int index = mapper.getWeIndex();

        final Integer indexInteger = Integer.valueOf(index);
        final int index0 = mapper.getIndex0();
        assertThat("The index of the exponential frequency term is an index", indexInteger, greaterThanOrEqualTo(ZERO));
        assertThat("This mapper maps values contiguously (weIndex)", indexInteger,
                greaterThanOrEqualTo(Integer.valueOf(index0)));
        assertEquals(index0, index, "The index of the exponential frequency term");

        return index;
    }

    private static int assertWhIndexInvariants(final HarmonicVector3Mapper mapper) {
        final int index = mapper.getWhIndex();

        final Integer indexInteger = Integer.valueOf(index);
        final int index0 = mapper.getIndex0();
        assertThat("The index of the harmonic frequency term is an index", indexInteger, greaterThanOrEqualTo(ZERO));
        assertThat("This mapper maps values contiguously (whIndex)", indexInteger,
                greaterThanOrEqualTo(Integer.valueOf(index0)));
        assertEquals(mapper.getWeIndex() + 1, index,
                "This maps the harmonic frequency term to the position just after the exponential frequency term.");

        return index;
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
