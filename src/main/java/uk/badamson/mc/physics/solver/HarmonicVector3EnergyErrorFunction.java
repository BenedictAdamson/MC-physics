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

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.FunctionNWithGradient;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.solver.mapper.HarmonicVector3Mapper;

/**
 * <p>
 * A {@linkplain FunctionNWithGradient functor} that calculates the physical
 * modelling error,which has dimensions of energy, of a
 * {@linkplain HarmonicVector3 functor for a time varying 3D vector property
 * that can have damped harmonic variation}.
 * </p>
 * <p>
 * This also acts as a Composite of a collection of function terms.
 * </p>
 */
@Immutable
public final class HarmonicVector3EnergyErrorFunction
        implements EnergyErrorFunction, Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> {

    private final HarmonicVector3Mapper mapper;
    private final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>[] terms;

    /**
     * <p>
     * Construct an object with given associations.
     * </p>
     *
     * @param mapper
     *            The Strategy for mapping from the {@linkplain HarmonicVector3
     *            object representation} of the time varying 3D vector property to
     *            (part of) a state-space representation, and vice versa.
     * @param terms
     *            The contributors to this function
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code mapper} is null.</li>
     *             <li>If {@code terms} is null.</li>
     *             <li>If {@code terms} contains a null.</li>
     *             </ul>
     */
    public HarmonicVector3EnergyErrorFunction(final HarmonicVector3Mapper mapper,
            final Collection<Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>> terms) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        @SuppressWarnings("unchecked")
        final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>[] termsArray = new Function[0];
        this.terms = Objects.requireNonNull(terms, "terms").toArray(termsArray);
        // Check after copy to avoid race hazards
        for (final var term : this.terms) {
            Objects.requireNonNull(term, "term");
        }
    }

    /**
     * <p>
     * The total {@linkplain HarmonicVector3EnergyErrorValueAndGradients error} of
     * the {@linkplain #getTerms() terms}, for a given functor.
     * </p>
     * <p>
     * This method provides the class with the functionality of a Composite.
     * </p>
     *
     * @param v
     *            The functor for which to compute the total error.
     *
     * @return The error value and its gradients; not null.
     * @throws NullPointerException
     *             If {@code v} is null.
     */
    @Override
    public HarmonicVector3EnergyErrorValueAndGradients apply(final HarmonicVector3 v) {
        Objects.requireNonNull(v, "v");
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * <p>
     * The associated Strategy for mapping from the {@linkplain HarmonicVector3
     * object representation} of the time varying 3D vector property to (part of) a
     * state-space representation, and vice versa.
     * </p>
     *
     * @return the mapper; not null
     */
    public final HarmonicVector3Mapper getMapper() {
        return mapper;
    }

    /**
     * <p>
     * The assocaited contributors to this function
     * </p>
     * <ul>
     * <li>Always have a (non null) collection of terms.</li>
     * <li>The collection of terms does not contain a null term.</li>
     * <li>The collection of terms is not modifiable.</li>
     * <li>The collection of terms may be a newly created object.</li>
     * </ul>
     */
    public final Collection<Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients>> getTerms() {
        return java.util.Collections.unmodifiableList(Arrays.asList(terms));
    }

    /**
     * {@inheritDoc}
     *
     * @param state
     *            {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public FunctionNWithGradientValue value(final ImmutableVectorN state) {
        // TODO Auto-generated method stub
        return null;
    }

}
