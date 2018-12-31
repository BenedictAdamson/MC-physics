package uk.badamson.mc.physics;
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

import java.time.Duration;
import java.util.Objects;

import net.jcip.annotations.Immutable;

/**
 * <p>
 * A {@linkplain TimeVaryingScalar functor for a notionally time varying scalar
 * property} that in fact does not vary with time.
 * </p>
 *
 * @see ConstantVector3
 */
@Immutable
public final class ConstantScalar extends AbstractTimeVaryingScalar {

    private final double value;

    /**
     * <p>
     * Construct a notionally time varying scalar property that in fact has a given
     * value at all points in time.
     * </p>
     * <ul>
     * <li>The {@linkplain #getValue() value} of this scalar is the given
     * value.</li>
     * </ul>
     *
     * @param value
     *            The value of the scalar at all points in time.
     */
    public ConstantScalar(final double value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>The value at all points in time is the {@linkplain #getValue() constant
     * value} of this function.</li>
     * </ul>
     *
     * @param t
     *            {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException
     *             {@inheritDoc}
     */
    @Override
    public final double at(final Duration t) {
        Objects.requireNonNull(t, "t");
        return value;
    }

    /**
     * <p>
     * The value of the scalar at all points in time.
     * </p>
     *
     * @return the value
     */
    public final double getValue() {
        return value;
    }

}
