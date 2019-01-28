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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Rotation3;
import uk.badamson.mc.math.Rotation3Quaternion;

/**
 * <p>
 * Unit tests for the {@link Rotation3QuaternionStateSpaceMapper} class.
 * </p>
 */
public class Rotation3QuaternionStateSpaceMapperTest {

    private static class IsCloseTo extends TypeSafeMatcher<Rotation3> {
        private final double tolerance;
        private final Rotation3 value;

        private IsCloseTo(final Rotation3 value, final double tolerance) {
            this.tolerance = tolerance;
            this.value = value;
        }

        @Override
        public void describeMismatchSafely(final Rotation3 item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" differed by ")
                    .appendValue(Double.valueOf(distance(item)));
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a rotation within ").appendValue(Double.valueOf(tolerance)).appendText(" of ")
                    .appendValue(value);
        }

        private final double distance(final Rotation3 item) {
            return value.getVersor().distance(item.getVersor());
        }

        @Override
        public boolean matchesSafely(final Rotation3 item) {
            return distance(item) <= tolerance;
        }
    }// class

    private static final double SMALL_ANGLE = Math.PI * 0.003;

    private static final double TOLERANCE = 4.0 * (Math.nextUp(1.0) - 1.0);

    public static void assertInvariants(final Rotation3QuaternionStateSpaceMapper mapper) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper);// inherited
    }

    public static void assertInvariants(final Rotation3QuaternionStateSpaceMapper mapper1,
            final Rotation3QuaternionStateSpaceMapper mapper2) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    public static Matcher<Rotation3> closeToRotation3(final Rotation3 operand) {
        return new IsCloseTo(operand, TOLERANCE);
    }

    public static Matcher<Rotation3> closeToRotation3(final Rotation3 operand, final double tolerance) {
        return new IsCloseTo(operand, tolerance);
    }

    private static void fromObject(final int index0, final int stateSize, final Rotation3Quaternion r) {
        final double tolerance = 2 * (Math.nextAfter(1.0, 2.0) - 1.0);
        final QuaternionStateSpaceMapper quaternionMapper = new QuaternionStateSpaceMapper(index0);
        final Rotation3QuaternionStateSpaceMapper rotationMapper = new Rotation3QuaternionStateSpaceMapper(
                quaternionMapper);
        final double[] state = new double[stateSize];

        fromObject(rotationMapper, state, r);

        assertEquals(r.getVersor().getA(), state[index0], tolerance, "state[index0]");
        assertEquals(r.getVersor().getB(), state[index0 + 1], tolerance, "state[index0+1]");
        assertEquals(r.getVersor().getC(), state[index0 + 2], tolerance, "state[index0+2]");
        assertEquals(r.getVersor().getD(), state[index0 + 3], tolerance, "state[index0+3]");
    }

    public static void fromObject(final Rotation3QuaternionStateSpaceMapper mapper, final double[] state,
            final Rotation3Quaternion quaternion) {
        ObjectStateSpaceMapperTest.fromObject(mapper, state, quaternion);

        assertInvariants(mapper);// check for side-effects
    }

    private static void fromToObjectSymmetry(final int index0, final int stateSize,
            final Rotation3Quaternion original) {
        final QuaternionStateSpaceMapper quaternionMapper = new QuaternionStateSpaceMapper(index0);
        final Rotation3QuaternionStateSpaceMapper rotationMapper = new Rotation3QuaternionStateSpaceMapper(
                quaternionMapper);
        final double[] state = new double[stateSize];
        Arrays.fill(state, Double.NaN);

        fromToObjectSymmetry(rotationMapper, state, original);
    }

    private static void fromToObjectSymmetry(final Rotation3QuaternionStateSpaceMapper mapper, final double[] state,
            final Rotation3Quaternion original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final Rotation3Quaternion reconstructed = toObject(mapper, stateVector);

        assertThat("symmetric", reconstructed, closeToRotation3(original));
    }

    public static Rotation3Quaternion toObject(final Rotation3QuaternionStateSpaceMapper mapper,
            final ImmutableVectorN state) {
        final Rotation3Quaternion vector = ObjectStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return vector;
    }

    @Test
    public void fromObject_0() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Rotation3Quaternion.ZERO);
    }

    @Test
    public void fromObject_extraAfter() {
        final int index = 0;
        final int stateSize = 6;
        fromObject(index, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void fromObject_extraBefore() {
        final int index = 2;
        final int stateSize = 6;
        fromObject(index, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void fromObject_iA() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void fromObject_iB() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, Math.PI * 0.5));
    }

    @Test
    public void fromObject_j() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void fromObject_k() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void fromToObjectSymmetry_extraAfter() {
        final int index0 = 0;
        final int stateSize = 6;
        fromToObjectSymmetry(index0, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void fromToObjectSymmetry_extraBefore() {
        final int index0 = 2;
        final int stateSize = 6;
        fromToObjectSymmetry(index0, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void fromToObjectSymmetry_ia() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void fromToObjectSymmetry_ib() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize,
                Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.I, Math.PI * 0.5));
    }

    @Test
    public void fromToObjectSymmetry_j() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void fromToObjectSymmetry_k() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Rotation3Quaternion.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

}
