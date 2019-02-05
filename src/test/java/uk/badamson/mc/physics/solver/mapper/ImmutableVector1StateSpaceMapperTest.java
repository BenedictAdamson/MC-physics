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

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector1;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * Unit tests for the {@link ImmutableVector1StateSpaceMapper} class.
 * </p>
 */
public class ImmutableVector1StateSpaceMapperTest {

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

        private ImmutableVector1StateSpaceMapper test(final int index) {
            final var mapper = new ImmutableVector1StateSpaceMapper(index);

            assertInvariants(mapper);
            assertEquals(index, mapper.getIndex(), "The index of this mapper is equal to the given index.");

            return mapper;
        }
    }

    public static int assertComponentIndexInvariants(final ImmutableVector1StateSpaceMapper mapper, final int i) {
        final int index = VectorStateSpaceMapperTest.assertComponentIndexInvariants(mapper, i);// inherited

        assertThat("The component of the state-pace vector is non negative.", Integer.valueOf(index),
                greaterThanOrEqualTo(Integer.valueOf(0)));
        assertEquals(mapper.getComponentIndex(i, 0), index,
                "The component of row i is equal to the component of row i of column 0.");

        return index;
    }

    public static int assertComponentIndexInvariants(final ImmutableVector1StateSpaceMapper mapper, final int i,
            final int j) {
        final int index = VectorStateSpaceMapperTest.assertComponentIndexInvariants(mapper, i, j);// inherited

        assertEquals(mapper.getIndex(), index,
                "The component index of the (sole) component is equal to the index of this mapper.");
        return index;
    }

    public static void assertInvariants(final ImmutableVector1StateSpaceMapper mapper) {
        ObjectTest.assertInvariants(mapper);// inherited
        VectorStateSpaceMapperTest.assertInvariants(mapper);// inherited

        final int size = mapper.getSize();
        assertEquals(1, size, "size");
        final int dimension = mapper.getDimension();
        assertEquals(dimension, 1, "Number of dimensions");
        assertComponentIndexInvariants(mapper, 0);
        assertComponentIndexInvariants(mapper, 0, 0);
    }

    public static void assertInvariants(final ImmutableVector1StateSpaceMapper mapper1,
            final ImmutableVector1StateSpaceMapper mapper2) {
        VectorStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static void fromObject(final ImmutableVector1StateSpaceMapper mapper, final double[] state,
            final ImmutableVector1 vector) {
        VectorStateSpaceMapperTest.fromObject(mapper, state, vector);

        assertInvariants(mapper);// check for side-effects
    }

    private static void fromObject(final int index, final int stateSize, final double x) {
        final double tolerance = Math.abs(x) * (Math.nextAfter(1.0, 2.0) - 1.0);
        final ImmutableVector1StateSpaceMapper mapper = new ImmutableVector1StateSpaceMapper(index);
        final double[] state = new double[stateSize];
        state[index] = Double.NaN;
        final ImmutableVector1 vector = ImmutableVector1.create(x);

        fromObject(mapper, state, vector);

        assertEquals(x, state[index], tolerance, "state[index]");
    }

    private static void fromToObjectSymmetry(final ImmutableVector1StateSpaceMapper mapper, final double[] state,
            final ImmutableVector1 original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final ImmutableVector1 reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    private static void fromToObjectSymmetry(final int index, final int stateSize, final ImmutableVector1 original) {
        final ImmutableVector1StateSpaceMapper mapper = new ImmutableVector1StateSpaceMapper(index);
        final double[] state = new double[stateSize];
        Arrays.fill(state, Double.NaN);

        fromToObjectSymmetry(mapper, state, original);
    }

    private static void fromToVectorSymmetry(final ImmutableVector1StateSpaceMapper mapper, final double[] state,
            final ImmutableVector1 original) {
        mapper.fromVector(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final ImmutableVector1 reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    private static void fromToVectorSymmetry(final int index, final int stateSize, final ImmutableVector1 original) {
        final ImmutableVector1StateSpaceMapper mapper = new ImmutableVector1StateSpaceMapper(index);
        final double[] state = new double[stateSize];

        fromToVectorSymmetry(mapper, state, original);
    }

    public static void fromVector(final ImmutableVector1StateSpaceMapper mapper, final double[] state,
            final Vector vector) {
        VectorStateSpaceMapperTest.fromVector(mapper, state, vector);

        assertInvariants(mapper);// check for side-effects
    }

    public static ImmutableVector1 toObject(final ImmutableVector1StateSpaceMapper mapper,
            final ImmutableVectorN state) {
        final ImmutableVector1 vector = VectorStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return vector;
    }

    @Test
    public void fromObject_2i() {
        final int index = 0;
        final int stateSize = 1;
        fromObject(index, stateSize, 2.0);
    }

    @Test
    public void fromObject_i() {
        final int index = 0;
        final int stateSize = 1;
        fromObject(index, stateSize, 1.0);
    }

    @Test
    public void fromObject_initialState() {
        final int index = 0;
        final int stateSize = 1;
        fromObject(index, stateSize, 1.0);
    }

    @Test
    public void fromToObjectSymmetry_2i() {
        final int index = 0;
        final int stateSize = 1;
        fromToObjectSymmetry(index, stateSize, ImmutableVector1.I.scale(2));
    }

    @Test
    public void fromToObjectSymmetry_i() {
        final int index = 0;
        final int stateSize = 1;
        fromToObjectSymmetry(index, stateSize, ImmutableVector1.I);
    }

    @Test
    public void fromToVectorSymmetry_2i() {
        final int index = 0;
        final int stateSize = 1;
        fromToVectorSymmetry(index, stateSize, ImmutableVector1.I.scale(2));
    }

    @Test
    public void fromToVectorSymmetry_extraAfter() {
        final int index = 0;
        final int stateSize = 2;
        fromToVectorSymmetry(index, stateSize, ImmutableVector1.I);
    }

    @Test
    public void fromToVectorSymmetry_extraBefore() {
        final int index = 2;
        final int stateSize = 5;
        fromToVectorSymmetry(index, stateSize, ImmutableVector1.I);
    }

    @Test
    public void fromToVectorSymmetry_i() {
        final int index = 0;
        final int stateSize = 3;
        fromToVectorSymmetry(index, stateSize, ImmutableVector1.I);
    }
}
