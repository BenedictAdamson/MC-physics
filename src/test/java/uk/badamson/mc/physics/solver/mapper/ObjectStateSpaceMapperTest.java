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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests for classes that implement the {@link ObjectStateSpaceMapper}
 * interface.
 */
public class ObjectStateSpaceMapperTest {

    public static <OBJECT> void assertInvariants(final ObjectStateSpaceMapper<OBJECT> mapper) {
        // Do nothing
    }

    public static <OBJECT> void assertInvariants(final ObjectStateSpaceMapper<OBJECT> mapper1,
            final ObjectStateSpaceMapper<OBJECT> mapper2) {
        // Do nothing
    }

    public static <OBJECT> void fromObject(final ObjectStateSpaceMapper<OBJECT> mapper, final double[] state,
            final OBJECT object) {
        mapper.fromObject(state, object);

        assertInvariants(mapper);// check for side-effects
        ObjectTest.assertInvariants(object);// check for side-effects
    }

    public static <OBJECT> void fromToObjectSymmetry(final ObjectStateSpaceMapper<OBJECT> mapper, final double[] state,
            final OBJECT original) {
        mapper.fromObject(state, original);
        final ImmutableVectorN stateVector = ImmutableVectorN.create(state);

        final OBJECT reconstructed = toObject(mapper, stateVector);

        assertEquals(original, reconstructed, "Symmetric");
    }

    public static <OBJECT> OBJECT toObject(final ObjectStateSpaceMapper<OBJECT> mapper, final ImmutableVectorN state) {
        final OBJECT object = mapper.toObject(state);

        assertInvariants(mapper);// check for side-effects
        assertNotNull(object, "result");
        ObjectTest.assertInvariants(object);

        return object;
    }
}
