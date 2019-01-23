package uk.badamson.mc.physics.kinematics;
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

import java.time.Duration;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;

/**
 * <p>
 * The continuous position of particle as a function of time, allowing for the
 * possibility of a constant acceleration and damped harmonic variation
 * </p>
 * <p>
 * That also allows for exponentially decaying and growing variation as a
 * special case of zero frequency of the sinusoidal variation.
 * </p>
 */
@Immutable
public final class HarmonicParticleTrajectory implements ParticleTrajectory {

    private final HarmonicVector3 position;
    private final HarmonicVector3 velocity;
    private final HarmonicVector3 acceleration;

    /**
     * <p>
     * Construct a trajectory from a position function.
     * </p>
     * <ul>
     * <li>The {@linkplain #getPosition() position function} of this trajectory is
     * the given position function.</li>
     * </ul>
     *
     * @param position
     *            The position vector of the particle as a function of time.
     * @throws NullPointerException
     *             If {@code position} is null.
     */
    public HarmonicParticleTrajectory(final HarmonicVector3 position) {
        this.position = Objects.requireNonNull(position, "position");
        final Duration t0 = position.getT0();
        final double we = position.getWe();
        final double wh = position.getWh();
        final ImmutableVector3 f22 = position.getF2().scale(2);
        velocity = new HarmonicVector3(t0, position.getF1(), f22, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                ImmutableVector3.ZERO, we, wh);// FIXME
        acceleration = new HarmonicVector3(t0, f22, ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                ImmutableVector3.ZERO, we, wh);// FIXME
    }

    @Override
    @NonNull
    public final HarmonicVector3 getAcceleration() {
        return acceleration;
    }

    @Override
    @NonNull
    public final HarmonicVector3 getPosition() {
        return position;
    }

    @Override
    @NonNull
    public final HarmonicVector3 getVelocity() {
        return velocity;
    }

}
