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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Quaternion;

/**
 * <p>
 * Unit tests for the {@link QuaternionStateSpaceMapper} class.
 * </p>
 */
public class QuaternionStateSpaceMapperTest {

    public static void assertInvariants(final QuaternionStateSpaceMapper mapper) {
        ObjectTest.assertInvariants(mapper);// inherited
        ObjectStateSpaceMapperTest.assertInvariants(mapper);// inherited

        assertEquals(4, mapper.getSize(), "size");
    }

    public static void assertInvariants(final QuaternionStateSpaceMapper mapper1,
            final QuaternionStateSpaceMapper mapper2) {
        ObjectStateSpaceMapperTest.assertInvariants(mapper1, mapper2);// inherited
    }

    private static void fromObject(final int index0, final int stateSize, final Quaternion q) {
        final double tolerance = q.norm() * (Math.nextAfter(1.0, 2.0) - 1.0);
        final QuaternionStateSpaceMapper mapper = new QuaternionStateSpaceMapper(index0);
        final double[] state = new double[stateSize];

        fromObject(mapper, state, q);

        assertEquals(q.getA(), state[index0], tolerance, "state[index0]");
        assertEquals(q.getB(), state[index0 + 1], tolerance, "state[index0+1]");
        assertEquals(q.getC(), state[index0 + 2], tolerance, "state[index0+2]");
        assertEquals(q.getD(), state[index0 + 3], tolerance, "state[index0+3]");
    }

    public static void fromObject(final QuaternionStateSpaceMapper mapper, final double[] state,
            final Quaternion quaternion) {
        ObjectStateSpaceMapperTest.fromObject(mapper, state, quaternion);

        assertInvariants(mapper);// check for side-effects
    }

    private static void fromToObjectSymmetry(final int index0, final int stateSize, final Quaternion original) {
        final QuaternionStateSpaceMapper mapper = new QuaternionStateSpaceMapper(index0);
        final double[] state = new double[stateSize];
        Arrays.fill(state, Double.NaN);

        fromToObjectSymmetry(mapper, state, original);
    }

    private static void fromToObjectSymmetry(final QuaternionStateSpaceMapper mapper, final double[] state,
            final Quaternion original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final Quaternion reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    public static Quaternion toObject(final QuaternionStateSpaceMapper mapper, final ImmutableVectorN state) {
        final Quaternion vector = ObjectStateSpaceMapperTest.toObject(mapper, state);

        assertInvariants(mapper);// check for side-effects

        return vector;
    }

    @Test
    public void fromObject_2i() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Quaternion.I.scale(2));
    }

    @Test
    public void fromObject_extraAfter() {
        final int index = 0;
        final int stateSize = 6;
        fromObject(index, stateSize, Quaternion.I);
    }

    @Test
    public void fromObject_extraBefore() {
        final int index = 2;
        final int stateSize = 6;
        fromObject(index, stateSize, Quaternion.I);
    }

    @Test
    public void fromObject_i() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Quaternion.I);
    }

    @Test
    public void fromObject_j() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Quaternion.J);
    }

    @Test
    public void fromObject_k() {
        final int index = 0;
        final int stateSize = 4;
        fromObject(index, stateSize, Quaternion.K);
    }

    @Test
    public void fromToObjectSymmetry_2i() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Quaternion.I.scale(2));
    }

    @Test
    public void fromToObjectSymmetry_extraAfter() {
        final int index0 = 0;
        final int stateSize = 6;
        fromToObjectSymmetry(index0, stateSize, Quaternion.I);
    }

    @Test
    public void fromToObjectSymmetry_extraBefore() {
        final int index0 = 2;
        final int stateSize = 6;
        fromToObjectSymmetry(index0, stateSize, Quaternion.I);
    }

    @Test
    public void fromToObjectSymmetry_i() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Quaternion.I);
    }

    @Test
    public void fromToObjectSymmetry_j() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Quaternion.J);
    }

    @Test
    public void fromToObjectSymmetry_k() {
        final int index0 = 0;
        final int stateSize = 4;
        fromToObjectSymmetry(index0, stateSize, Quaternion.K);
    }

}
