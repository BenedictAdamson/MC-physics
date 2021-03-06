package uk.badamson.mc;
/*
 * © Copyright Benedict Adamson 2018.
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * <p>
 * Unit tests for the {@link Object} class.
 * </p>
 */
@SuppressFBWarnings(justification = "Checking contract", value = "EC_NULL_ARG")
public final class ObjectTest {

    public static void assertInvariants(final Object object) {
        assert object != null;
        assertAll("equals", () -> assertEquals(object, object, "An object is always equivalent to itself"),
                () -> assertFalse(object.equals(null), "An object is never equivalent to null"));
    }

    public static void assertInvariants(final Object object1, final Object object2) {
        assert object1 != null;
        assert object2 != null;
        final boolean equals = object1.equals(object2);
        assertAll("equals", () -> assertTrue(equals == object2.equals(object1), "Equality is symmetric"),
                () -> assertFalse(equals && object1.hashCode() != object2.hashCode(),
                        "hashCode() is consistent with equals()"));
    }
}
