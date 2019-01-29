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

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.Function1WithGradientValue;
import uk.badamson.mc.math.FunctionNWithGradient;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.MinN;

/**
 * <p>
 * A {@linkplain FunctionNWithGradient functor} that calculates the physical
 * modelling error of a system.
 * </p>
 * <p>
 * The function can be {@linkplain MinN minimised} to calculate the state of the
 * physical system. By solving for a {@linkplain ImmutableVectorN
 * multi-dimensional state vector}, that simultaneously solves for all the
 * physical parameters of the system. By using minimisation to calculate the
 * state, it is straightforward to include non-linear coupling of parameters of
 * the physical system.
 * </p>
 * <p>
 * The function also calculates the
 * {@linkplain FunctionNWithGradientValue#getDfDx() rate of change} of the error
 * with respect to changes in the state vector. That enables the minimisation to
 * be performed using a
 * {@linkplain MinN#findFletcherReevesPolakRibere(FunctionNWithGradient, ImmutableVectorN, double)
 * conjugate-gradient method}.
 * </p>
 * <p>
 * The function calculates an error value that has dimensions of energy. It is
 * therefore straightforward for the function to ensure conservation of energy.
 * Other conservation laws can be incorporated by using appropriate dimension
 * scales to convert other errors to energy errors.
 * </p>
 */
@Immutable
public interface EnergyErrorFunction extends FunctionNWithGradient {

    /**
     * <p>
     * The number of independent variables of this function; the number of variables
     * of the physical model.
     * </p>
     * <p>
     * This attribute must be <dfn>constant</dfn>: the value for a given object must
     * always be the same value.
     * </p>
     *
     * @return the number of dimensions; positive.
     */
    @Override
    public int getDimension();

    /**
     * <p>
     * Calculate the physical modelling error of the system.
     * </p>
     * <ul>
     * <li>Always returns a (non null) value.</li>
     * <li>The {@linkplain Function1WithGradientValue#getX() domain value} of the
     * returned object is the given state vector.</li>
     * </ul>
     *
     * @param state
     *            The state of the physical system, expressed as a state vector.
     * @return The error.
     * @throws NullPointerException
     *             If {@code state} is null.
     * @throws IllegalArgumentException
     *             If the {@linkplain ImmutableVectorN#getDimension() dimension} of
     *             {@code state} does not equal the {@linkplain #getDimension()
     *             dimension} of this functor.
     */
    @Override
    public @NonNull FunctionNWithGradientValue value(@NonNull ImmutableVectorN state);
}
