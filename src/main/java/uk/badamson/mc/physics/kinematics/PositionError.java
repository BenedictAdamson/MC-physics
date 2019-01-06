package uk.badamson.mc.physics.kinematics;/*
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

import java.util.Objects;

import net.jcip.annotations.Immutable;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;
import uk.badamson.mc.physics.solver.AbstractTimeStepEnergyErrorFunctionTerm;
import uk.badamson.mc.physics.solver.TimeStepEnergyErrorFunction;
import uk.badamson.mc.physics.solver.TimeStepEnergyErrorFunctionTerm;
import uk.badamson.mc.physics.solver.mapper.VectorStateSpaceMapper;

/**
 * <p>
 * A {@linkplain TimeStepEnergyErrorFunctionTerm term} for a
 * {@linkplain TimeStepEnergyErrorFunction functor that calculates the physical
 * modelling error of a system at a future point in time} that gives the degree
 * of inconsistency of the position and velocity of a body.
 * </p>
 */
@Immutable
public final class PositionError<VECTOR extends Vector> extends AbstractTimeStepEnergyErrorFunctionTerm {

    private final double mass;
    private final VectorStateSpaceMapper<VECTOR> positionVectorMapper;
    private final VectorStateSpaceMapper<VECTOR> velocityVectorMapper;

    /**
     * <p>
     * Construct a position error term.
     * </p>
     * <ul>
     * <li>The constructed object has attribute values equal to the given
     * values.</li>
     * </ul>
     *
     * @param mass
     *            A reference mass scale.
     * @param positionVectorMapper
     *            The Strategy for mapping from an object representation of the
     *            position {@linkplain Vector vector} to (part of) a state-space
     *            representation, and vice versa.
     * @param velocityVectorMapper
     *            The Strategy for mapping from an object representation of the
     *            velocity {@linkplain Vector vector} to (part of) a state-space
     *            representation, and vice versa.
     * @throws NullPointerException
     *             <ul>
     *             <li>If {@code positionVectorMapper} is null.</li>
     *             <li>If {@code velocityVectorMapper} is null.</li>
     *             </ul>
     * @throws IllegalArgumentException
     *             <ul>
     *             <li>If {@code mass} is not a positive and
     *             {@linkplain Double#isFinite(double) finite}.</li>
     *             <li>
     *             <li>If the{@linkplain VectorStateSpaceMapper#getDimension()
     *             number of dimensions} of {@code positionVectorMapper} is not
     *             equal to the number of dimensions of the
     *             {@code velocityVectorMapper}.</li>
     *             </ul>
     */
    public PositionError(final double mass, final VectorStateSpaceMapper<VECTOR> positionVectorMapper,
            final VectorStateSpaceMapper<VECTOR> velocityVectorMapper) {
        this.mass = requireReferenceScale(mass, "mass");
        this.positionVectorMapper = Objects.requireNonNull(positionVectorMapper, "positionVectorMapper");
        this.velocityVectorMapper = Objects.requireNonNull(velocityVectorMapper, "velocityVectorMapper");
        if (positionVectorMapper.getDimension() != velocityVectorMapper.getDimension()) {
            throw new IllegalArgumentException(
                    "Inconsistent mapper dimensions. positionVectorMapper " + positionVectorMapper.getDimension()
                            + " velocityVectorMapper " + velocityVectorMapper.getDimension());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <ol>
     * <li>The method uses the {@linkplain #getPositionVectorMapper() position} and
     * {@linkplain #getVelocityVectorMapper() velocity} mappers to extract position
     * and velocity vectors from the given state vectors.</li>
     * <li>It calculates a mean acceleration from the old and new velocity values,
     * and the time-step size.</li>
     * <li>It calculates the extrapolated position from the old position, the old
     * velocity, and the mean acceleration.</li>
     * <li>It calculates a position error by comparing the new position with the
     * extrapolated position.</li>
     * <li>From that it calculates an equivalent velocity error, by dividing the
     * position error by the time-step size.</li>
     * <li>From that it calculates an equivalent kinetic energy error, using the
     * {@linkplain #getMass() characteristic mass value}. That is the error term it
     * returns.</li>
     * </ol>
     *
     * @param dedState
     *            {@inheritDoc}
     * @param state0
     *            {@inheritDoc}
     * @param state
     *            {@inheritDoc}
     * @param dt
     *            {@inheritDoc}
     * @return the value
     *
     * @throws NullPointerException
     *             {@inheritDoc}
     * @throws IllegalArgumentException
     *             {@inheritDoc}
     * @throws IllegalArgumentException
     *             If the length of {@code dedx} does not equal the
     *             {@linkplain ImmutableVectorN#getDimension() dimension} of
     *             {@code state0}.
     */
    @Override
    public final double evaluate(final double[] dedState, final ImmutableVectorN state0, final ImmutableVectorN state,
            final double dt) {
        super.evaluate(dedState, state0, state, dt);

        final double rate = 1.0 / dt;

        final Vector x0 = positionVectorMapper.toObject(state0);
        final Vector v0 = velocityVectorMapper.toObject(state0);
        final Vector x = positionVectorMapper.toObject(state);
        final Vector v = velocityVectorMapper.toObject(state);

        final Vector dx = x.minus(x0);
        final Vector vMean = v.mean(v0);
        final Vector ve = dx.scale(rate).minus(vMean);

        final double e = 0.5 * mass * ve.magnitude2();
        final Vector dedx = ve.scale(mass * rate);
        final Vector dedv = ve.scale(-0.5 * mass);

        positionVectorMapper.fromVector(dedState, dedx);
        velocityVectorMapper.fromVector(dedState, dedv);

        return e;
    }

    /**
     * <p>
     * A reference mass scale.
     * </p>
     * <p>
     * The functor uses this value to convert a position error into an energy error.
     * It is tempting to use the mass of the solid body for which this functor
     * calculates the position error, but that will produce bad results if there are
     * multiple bodies and they have very different masses; it is better to use the
     * same value for all bodies, with that value equal to the mass of a typical
     * body.
     * </p>
     *
     * @return the mass; positive and {@linkplain Double#isFinite(double) finite}
     */
    public final double getMass() {
        return mass;
    }

    /**
     * <p>
     * The Strategy for mapping from an object representation of the position
     * {@linkplain Vector vector} to (part of) a state-space representation, and
     * vice versa.
     * </p>
     *
     * @return the strategy; not null
     */
    public final VectorStateSpaceMapper<VECTOR> getPositionVectorMapper() {
        return positionVectorMapper;
    }

    /**
     * <p>
     * The number of space dimensions for which this calculates a position error.
     * </p>
     *
     * @return the number of dimensions; equal to the
     *         {@linkplain VectorStateSpaceMapper#getDimension() number of
     *         dimensions} of the {@linkplain #getPositionVectorMapper() position
     *         vector mapper}.
     */
    public final int getSpaceDimension() {
        return positionVectorMapper.getDimension();
    }

    /**
     * <p>
     * The Strategy for mapping from an object representation of the velocity
     * {@linkplain Vector vector} to (part of) a state-space representation, and
     * vice versa.
     * </p>
     * <ul>
     * <li>Always have a (non null) velocity vector mapper.</li>
     * <li>The {@linkplain VectorStateSpaceMapper#getDimension() number of
     * dimensions} of the velocity vector mapper is equal to the number of
     * dimensions of the {@linkplain #getPositionVectorMapper() position vector
     * mapper}.</li>
     * </ul>
     *
     * @return the strategy; not null
     */
    public final VectorStateSpaceMapper<VECTOR> getVelocityVectorMapper() {
        return velocityVectorMapper;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>This is valid if, and only if, the {@linkplain #getPositionVectorMapper()
     * position vector mapper} is valid for the given number of variables and the
     * {@linkplain #getVelocityVectorMapper() velocity vector mapper} is valid for
     * the given number of variables.</li>
     * </ul>
     *
     * @return {@inheritDoc}
     * @throws IllegalArgumentException
     *             {@inheritDoc}
     */
    @Override
    public boolean isValidForDimension(final int n) {
        return positionVectorMapper.isValidForDimension(n) && velocityVectorMapper.isValidForDimension(n);
    }

}
