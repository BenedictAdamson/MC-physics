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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;

/**
 * <p>
 * Unit tests and auxiliary test code for the class
 * {@link HarmonicParticleTrajectory}.
 * </p>
 */
public class HarmonicParticleTrajectoryTest {

    @Nested
    public class Constructor {

        @Nested
        public class ConstantVelocity {

            @Test
            public void a() {
                test(T_1, V_1, V_2, 13, 17);
            }

            @Test
            public void b() {
                test(T_2, V_2, V_3, 17, 19);
            }

            private HarmonicParticleTrajectory test(final Duration t0, final ImmutableVector3 f0,
                    final ImmutableVector3 f1, final double we, final double wh) {
                final HarmonicVector3 position = new HarmonicVector3(t0, f0, f1, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);
                final HarmonicVector3 velocity = new HarmonicVector3(t0, f1, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                final HarmonicParticleTrajectory trajectory = Constructor.this.test(position);

                assertEquals(velocity, trajectory.getVelocity(), "velocity");

                return trajectory;
            }
        }// class

        @Nested
        public class Stationary {

            @Test
            public void a() {
                final double we = 0.0;
                final double wh = 0.0;
                test(T_1, V_1, we, wh);
            }

            @Test
            public void b() {
                final double we = 5.0;
                final double wh = 7.0;
                test(T_2, V_2, we, wh);
            }

            private HarmonicParticleTrajectory test(final Duration t0, final ImmutableVector3 x, final double we,
                    final double wh) {
                final HarmonicVector3 position = new HarmonicVector3(t0, x, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);
                final HarmonicVector3 zero = new HarmonicVector3(t0, ImmutableVector3.ZERO, ImmutableVector3.ZERO,
                        ImmutableVector3.ZERO, ImmutableVector3.ZERO, ImmutableVector3.ZERO, we, wh);

                final HarmonicParticleTrajectory trajectory = Constructor.this.test(position);

                assertEquals(zero, trajectory.getVelocity(), "velocity");

                return trajectory;
            }
        }// class

        private HarmonicParticleTrajectory test(final HarmonicVector3 position) {
            final HarmonicParticleTrajectory trajectory = new HarmonicParticleTrajectory(position);

            assertInvariants(trajectory);
            assertSame(position, trajectory.getPosition(),
                    "The position function of this trajectory is the given position function.");
            return trajectory;
        }
    }// class

    private static final Duration T_1 = Duration.ofSeconds(1);
    private static final Duration T_2 = Duration.ofSeconds(1);
    private static final ImmutableVector3 V_1 = ImmutableVector3.I;
    private static final ImmutableVector3 V_2 = ImmutableVector3.J.scale(2);
    private static final ImmutableVector3 V_3 = ImmutableVector3.K.scale(3);

    public static void assertInvariants(final HarmonicParticleTrajectory trajectory) {
        ObjectTest.assertInvariants(trajectory);// inherited
        ParticleTrajectoryTest.assertInvariants(trajectory);// inherited
    }

    public static void assertInvariants(final HarmonicParticleTrajectory trajectory1,
            final HarmonicParticleTrajectory trajectory2) {
        ObjectTest.assertInvariants(trajectory1, trajectory2);// inherited
        ParticleTrajectoryTest.assertInvariants(trajectory1, trajectory2);// inherited
    }
}
