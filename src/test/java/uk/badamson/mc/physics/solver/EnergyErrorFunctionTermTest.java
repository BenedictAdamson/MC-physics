package uk.badamson.mc.physics.solver;
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

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests classes that implement the interface
 * {@link EnergyErrorFunctionTerm}.
 * </p>
 */
public class EnergyErrorFunctionTermTest {

    public static void assertInvariants(final EnergyErrorFunctionTerm t) {
        ObjectTest.assertInvariants(t);// inherited
    }

    public static void assertInvariants(final EnergyErrorFunctionTerm t1, final EnergyErrorFunctionTerm t2) {
        ObjectTest.assertInvariants(t1, t2);// inherited
    }

    public static double evaluate(final EnergyErrorFunctionTerm term, final double[] dedx, final ImmutableVectorN x) {
        final double e = term.evaluate(dedx, x);

        assertInvariants(term);

        return e;
    }
}