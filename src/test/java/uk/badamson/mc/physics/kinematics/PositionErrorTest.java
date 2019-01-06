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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVector1;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Vector;
import uk.badamson.mc.physics.AbstractTimeStepEnergyErrorFunctionTermTest;
import uk.badamson.mc.physics.solver.mapper.ImmutableVector1StateSpaceMapper;
import uk.badamson.mc.physics.solver.mapper.ImmutableVector3StateSpaceMapper;
import uk.badamson.mc.physics.solver.mapper.VectorStateSpaceMapper;

/**
 * <p>
 * Unit tests for the class {@link PositionError}.
 * </p>
 */
public class PositionErrorTest {

    private static final double MASS_1 = 2.0;

    private static final double MASS_2 = 1E24;

    public static <VECTOR extends Vector> void assertInvariants(final PositionError<VECTOR> term) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term);// inherited

        final double mass = term.getMass();
        final int spaceDimension = term.getSpaceDimension();
        final VectorStateSpaceMapper<VECTOR> positionVectorMapper = term.getPositionVectorMapper();
        final VectorStateSpaceMapper<VECTOR> velocityVectorMapper = term.getVelocityVectorMapper();

        assertNotNull(positionVectorMapper, "positionVectorMapper");// guard
        assertNotNull(velocityVectorMapper, "velocityVectorMapper");// guard

        final int positionVectorMapperDimension = positionVectorMapper.getDimension();

        AbstractTimeStepEnergyErrorFunctionTermTest.assertIsReferenceScale("mass", mass);
        assertThat("spaceDimension", Integer.valueOf(spaceDimension),
                org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(1)));
        assertEquals(positionVectorMapperDimension, spaceDimension,
                "The number of dimensions equals the number of dimensions of the position vector mapper.");
        assertEquals(positionVectorMapperDimension, velocityVectorMapper.getDimension(),
                "The dimension of the velocity vector mapper is equal to the dimension of the position vector mapper.");
    }

    public static <VECTOR extends Vector> void assertInvariants(final PositionError<VECTOR> term1,
            final PositionError<VECTOR> term2) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term1, term2);// inherited
    }

    private static <VECTOR extends Vector> PositionError<VECTOR> constructor(final double mass,
            final VectorStateSpaceMapper<VECTOR> positionVectorMapper,
            final VectorStateSpaceMapper<VECTOR> velocityVectorMapper) {
        final PositionError<VECTOR> term = new PositionError<>(mass, positionVectorMapper, velocityVectorMapper);

        assertInvariants(term);

        assertEquals(mass, term.getMass(), Double.MIN_NORMAL, "mass");
        assertSame(positionVectorMapper, term.getPositionVectorMapper(), "positionVectorMapper");
        assertSame(velocityVectorMapper, term.getVelocityVectorMapper(), "velocityVectorMapper");

        return term;
    }

    private static <VECTOR extends Vector> double evaluate(final PositionError<VECTOR> term, final double[] dedx,
            final ImmutableVectorN x0, final ImmutableVectorN x, final double dt) {
        final double e = AbstractTimeStepEnergyErrorFunctionTermTest.evaluate(term, dedx, x0, x, dt);

        assertInvariants(term);

        return e;
    }

    private static final void evaluate_1(final double mass, final int positionTerm, final int velocityTerm,
            final double dedx0, final double dedv0, final double x0, final double v0, final double x, final double v,
            final double dt, final double eExpected, final double dEDXExpected, final double dEDVExpected,
            final double tolerance) {
        final ImmutableVector1StateSpaceMapper positionVectorMapper = new ImmutableVector1StateSpaceMapper(
                positionTerm);
        final ImmutableVector1StateSpaceMapper velocityVectorMapper = new ImmutableVector1StateSpaceMapper(
                velocityTerm);
        final PositionError<ImmutableVector1> term = new PositionError<>(mass, positionVectorMapper,
                velocityVectorMapper);
        final double[] dedx = { dedx0, dedv0 };

        final double e = evaluate(term, dedx, ImmutableVectorN.create(x0, v0), ImmutableVectorN.create(x, v), dt);

        assertEquals(eExpected, e, tolerance, "energy");
        assertEquals(dEDXExpected, dedx[positionTerm], tolerance, "dedx[positionTerm]");
        assertEquals(dEDVExpected, dedx[velocityTerm], tolerance, "dedx[velocityTerm]");
    }

    private static void evaluate_1Minimum(final double mass, final int positionTerm, final int velocityTerm,
            final double dedx0, final double dedv0, final double x0, final double v0, final double x, final double v,
            final double dt, final double tolerance) {
        final double eExpected = 0.0;
        final double dEDXExpected = dedx0;
        final double dEDVExpected = dedv0;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }

    @Test
    public void constructor_1A() {
        final ImmutableVector1StateSpaceMapper positionVectorMapper = new ImmutableVector1StateSpaceMapper(3);
        final ImmutableVector1StateSpaceMapper velocityVectorMapper = new ImmutableVector1StateSpaceMapper(4);

        constructor(MASS_1, positionVectorMapper, velocityVectorMapper);
    }

    @Test
    public void constructor_1B() {
        final ImmutableVector1StateSpaceMapper positionVectorMapper = new ImmutableVector1StateSpaceMapper(7);
        final ImmutableVector1StateSpaceMapper velocityVectorMapper = new ImmutableVector1StateSpaceMapper(11);

        constructor(MASS_2, positionVectorMapper, velocityVectorMapper);
    }

    @Test
    public void constructor_3() {
        final ImmutableVector3StateSpaceMapper positionVectorMapper = new ImmutableVector3StateSpaceMapper(3);
        final ImmutableVector3StateSpaceMapper velocityVectorMapper = new ImmutableVector3StateSpaceMapper(4);

        constructor(MASS_1, positionVectorMapper, velocityVectorMapper);
    }

    @Test
    public void evaluate_1MassX0() {
        final double mass = 2.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 2.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double eExpected = 4.0;
        final double dEDXExpected = -4.0;
        final double dEDVExpected = 2.0;
        final double tolerance = 1E-3;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }

    @Test
    public void evaluate_1MinimumBase() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double tolerance = 1E-3;

        evaluate_1Minimum(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, tolerance);
    }

    @Test
    public void evaluate_1MinimumDEDV0() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 2.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double tolerance = 1E-3;

        evaluate_1Minimum(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, tolerance);
    }

    @Test
    public void evaluate_1MinimumDEDX0() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 2.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double tolerance = 1E-3;

        evaluate_1Minimum(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, tolerance);
    }

    @Test
    public void evaluate_1MinimumMass() {
        final double mass = 2.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double tolerance = 1E-3;

        evaluate_1Minimum(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, tolerance);
    }

    @Test
    public void evaluate_1MinimumMoving() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 1.0;
        final double x = 1.0;
        final double v = v0;
        final double dt = 1.0;
        final double tolerance = 1E-3;

        evaluate_1Minimum(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, tolerance);
    }

    @Test
    public void evaluate_1MinimumTerms() {
        final double mass = 1.0;
        final int positionTerm = 1;
        final int velocityTerm = 0;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double tolerance = 1E-3;

        evaluate_1Minimum(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, tolerance);
    }

    @Test
    public void evaluate_1V() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 2.0;
        final double dt = 1.0;
        final double eExpected = 0.5;
        final double dEDXExpected = -1.0;
        final double dEDVExpected = 0.5;
        final double tolerance = 1E-3;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }

    @Test
    public void evaluate_1V0() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 2.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double eExpected = 0.5;
        final double dEDXExpected = -1.0;
        final double dEDVExpected = 0.5;
        final double tolerance = 1E-3;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }

    @Test
    public void evaluate_1X() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 2.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double eExpected = 2.0;
        final double dEDXExpected = 2.0;
        final double dEDVExpected = -1.0;
        final double tolerance = 1E-3;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }

    @Test
    public void evaluate_1X0() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 2.0;
        final double v0 = 0.0;
        final double x = 0.0;
        final double v = 0.0;
        final double dt = 1.0;
        final double eExpected = 2.0;
        final double dEDXExpected = -2.0;
        final double dEDVExpected = 1.0;
        final double tolerance = 1E-3;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }

    @Test
    public void evaluate_1XDT() {
        final double mass = 1.0;
        final int positionTerm = 0;
        final int velocityTerm = 1;
        final double dedx0 = 0.0;
        final double dedv0 = 0.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double x = 2.0;
        final double v = 0.0;
        final double dt = 2.0;
        final double eExpected = 0.5;
        final double dEDXExpected = 0.5;
        final double dEDVExpected = -0.5;
        final double tolerance = 1E-3;

        evaluate_1(mass, positionTerm, velocityTerm, dedx0, dedv0, x0, v0, x, v, dt, eExpected, dEDXExpected,
                dEDVExpected, tolerance);
    }
}
