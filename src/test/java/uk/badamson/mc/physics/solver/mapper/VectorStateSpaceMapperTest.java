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

import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * Unit tests for classes that implement the {@link VectorStateSpaceMapper}
 * interface.
 */
public class VectorStateSpaceMapperTest {

    public static <VECTOR extends Vector> int assertComponentIndexInvariants(
            final VectorStateSpaceMapper<VECTOR> mapper, final int i) {
        final int index = mapper.getComponentIndex(i);

        assertThat("The component of the state-pace vector is non negative.", Integer.valueOf(index),
                greaterThanOrEqualTo(Integer.valueOf(0)));
        assertEquals(mapper.getComponentIndex(i, 0), index,
                "The component of row i is equal to the component of row i of column 0.");

        return index;
    }

    public static <VECTOR extends Vector> int assertComponentIndexInvariants(
            final VectorStateSpaceMapper<VECTOR> mapper, final int i, final int j) {
        final int index = MatrixStateSpaceMapperTest.assertComponentIndexInvariants(mapper, i, j);// inherited

        return index;
    }

    public static <VECTOR extends Vector> void assertInvariants(final VectorStateSpaceMapper<VECTOR> mapper) {
        MatrixStateSpaceMapperTest.assertInvariants(mapper);// inherited

        final int dimension = mapper.getDimension();
        assertEquals(dimension, mapper.getSize(), "The size equals the number of dimensions.");
        assertThat("Number of dimensions", Integer.valueOf(dimension),
                org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(1)));
        for (int i = 0; i < dimension; ++i) {
            assertComponentIndexInvariants(mapper, i);
        }
    }

    public static <VECTOR extends Vector> void assertInvariants(final VectorStateSpaceMapper<VECTOR> mapper1,
            final VectorStateSpaceMapper<VECTOR> mapper2) {
        MatrixStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static <VECTOR extends Vector> void fromObject(final VectorStateSpaceMapper<VECTOR> mapper,
            final double[] state, final VECTOR vector) {
        MatrixStateSpaceMapperTest.fromObject(mapper, state, vector);

        assertInvariants(mapper);// check for side-effects
    }

    public static <VECTOR extends Vector> void fromToObjectSymmetry(final VectorStateSpaceMapper<VECTOR> mapper,
            final double[] state, final VECTOR original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final VECTOR reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    public static <VECTOR extends Vector> void fromToVectorSymmetry(final VectorStateSpaceMapper<VECTOR> mapper,
            final double[] state, final Vector original) {
        mapper.fromVector(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final VECTOR reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    public static <VECTOR extends Vector> void fromVector(final VectorStateSpaceMapper<VECTOR> mapper,
            final double[] state, final Vector vector) {
        mapper.fromVector(state, vector);

        assertInvariants(mapper);// check for side effects
    }

    public static <VECTOR extends Vector> VECTOR toObject(final VectorStateSpaceMapper<VECTOR> mapper,
            final ImmutableVectorN state) {
        final VECTOR vector = MatrixStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return vector;
    }
}
