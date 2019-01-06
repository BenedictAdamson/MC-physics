package uk.badamson.mc.physics;/*
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.Function1WithGradient;
import uk.badamson.mc.math.Function1WithGradientValue;
import uk.badamson.mc.math.FunctionNWithGradient;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.MinN;
import uk.badamson.mc.physics.dynamics.Newton2Error;
import uk.badamson.mc.physics.kinematics.PositionError;
import uk.badamson.mc.physics.kinematics.VelocityError;
import uk.badamson.mc.physics.solver.AbstractTimeStepEnergyErrorFunctionTerm;
import uk.badamson.mc.physics.solver.MassConservationError;
import uk.badamson.mc.physics.solver.MomentumConservationError;
import uk.badamson.mc.physics.solver.TimeStepEnergyErrorFunction;
import uk.badamson.mc.physics.solver.TimeStepEnergyErrorFunctionTerm;
import uk.badamson.mc.physics.solver.mapper.ImmutableVector1StateSpaceMapper;

/**
 * <p>
 * Integration tests of classes in the package uk.badamson.mc.physics.
 * </p>
 */
public class IntegrationTest {
    private static final class ConstantForceError extends AbstractTimeStepEnergyErrorFunctionTerm {

        private final boolean forceOn;

        public ConstantForceError(final boolean forceOn) {
            this.forceOn = forceOn;
        }

        @Override
        public final double evaluate(final double[] dedx, final ImmutableVectorN state0, final ImmutableVectorN state,
                final double dt) {
            final double sign = forceOn ? 1.0 : -1.0;
            final double m0 = state0.get(massTerm);
            final double f0 = state0.get(forceTerm[0]) * sign;

            final double m = state.get(massTerm);
            final double f = state.get(forceTerm[0]);

            final double mMean = (m + m0) * 0.5;

            final double fe = f - f0;
            final double ae = fe / mMean;
            final double ve = ae * dt;
            final double e = 0.5 * mMean * ve * ve;

            final double dedf = ve * dt / mMean;
            dedx[forceTerm[0]] += dedf;

            return e;
        }

        @Override
        public final boolean isValidForDimension(final int n) {
            return 2 <= n;
        }

    }// class

    private static final int massTerm = 0;
    private static final ImmutableVector1StateSpaceMapper positionVectorMapper = new ImmutableVector1StateSpaceMapper(
            1);
    private static final ImmutableVector1StateSpaceMapper velocityVectorMapper = new ImmutableVector1StateSpaceMapper(
            2);
    private static final ImmutableVector1StateSpaceMapper accelerationVectorMapper = new ImmutableVector1StateSpaceMapper(
            3);
    private static final int[] velocityTerm = { 2 };
    private static final int[] accelerationTerm = { 3 };
    private static final int[] forceTerm = { 4 };
    private static final boolean[] massTransferInto = {};
    private static final int[] advectionMassRateTerm = {};

    private static final int[] advectionVelocityTerm = {};

    private static void assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(final boolean forceOn,
            final double massReference, final double timeReference, final double specificEnergyReference,
            final double m0, final double x0, final double v0, final double a0, final double dt, final double dm,
            final double dx, final double dv, final double da, final double df, final double tolerance, final double w1,
            final double w2, final int n) {
        final TimeStepEnergyErrorFunction errorFunction = create1DConstantForceErrorFunction(forceOn, massReference,
                timeReference, specificEnergyReference, m0, x0, v0, a0, dt);
        final ImmutableVectorN s0 = create1DStateVector(m0, x0, v0, a0, m0 * a0);
        final ImmutableVectorN ds = create1DStateVector(dm, dx, dv, da, df);
        assertValueConsistentWithGradientAlongLine(errorFunction, w1, w2, n, s0, ds);
    }

    private static void assertValueConsistentWithGradient(final Function1WithGradient f, final double x1,
            final double x2, final int n) {
        assert 3 <= n;
        final Function1WithGradientValue[] fx = new Function1WithGradientValue[n];
        for (int i = 0; i < n; ++i) {
            final double x = x1 + (x2 - x1) * i / n;
            fx[i] = f.value(x);
        }
        for (int i = 1; i < n - 1; i++) {
            final Function1WithGradientValue fl = fx[i - 1];
            final Function1WithGradientValue fi = fx[i];
            final Function1WithGradientValue fr = fx[i + 1];
            final double dfl = fi.getF() - fl.getF();
            final double dfr = fr.getF() - fi.getF();
            assertTrue(sign(dfl) != sign(dfr) || sign(fi.getDfDx()) == sign(dfl),
                    "Consistent gradient <" + fl + "," + fi + "," + fr + ">");
        }
    }

    private static void assertValueConsistentWithGradientAlongLine(final FunctionNWithGradient f, final double w1,
            final double w2, final int n, final ImmutableVectorN x0, final ImmutableVectorN dx) {
        final Function1WithGradient fLine = MinN.createLineFunction(f, x0, dx);
        assertValueConsistentWithGradient(fLine, w1, w2, n);
    }

    private static void constantForceSolution(final boolean forceOn, final double massReference,
            final double timeReference, final double specificEnergyReference, final double m0, final double x0,
            final double v0, final double a0, final double dt, final double tolerance) {
        final TimeStepEnergyErrorFunction errorFunction = create1DConstantForceErrorFunction(forceOn, massReference,
                timeReference, specificEnergyReference, m0, x0, v0, a0, dt);
        final double f0 = m0 * a0;
        final ImmutableVectorN state0 = create1DStateVector(m0, x0, v0, a0, f0);

        final FunctionNWithGradientValue solution = MinN.findFletcherReevesPolakRibere(errorFunction, state0,
                tolerance);

        final ImmutableVectorN state = solution.getX();
        final double m = state.get(0);
        final double x = state.get(1);
        final double v = state.get(2);
        final double a = state.get(3);
        final double f = state.get(4);

        assertEquals(m0, m, tolerance, "m");
        assertEquals(x0 + dt * (v0 + 0.5 * a0 * dt), x, tolerance, "x");
        assertEquals(v0 + dt * a0, v, tolerance, "v");
        assertEquals(a0, a, tolerance, "a");
        assertEquals(f0, f, tolerance, "f");
    }

    private static TimeStepEnergyErrorFunction create1DConstantForceErrorFunction(final boolean forceOn,
            final double massReference, final double timeReference, final double specificEnergyReference,
            final double m0, final double x0, final double v0, final double a0, final double dt) {
        final double f0 = m0 * a0;
        final List<TimeStepEnergyErrorFunctionTerm> terms = Arrays.asList(
                new PositionError<>(massReference, positionVectorMapper, velocityVectorMapper),
                new VelocityError<>(massReference, velocityVectorMapper, accelerationVectorMapper),
                new Newton2Error(massReference, timeReference, massTerm, velocityTerm, accelerationTerm,
                        massTransferInto, advectionMassRateTerm, advectionVelocityTerm, new boolean[] { forceOn },
                        forceTerm),
                new MassConservationError(massReference, specificEnergyReference, massTerm, massTransferInto,
                        advectionMassRateTerm),
                new MomentumConservationError(massTerm, velocityTerm, massTransferInto, advectionMassRateTerm,
                        advectionVelocityTerm, new boolean[] { forceOn }, forceTerm),
                new ConstantForceError(forceOn));

        final TimeStepEnergyErrorFunction errorFunction = new TimeStepEnergyErrorFunction(
                create1DStateVector(m0, x0, v0, a0, f0), dt, terms);
        return errorFunction;
    }

    private static ImmutableVectorN create1DStateVector(final double m, final double x, final double v, final double a,
            final double f) {
        return ImmutableVectorN.create(m, x, v, a, f);
    }

    private static int sign(final double x) {
        if (x < -Double.MIN_NORMAL) {
            return -1;
        } else if (Double.MIN_NORMAL < x) {
            return 1;
        } else {
            return 0;
        }
    }

    @Test
    public void constantForceErrorFunctionConsitentWithGradientAlongLine_da() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;

        final double dm = 0.0;
        final double dx = 0.0;
        final double dv = 0.0;
        final double da = 1.0;
        final double df = 0.0;

        final double tolerance = 0.0;
        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(forceOn, massReference, timeReference,
                specificEnergyReference, m0, x0, v0, a0, dt, dm, dx, dv, da, df, tolerance, w1, w2, n);
    }

    @Test
    public void constantForceErrorFunctionConsitentWithGradientAlongLine_dfA() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;

        final double dm = 0.0;
        final double dx = 0.0;
        final double dv = 0.0;
        final double da = 0.0;
        final double df = 1.0;

        final double tolerance = 0.0;
        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(forceOn, massReference, timeReference,
                specificEnergyReference, m0, x0, v0, a0, dt, dm, dx, dv, da, df, tolerance, w1, w2, n);
    }

    @Test
    public void constantForceErrorFunctionConsitentWithGradientAlongLine_dfB() {
        final boolean forceOn = false;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;

        final double dm = 0.0;
        final double dx = 0.0;
        final double dv = 0.0;
        final double da = 0.0;
        final double df = 1.0;

        final double tolerance = 0.0;
        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(forceOn, massReference, timeReference,
                specificEnergyReference, m0, x0, v0, a0, dt, dm, dx, dv, da, df, tolerance, w1, w2, n);
    }

    @Test
    public void constantForceErrorFunctionConsitentWithGradientAlongLine_dm() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;

        final double dm = 1.0;
        final double dx = 0.0;
        final double dv = 0.0;
        final double da = 0.0;
        final double df = 0.0;

        final double tolerance = 0.0;
        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(forceOn, massReference, timeReference,
                specificEnergyReference, m0, x0, v0, a0, dt, dm, dx, dv, da, df, tolerance, w1, w2, n);
    }

    @Test
    public void constantForceErrorFunctionConsitentWithGradientAlongLine_dv() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;

        final double dm = 0.0;
        final double dx = 0.0;
        final double dv = 1.0;
        final double da = 0.0;
        final double df = 0.0;

        final double tolerance = 0.0;
        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(forceOn, massReference, timeReference,
                specificEnergyReference, m0, x0, v0, a0, dt, dm, dx, dv, da, df, tolerance, w1, w2, n);
    }

    @Test
    public void constantForceErrorFunctionConsitentWithGradientAlongLine_dx() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;

        final double dm = 0.0;
        final double dx = 1.0;
        final double dv = 0.0;
        final double da = 0.0;
        final double df = 0.0;

        final double tolerance = 0.0;
        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assert1DConstantForceErrorFunctionConsitentWithGradientAlongLine(forceOn, massReference, timeReference,
                specificEnergyReference, m0, x0, v0, a0, dt, dm, dx, dv, da, df, tolerance, w1, w2, n);
    }

    @Test
    public void constantForceSolution_0() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double a0 = 0.0;

        final double dt = 1.0;
        final double tolerance = 1.0E-6;

        constantForceSolution(forceOn, massReference, timeReference, specificEnergyReference, m0, x0, v0, a0, dt,
                tolerance);
    }

    @Test
    public void constantForceSolution_a() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double a0 = 1.0;

        final double dt = 1.0;
        final double tolerance = 1.0E-6;

        constantForceSolution(forceOn, massReference, timeReference, specificEnergyReference, m0, x0, v0, a0, dt,
                tolerance);
    }

    @Test
    public void constantForceSolution_v() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 0.0;
        final double v0 = 1.0;
        final double a0 = 0.0;

        final double dt = 1.0;
        final double tolerance = 1.0E-6;

        constantForceSolution(forceOn, massReference, timeReference, specificEnergyReference, m0, x0, v0, a0, dt,
                tolerance);
    }

    @Test
    public void constantForceSolution_va() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 0.0;
        final double v0 = 1.0;
        final double a0 = 1.0;

        final double dt = 1.0;
        final double tolerance = 1.0E-6;

        constantForceSolution(forceOn, massReference, timeReference, specificEnergyReference, m0, x0, v0, a0, dt,
                tolerance);
    }

    @Test
    public void constantForceSolution_x() {
        final boolean forceOn = true;
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double specificEnergyReference = 1.0;

        final double m0 = 1.0;
        final double x0 = 1.0;
        final double v0 = 0.0;
        final double a0 = 0.0;

        final double dt = 1.0;
        final double tolerance = 1.0E-6;

        constantForceSolution(forceOn, massReference, timeReference, specificEnergyReference, m0, x0, v0, a0, dt,
                tolerance);
    }

}
