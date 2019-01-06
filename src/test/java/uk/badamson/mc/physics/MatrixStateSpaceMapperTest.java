package uk.badamson.mc.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Matrix;

/**
 * <p>
 * Unit tests for classes that implement the {@link MatrixStateSpaceMapper}
 * interface.
 */
public class MatrixStateSpaceMapperTest {

    public static <MATRIX extends Matrix> void assertInvariants(MatrixStateSpaceMapper<MATRIX> mapper) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper);// inherited
    }

    public static <MATRIX extends Matrix> void assertInvariants(MatrixStateSpaceMapper<MATRIX> mapper1,
            MatrixStateSpaceMapper<MATRIX> mapper2) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static <MATRIX extends Matrix> void fromObject(MatrixStateSpaceMapper<MATRIX> mapper, double[] state,
            MATRIX matrix) {
        ObjectStateSpaceMapperTest.fromObject(mapper, state, matrix);

        assertInvariants(mapper);// check for side-effects
    }

    public static <MATRIX extends Matrix> void fromToObjectSymmetry(MatrixStateSpaceMapper<MATRIX> mapper,
            double[] state, MATRIX original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final MATRIX reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    public static <MATRIX extends Matrix> MATRIX toObject(MatrixStateSpaceMapper<MATRIX> mapper,
            ImmutableVectorN state) {
        MATRIX matrix = ObjectStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return matrix;
    }
}
