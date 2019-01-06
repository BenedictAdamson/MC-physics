package uk.badamson.mc.physics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * Unit tests for classes that implement the {@link VectorStateSpaceMapper}
 * interface.
 */
public class VectorStateSpaceMapperTest {

    public static <VECTOR extends Vector> void assertInvariants(final VectorStateSpaceMapper<VECTOR> mapper) {
        MatrixStateSpaceMapperTest.assertInvariants(mapper);// inherited

        assertThat("Number of dimensions", Integer.valueOf(mapper.getDimension()),
                org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(1)));
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
