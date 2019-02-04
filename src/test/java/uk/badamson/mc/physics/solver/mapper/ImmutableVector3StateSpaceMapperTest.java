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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * Unit tests for the {@link ImmutableVector3StateSpaceMapper} class.
 * </p>
 */
public class ImmutableVector3StateSpaceMapperTest {

    @Nested
    public class Constructor {

        @Test
        public void a() {
            test(0);
        }

        @Test
        public void b() {
            test(13);
        }

        private ImmutableVector3StateSpaceMapper test(final int index0) {
            final var mapper = new ImmutableVector3StateSpaceMapper(index0);

            assertInvariants(mapper);
            assertEquals(index0, mapper.getIndex0(),
                    "The index position origin of this mapper is equal to the given index position origin.");

            return mapper;
        }
    }// class

    public static int assertComponentIndexInvariants(final ImmutableVector3StateSpaceMapper mapper, final int i,
            final int j) {
        final int index = VectorStateSpaceMapperTest.assertComponentIndexInvariants(mapper, i, j);// inherited

        assertInvariants(mapper);// check for side effects

        return index;
    }

    public static void assertInvariants(final ImmutableVector3StateSpaceMapper mapper) {
        VectorStateSpaceMapperTest.assertInvariants(mapper);// inherited

        assertEquals(3, mapper.getDimension(), "Number of dimensions");
        assertThat("The index position origin is not negative.", Integer.valueOf(mapper.getIndex0()),
                greaterThanOrEqualTo(Integer.valueOf(0)));
    }

    public static void assertInvariants(final ImmutableVector3StateSpaceMapper mapper1,
            final ImmutableVector3StateSpaceMapper mapper2) {
        VectorStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static void fromObject(final ImmutableVector3StateSpaceMapper mapper, final double[] state,
            final ImmutableVector3 vector) {
        VectorStateSpaceMapperTest.fromObject(mapper, state, vector);

        assertInvariants(mapper);// check for side-effects
    }

    private static void fromObject(final int index0, final int stateSize, final ImmutableVector3 v) {
        final double tolerance = v.magnitude() * (Math.nextAfter(1.0, 2.0) - 1.0);
        final ImmutableVector3StateSpaceMapper mapper = new ImmutableVector3StateSpaceMapper(index0);
        final double[] state = new double[stateSize];

        fromObject(mapper, state, v);

        assertEquals(v.get(0), state[index0], tolerance, "state[index0]");
        assertEquals(v.get(1), state[index0 + 1], tolerance, "state[index0+1]");
        assertEquals(v.get(2), state[index0 + 2], tolerance, "state[index0+2]");
    }

    private static void fromToObjectSymmetry(final ImmutableVector3StateSpaceMapper mapper, final double[] state,
            final ImmutableVector3 original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final ImmutableVector3 reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    private static void fromToObjectSymmetry(final int index0, final int stateSize, final ImmutableVector3 original) {
        final ImmutableVector3StateSpaceMapper mapper = new ImmutableVector3StateSpaceMapper(index0);
        final double[] state = new double[stateSize];
        Arrays.fill(state, Double.NaN);

        fromToObjectSymmetry(mapper, state, original);
    }

    private static void fromToVectorSymmetry(final ImmutableVector3StateSpaceMapper mapper, final double[] state,
            final ImmutableVector3 original) {
        mapper.fromVector(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final ImmutableVector3 reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    private static void fromToVectorSymmetry(final int index0, final int stateSize, final ImmutableVector3 original) {
        final ImmutableVector3StateSpaceMapper mapper = new ImmutableVector3StateSpaceMapper(index0);
        final double[] state = new double[stateSize];

        fromToVectorSymmetry(mapper, state, original);
    }

    public static void fromVector(final ImmutableVector3StateSpaceMapper mapper, final double[] state,
            final Vector vector) {
        VectorStateSpaceMapperTest.fromVector(mapper, state, vector);

        assertInvariants(mapper);// check for side-effects
    }

    public static int getComponentIndex(final ImmutableVector3StateSpaceMapper mapper, final int i) {
        final int index = VectorStateSpaceMapperTest.assertComponentIndexInvariants(mapper, i);// inherited

        assertInvariants(mapper);// check for side effects
        assertEquals(mapper.getIndex0() + i, index,
                "The component index of row i is equal to the index origin of this mapper plus i.");

        return index;
    }

    public static ImmutableVector3 toObject(final ImmutableVector3StateSpaceMapper mapper,
            final ImmutableVectorN state) {
        final ImmutableVector3 vector = VectorStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return vector;
    }

    @Test
    public void fromObject_2i() {
        final int index = 0;
        final int stateSize = 3;
        fromObject(index, stateSize, ImmutableVector3.I.scale(2));
    }

    @Test
    public void fromObject_extraAfter() {
        final int index = 0;
        final int stateSize = 5;
        fromObject(index, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromObject_extraBefore() {
        final int index = 2;
        final int stateSize = 5;
        fromObject(index, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromObject_i() {
        final int index = 0;
        final int stateSize = 3;
        fromObject(index, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromObject_j() {
        final int index = 0;
        final int stateSize = 3;
        fromObject(index, stateSize, ImmutableVector3.J);
    }

    @Test
    public void fromObject_k() {
        final int index = 0;
        final int stateSize = 3;
        fromObject(index, stateSize, ImmutableVector3.K);
    }

    @Test
    public void fromToObjectSymmetry_2i() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToObjectSymmetry(index0, stateSize, ImmutableVector3.I.scale(2));
    }

    @Test
    public void fromToObjectSymmetry_extraAfter() {
        final int index0 = 0;
        final int stateSize = 5;
        fromToObjectSymmetry(index0, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromToObjectSymmetry_extraBefore() {
        final int index0 = 2;
        final int stateSize = 5;
        fromToObjectSymmetry(index0, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromToObjectSymmetry_i() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToObjectSymmetry(index0, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromToObjectSymmetry_j() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToObjectSymmetry(index0, stateSize, ImmutableVector3.J);
    }

    @Test
    public void fromToObjectSymmetry_k() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToObjectSymmetry(index0, stateSize, ImmutableVector3.K);
    }

    @Test
    public void fromToVectorSymmetry_2i() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToVectorSymmetry(index0, stateSize, ImmutableVector3.I.scale(2));
    }

    @Test
    public void fromToVectorSymmetry_extraAfter() {
        final int index0 = 0;
        final int stateSize = 5;
        fromToVectorSymmetry(index0, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromToVectorSymmetry_extraBefore() {
        final int index0 = 2;
        final int stateSize = 5;
        fromToVectorSymmetry(index0, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromToVectorSymmetry_i() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToVectorSymmetry(index0, stateSize, ImmutableVector3.I);
    }

    @Test
    public void fromToVectorSymmetry_j() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToVectorSymmetry(index0, stateSize, ImmutableVector3.J);
    }

    @Test
    public void fromToVectorSymmetry_k() {
        final int index0 = 0;
        final int stateSize = 3;
        fromToVectorSymmetry(index0, stateSize, ImmutableVector3.K);
    }
}
