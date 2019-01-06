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

import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;

/**
 * <p>
 * A Strategy for mapping from an object representation of a {@linkplain Vector
 * vector} to (part of) a state-space representation, and vice versa.
 * </p>
 */
@Immutable
public interface VectorStateSpaceMapper<VECTOR extends Vector> extends MatrixStateSpaceMapper<VECTOR> {

    /**
     * {@inheritDoc}
     *
     * @param state
     *            {@inheritDoc}
     * @param object
     *            {@inheritDoc}
     * @throws NullPointerException
     *             {@inheritDoc}
     * @throws IllegalArgumentException
     *             If {@code object} is unsuitable for conversion.
     * @throws IndexOutOfBoundsException
     *             {@inheritDoc}
     */
    @Override
    public void fromObject(double[] state, VECTOR object);

    /**
     * <p>
     * Using this mapping to convert a vector object to (part of) a state-space
     * vector.
     * </p>
     * <ul>
     * <li>The method <em>adds</em> the components of the given state-space vector.
     * Normally, the state-space vector should have been initialised to zero.</li>
     * <li>This is the inverse operation of the {@link #toObject(ImmutableVectorN)}
     * method.
     * </ul>
     *
     * @param state
     *            The components of the state-space vector.
     * @param vector
     *            The object to convert to state-space representation.
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code state} is null.</li>
     *             <li>If {@code vector} is null.</li>
     *             </ul>
     * @throws IndexOutOfBoundsException
     *             If {@code state} does not have the number of dimensions that this
     *             mapper expects.
     * @throws IllegalArgumentException
     *             If the {@linkplain Vector#getDimension() dimension} of the
     *             {@code vector} does not equal the {@linkplain #getDimension()
     *             dimension} of this mapper.
     */
    public void fromVector(double[] state, Vector vector);

    /**
     * <p>
     * The number of {@linkplain Vector#getDimension() dimensions} of the vectors
     * that this maps.
     * </p>
     *
     * @return the number of dimensions; positive.
     */
    public int getDimension();
}
