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

import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * A contributor to the {@linkplain TimeStepEnergyErrorFunction physical
 * modelling error of a system at a future point in time}.
 * </p>
 * <p>
 * The term calculates an error value that has dimensions of energy.
 * </p>
 */
@Immutable
public interface TimeStepEnergyErrorFunctionTerm {

    /**
     * <p>
     * Calculate the value of this term.
     * </p>
     * <p>
     * The method returns the value for this error term, and adds the components of
     * the gradient of the error value to the given array of components.
     * </p>
     *
     * @param dedx
     *            An array for accumulating the components of the gradient of the
     *            error value.
     * @param state0
     *            The state vector of the physical system at the current point in
     *            time.
     * @param state
     *            The state vector of the physical system at the future point in
     *            time.
     * @param dt
     *            The size of the time-step; the difference between the future point
     *            in time and the current point in time.
     * @return the value, which has dimensions of energy.
     *
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code dedx} is null.</li>
     *             <li>If {@code state0} is null.</li>
     *             <li>If {@code state} is null.</li>
     *             </ul>
     * @throws IllegalArgumentException
     *             <ul>
     *             <li>If {@code dt} is not positive and
     *             {@linkplain Double#isInfinite() finite}.</li>
     *             <li>If {@code x0} and {@code state} have different
     *             {@linkplain ImmutableVectorN dimensions}.</li>
     *             <li>If this is not {@linkplain #isValidForDimension(int) valid}
     *             for the dimension of {@code x0}.</li>
     *             </ul>
     * @throws RuntimeException
     *             If the length of {@code dedx} does not equal the
     *             {@linkplain ImmutableVectorN#getDimension() dimension} of
     *             {@code x0}. For a typical implementation this would be an
     *             {@link IndexOutOfBoundsException}, but it could be an
     *             {@link IllegalArgumentException}.
     */
    public double evaluate(double[] dedx, ImmutableVectorN state0, ImmutableVectorN state, double dt);

    /**
     * <p>
     * Whether this term can be calculated for a state vector that has a given
     * number of variables.
     * </p>
     *
     * @return whether valid.
     * @throws IllegalArgumentException
     *             If {@code n} is not positive.
     */
    public boolean isValidForDimension(int n);

}