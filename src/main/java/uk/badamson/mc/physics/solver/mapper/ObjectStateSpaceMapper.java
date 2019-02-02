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

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * A Strategy for mapping from an object representation to (part of) a
 * state-space representation, and vice versa.
 * </p>
 */
@Immutable
public interface ObjectStateSpaceMapper<OBJECT> {

    /**
     * <p>
     * Use this mapping to convert an object to (part of) a state-space vector.
     * </p>
     * <ul>
     * <li>The method sets components of the given state-space vector. Normally, the
     * state-space vector should have been initialised to zero.</li>
     * <li>This is the inverse operation of the {@link #toObject(ImmutableVectorN)}
     * method.</li>
     * </ul>
     *
     * @param state
     *            The components of the state-space vector.
     * @param object
     *            The object to convert to state-space representation.
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code state} is null.</li>
     *             <li>If {@code object} is null.</li>
     *             </ul>
     * @throws IllegalArgumentException
     *             If {@code object} is unsuitable for conversion.
     * @throws RuntimeException
     *             (Typically an {@link IndexOutOfBoundsException} or
     *             {@link IllegalArgumentException}). If the length of the
     *             {@code state} array less than the
     *             {@linkplain #getMinimumStateSpaceDimension() minimum state space
     *             dimension} of this mapper.
     */
    public void fromObject(@NonNull double[] state, @NonNull OBJECT object);

    /**
     * <p>
     * The smallest {@linkplain ImmutableVectorN#getDimension() dimension} of a
     * state-space vector that this mapper can be used with.
     * </p>
     *
     * @return the minimum dimension; positive.
     */
    public int getMinimumStateSpaceDimension();

    /**
     * <p>
     * Use this mapping to convert (part of) a state-space vector to an object.
     * </p>
     *
     * @param state
     *            The state-space vector
     * @return The representation of (part of) the state-space; not null.
     * @throws NullPointerException
     *             If {@code state} is null.
     * @throws RuntimeException
     *             (Typically an {@link IndexOutOfBoundsException} or
     *             {@link IllegalArgumentException}). If the
     *             {@linkplain ImmutableVectorN#getDimension() dimension} of the
     *             {@code state} less than the
     *             {@linkplain #getMinimumStateSpaceDimension() minimum state space
     *             dimension} of this mapper.
     */
    public @NonNull OBJECT toObject(@NonNull ImmutableVectorN state);
}
