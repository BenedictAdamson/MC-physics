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

import java.util.function.Function;

import uk.badamson.mc.physics.HarmonicVector3;

/**
 * <p>
 * Functors and functor factory methods for {@linkplain EnergyErrorFunction
 * Energy error function} terms (contributions) that can be used in a
 * {@linkplain HarmonicVector3EnergyErrorFunction energy error function that
 * calculates the physical modelling error for a time varying 3D vector
 * property}.
 * </p>
 * <p>
 * This class provides
 * {@code Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>}
 * functors. The {@link Function#apply(Object)} method of all the functors
 * provided by this class expect a non-null {@link HarmonicVector3} and return a
 * non-null {@link HarmonicVector3EnergyErrorValueAndGradients}.
 * </p>
 */
public final class HarmonicVector3EnergyErrorFunctionTerms {

    /**
     * <p>
     * A term that is always
     * {@linkplain HarmonicVector3EnergyErrorValueAndGradients.ZERO zero}.
     * </p>
     */
    public static final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> ZERO = (
            v) -> HarmonicVector3EnergyErrorValueAndGradients.ZERO;
}
