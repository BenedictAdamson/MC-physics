package uk.badamson.mc.physics.kinematics;

import edu.umd.cs.findbugs.annotations.NonNull;
import uk.badamson.mc.physics.TimeVaryingVector3;

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

/**
 * <p>
 * The continuous position of particle as a function of time.
 * </p>
 */
public interface ParticleTrajectory {

    /**
     * <p>
     * The acceleration vector of the particle as a function of time.
     * </p>
     * <ul>
     * <li>Equal to the derivative of the {@linkplain #getVelocity() velocity
     * vector}, and thus the second derivative of the {@linkplain #getPosition()
     * position vector}. However, that equality may be subject to numerical
     * errors.</li>
     * </ul>
     *
     * @return the acceleration; not null.
     */
    @NonNull
    public TimeVaryingVector3 getAcceleration();

    /**
     * <p>
     * The position vector of the particle as a function of time.
     * </p>
     *
     * @return the position; not null.
     */
    @NonNull
    public TimeVaryingVector3 getPosition();

    /**
     * <p>
     * The velocity vector of the particle as a function of time.
     * </p>
     * <ul>
     * <li>Equal to the derivative of the {@linkplain #getPosition() position
     * vector}. However, that equality may be subject to numerical errors.</li>
     * </ul>
     *
     * @return the velocity; not null.
     */
    @NonNull
    public TimeVaryingVector3 getVelocity();
}
