package uk.badamson.mc.physics;/* 
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

import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Matrix;

/**
 * <p>
 * Unit tests for classes that implement the {@link MatrixStateSpaceMapper}
 * interface.
 */
public class MatrixStateSpaceMapperTest {

    public static <MATRIX extends Matrix> void assertInvariants(final MatrixStateSpaceMapper<MATRIX> mapper) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper);// inherited
    }

    public static <MATRIX extends Matrix> void assertInvariants(final MatrixStateSpaceMapper<MATRIX> mapper1,
            final MatrixStateSpaceMapper<MATRIX> mapper2) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static <MATRIX extends Matrix> void fromObject(final MatrixStateSpaceMapper<MATRIX> mapper,
            final double[] state, final MATRIX matrix) {
        ObjectStateSpaceMapperTest.fromObject(mapper, state, matrix);

        assertInvariants(mapper);// check for side-effects
    }

    public static <MATRIX extends Matrix> void fromToObjectSymmetry(final MatrixStateSpaceMapper<MATRIX> mapper,
            final double[] state, final MATRIX original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final MATRIX reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    public static <MATRIX extends Matrix> MATRIX toObject(final MatrixStateSpaceMapper<MATRIX> mapper,
            final ImmutableVectorN state) {
        final MATRIX matrix = ObjectStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return matrix;
    }
}
