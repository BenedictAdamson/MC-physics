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
import uk.badamson.mc.math.Matrix;

/**
 * <p>
 * A Strategy for mapping from an object representation of a {@linkplain Matrix
 * matrix} to (part of) a state-space representation, and vice versa.
 * </p>
 */
@Immutable
public interface MatrixStateSpaceMapper<MATRIX extends Matrix> extends ObjectStateSpaceMapper<MATRIX> {

    /**
     * <p>
     * The component of the state-space vector that corresponds to an element of the
     * {@linkplain Matrix matrix} that this maps.
     * </p>
     * <ul>
     * <li>The component of the state-pace vector is non negative.</li>
     * </ul>
     *
     * @param i
     *            the cardinal number of the row of the element (0 for the first
     *            row, 1 for the second row, and so on).
     * @param j
     *            the cardinal number of the column of the element (0 for the first
     *            column, 1 for the second column, and so on).
     * @return the component of the state-space vector.
     *
     * @throws IndexOutOfBoundsException
     *             <ul>
     *             <li>If {@code i} is negative.</li>
     *             <li>If {@code i} is greater than or equal to the number of
     *             {@linkplain Matrix#getRows() rows} of the matrix.</li>
     *             <li>If {@code j} is negative.</li>
     *             <li>If {@code j} is greater than or equal to the number of
     *             {@linkplain Matrix#getColumns() columns} of the matrix.</li>
     *             </ul>
     */
    public int getComponentIndex(int i, int j);
}
