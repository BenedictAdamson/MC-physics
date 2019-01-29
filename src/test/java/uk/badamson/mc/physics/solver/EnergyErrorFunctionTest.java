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

import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Auxiliary tests for classes that implement the interface
 * {@link EnergyErrorFunction}.
 * </p>
 */
public class EnergyErrorFunctionTest {

    public static void assertInvariants(final EnergyErrorFunction f) {
        // Do nothing
    }

    public static void assertInvariants(final EnergyErrorFunction f1, final EnergyErrorFunction f2) {
        // Do nothing
    }

    public static FunctionNWithGradientValue value(final EnergyErrorFunction f, final ImmutableVectorN x) {
        final FunctionNWithGradientValue fx = f.value(x);

        assertInvariants(f);

        return fx;
    }
}
