package uk.badamson.mc.physics.kinematics;

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
import uk.badamson.mc.physics.AbstractTimeStepEnergyErrorFunctionTerm;
import uk.badamson.mc.physics.ImmutableVector1StateSpaceMapper;
import uk.badamson.mc.physics.TimeStepEnergyErrorFunction;
import uk.badamson.mc.physics.TimeStepEnergyErrorFunctionTerm;

/**
 * <p>
 * Integration tests of classes in the package
 * uk.badamson.mc.physics.kinematics.
 * </p>
 */
public class IntegrationTest {

    private static final class ConstantVelocityError extends AbstractTimeStepEnergyErrorFunctionTerm {

        private final double mass;

        public ConstantVelocityError(final double mass) {
            this.mass = mass;
        }

        @Override
        public final double evaluate(final double[] dedx, final ImmutableVectorN state0, final ImmutableVectorN state,
                final double dt) {
            final double v0 = state0.get(1);
            final double v = state.get(1);
            final double vError = v - v0;
            final double mvError = mass * vError;
            final double eError = 0.5 * mvError * vError;
            dedx[1] += mvError;
            return eError;
        }

        @Override
        public final boolean isValidForDimension(final int n) {
            return 2 <= n;
        }

    }// class

    private static void assertConstantVelocityErrorFunctionConsitentWithGradientAlongLine(final double x0,
            final double v0, final double dt, final double dx, final double dv, final double da, final double mass,
            final double tolerance, final double w1, final double w2, final int n) {
        final TimeStepEnergyErrorFunction errorFunction = create1DconstantVelocityErrorFunction(x0, v0, dt, mass);
        final ImmutableVectorN s0 = create1DStateVector(x0, v0, 0.0);
        final ImmutableVectorN ds = create1DStateVector(dx, dv, da);
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

    private static void constantVelocitySolution(final double x0, final double v0, final double dt, final double mass,
            final double tolerance) {
        final TimeStepEnergyErrorFunction errorFunction = create1DconstantVelocityErrorFunction(x0, v0, dt, mass);
        final ImmutableVectorN state0 = create1DStateVector(x0, v0, 0.0);

        final FunctionNWithGradientValue solution = MinN.findFletcherReevesPolakRibere(errorFunction, state0,
                tolerance);

        final ImmutableVectorN state = solution.getX();
        final double x = state.get(0);
        final double v = state.get(1);
        final double a = state.get(2);

        assertEquals(x0 + dt * v0, x, tolerance, "x");
        assertEquals(v0, v, tolerance, "v");
        assertEquals(0, a, tolerance, "a");
    }

    private static TimeStepEnergyErrorFunction create1DconstantVelocityErrorFunction(final double x0, final double v0,
            final double dt, final double mass) {
        final ImmutableVector1StateSpaceMapper positionVectorMapper = new ImmutableVector1StateSpaceMapper(0);
        final ImmutableVector1StateSpaceMapper velocityVectorMapper = new ImmutableVector1StateSpaceMapper(1);
        final ImmutableVector1StateSpaceMapper accelerationVectorMapper = new ImmutableVector1StateSpaceMapper(2);
        final List<TimeStepEnergyErrorFunctionTerm> terms = Arrays.asList(
                new PositionError<>(mass, positionVectorMapper, velocityVectorMapper),
                new VelocityError<>(mass, velocityVectorMapper, accelerationVectorMapper),
                new ConstantVelocityError(mass));

        final TimeStepEnergyErrorFunction errorFunction = new TimeStepEnergyErrorFunction(
                create1DStateVector(x0, v0, 0.0), dt, terms);
        return errorFunction;
    }

    private static ImmutableVectorN create1DStateVector(final double x, final double v, final double a) {
        return ImmutableVectorN.create(x, v, a);
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
    public void assertConstantVelocityErrorFunctionConsitentWithGradientAlongLineDA() {
        final double x0 = 0.0;
        final double v0 = 2.0;
        final double dt = 1.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;
        ;
        final double dx = 0.0;
        final double dv = 0.0;
        final double da = 1.0;

        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assertConstantVelocityErrorFunctionConsitentWithGradientAlongLine(x0, v0, dt, dx, dv, da, mass, tolerance, w1,
                w2, n);
    }

    @Test
    public void assertConstantVelocityErrorFunctionConsitentWithGradientAlongLineDV() {
        final double x0 = 0.0;
        final double v0 = 2.0;
        final double dt = 1.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;
        ;
        final double dx = 0.0;
        final double dv = 1.0;
        final double da = 0.0;

        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assertConstantVelocityErrorFunctionConsitentWithGradientAlongLine(x0, v0, dt, dx, dv, da, mass, tolerance, w1,
                w2, n);
    }

    @Test
    public void assertConstantVelocityErrorFunctionConsitentWithGradientAlongLineDX() {
        final double x0 = 0.0;
        final double v0 = 2.0;
        final double dt = 1.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;
        ;
        final double dx = 1.0;
        final double dv = 0.0;
        final double da = 0.0;

        final double w1 = -2.0;
        final double w2 = 2.0;
        final int n = 20;

        assertConstantVelocityErrorFunctionConsitentWithGradientAlongLine(x0, v0, dt, dx, dv, da, mass, tolerance, w1,
                w2, n);
    }

    @Test
    public void constantVelocity_Base() {
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double dt = 1.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;

        constantVelocitySolution(x0, v0, dt, mass, tolerance);
    }

    @Test
    public void constantVelocity_dt() {
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double dt = 2.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;

        constantVelocitySolution(x0, v0, dt, mass, tolerance);
    }

    @Test
    public void constantVelocity_m() {
        final double x0 = 0.0;
        final double v0 = 0.0;
        final double dt = 1.0;
        final double mass = 2.0;
        final double tolerance = 1E-6;

        constantVelocitySolution(x0, v0, dt, mass, tolerance);
    }

    @Test
    public void constantVelocity_v() {
        final double x0 = 0.0;
        final double v0 = 2.0;
        final double dt = 1.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;

        constantVelocitySolution(x0, v0, dt, mass, tolerance);
    }

    @Test
    public void constantVelocity_x() {
        final double x0 = 2.0;
        final double v0 = 0.0;
        final double dt = 1.0;
        final double mass = 1.0;
        final double tolerance = 1E-6;

        constantVelocitySolution(x0, v0, dt, mass, tolerance);
    }

}
