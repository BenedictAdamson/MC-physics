package uk.badamson.mc.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.FunctionNWithGradientTest;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests for the class {@link TimeStepEnergyErrorFunction}.
 * </p>
 */
public class TimeStepEnergyErrorFunctionTest {

    private static final class QuadraticTerm1 implements TimeStepEnergyErrorFunctionTerm {

        private final double xMin;
        private final double eMin;

        QuadraticTerm1(double xMin, double eMin) {
            this.xMin = xMin;
            this.eMin = eMin;
        }

        @Override
        public final double evaluate(double[] dedx, ImmutableVectorN x0, ImmutableVectorN x, double dt) {
            Objects.requireNonNull(dedx, "dsdx");
            Objects.requireNonNull(x0, "x0");
            if (dedx.length != x0.getDimension()) {
                throw new IllegalArgumentException(
                        "Inconsistent length " + dedx.length + " for dimension " + x0.getDimension());
            }

            final double xr = x.get(0) - xMin;
            dedx[0] += 2.0 * xr;
            return eMin + xr * xr;
        }

        @Override
        public final boolean isValidForDimension(int n) {
            return n == 1;
        }

    }// class

    private static final class ZeroTerm implements TimeStepEnergyErrorFunctionTerm {

        @Override
        public double evaluate(double[] dedx, ImmutableVectorN x0, ImmutableVectorN x, double dt) {
            Objects.requireNonNull(dedx, "dsdx");
            Objects.requireNonNull(x0, "x0");
            if (dedx.length != x0.getDimension()) {
                throw new IllegalArgumentException(
                        "Inconsistent length " + dedx.length + " for dimension " + x0.getDimension());
            }
            return 0;
        }

        @Override
        public final boolean isValidForDimension(int n) {
            return true;
        }

    }// class

    private static final double DT_A = 1.0;

    private static final double DT_B = 1E-3;
    private static final ImmutableVectorN X_1A = ImmutableVectorN.create(1.0);

    private static final ImmutableVectorN X_1B = ImmutableVectorN.create(2.0);
    private static final ImmutableVectorN X_2A = ImmutableVectorN.create(1.0, 2.0);
    private static final ImmutableVectorN X_2B = ImmutableVectorN.create(3.0, 5.0);

    private static final ZeroTerm TERM_0A = new ZeroTerm();
    private static final ZeroTerm TERM_0B = new ZeroTerm();

    public static void assertInvariants(TimeStepEnergyErrorFunction f) {
        ObjectTest.assertInvariants(f);// inherited
        FunctionNWithGradientTest.assertInvariants(f);// inherited

        final ImmutableVectorN x0 = f.getX0();
        final double dt = f.getDt();
        final Collection<TimeStepEnergyErrorFunctionTerm> terms = f.getTerms();

        assertNotNull("Always have a state vector of the physical system at the current point in time.", x0);// guard
        assertNotNull("Always have a collection of terms.", terms);// guard

        assertTrue("The time-step <" + dt + "> is positive and finite.", 0.0 < dt && Double.isFinite(dt));
        for (TimeStepEnergyErrorFunctionTerm term : terms) {
            assertNotNull("The collection of terms does not contain any null elements.", term);// guard
            TimeStepEnergyErrorFunctionTermTest.assertInvariants(term);
        }
        assertEquals(
                "The dimension equals the dimension of the state vector of the physical system at the current point in time.",
                x0.getDimension(), f.getDimension());
    }

    public static void assertInvariants(TimeStepEnergyErrorFunction f1, TimeStepEnergyErrorFunction f2) {
        ObjectTest.assertInvariants(f1, f2);// inherited
    }

    private static TimeStepEnergyErrorFunction constructor(ImmutableVectorN x0, double dt,
            List<TimeStepEnergyErrorFunctionTerm> terms) {
        final TimeStepEnergyErrorFunction f = new TimeStepEnergyErrorFunction(x0, dt, terms);

        assertInvariants(f);
        assertSame("x0", x0, f.getX0());
        assertEquals("dt", dt, f.getDt(), Double.MIN_NORMAL);
        assertEquals("terms", terms, f.getTerms());

        return f;
    }

    private static FunctionNWithGradientValue value(TimeStepEnergyErrorFunction f, ImmutableVectorN x) {
        final FunctionNWithGradientValue fx = FunctionNWithGradientTest.value(f, x);// inherited

        return fx;
    }

    private static void value_0(ImmutableVectorN x0, double dt, ImmutableVectorN x) {
        final List<TimeStepEnergyErrorFunctionTerm> terms = Collections.emptyList();
        final TimeStepEnergyErrorFunction f = new TimeStepEnergyErrorFunction(x0, dt, terms);

        final FunctionNWithGradientValue fx = FunctionNWithGradientTest.value(f, x);// inherited

        assertEquals("value.f", fx.getF(), 0.0, Double.MIN_NORMAL);
        assertEquals("value.dfDx <" + fx.getDfDx() + "> magnitude", fx.getDfDx().magnitude(), 0.0, Double.MIN_NORMAL);
    }

    private static void value_quadraticTerm(double x0, double dt, double x, double xMin, double eMin, double expectedE,
            double expectedDeDx) {
        final QuadraticTerm1 term = new QuadraticTerm1(xMin, eMin);
        final List<TimeStepEnergyErrorFunctionTerm> terms = Collections.singletonList(term);
        final TimeStepEnergyErrorFunction f = new TimeStepEnergyErrorFunction(ImmutableVectorN.create(x0), dt, terms);

        final FunctionNWithGradientValue fx = value(f, ImmutableVectorN.create(x));// inherited

        assertEquals("value.f", expectedE, fx.getF(), Double.MIN_NORMAL);
        assertEquals("value.dfDx", expectedDeDx, fx.getDfDx().get(0), Double.MIN_NORMAL);
    }

    @Test
    public void constructor_A() {
        final List<TimeStepEnergyErrorFunctionTerm> terms = Collections.emptyList();
        constructor(X_1A, DT_A, terms);
    }

    @Test
    public void constructor_B() {
        final List<TimeStepEnergyErrorFunctionTerm> terms = Collections.singletonList(TERM_0A);
        constructor(X_1A, DT_A, terms);
    }

    @Test
    public void constructor_C() {
        final List<TimeStepEnergyErrorFunctionTerm> terms = Arrays.asList(TERM_0A, TERM_0B);
        constructor(X_1A, DT_B, terms);
    }

    @Test
    public void constructor_D() {
        final List<TimeStepEnergyErrorFunctionTerm> terms = Arrays.asList(TERM_0A);
        constructor(X_2A, DT_B, terms);
    }

    @Test
    public void value_0A() {
        value_0(X_1A, DT_A, X_1B);
    }

    @Test
    public void value_0B() {
        value_0(X_2A, DT_B, X_2B);
    }

    @Test
    public void value_quadraticTermA() {
        final double x0 = 0;
        final double dt = 1.0;
        final double x = 0.0;
        final double xMin = 0.0;
        final double eMin = 0.0;
        final double expectedE = 0.0;
        final double expectedDeDx = 0.0;

        value_quadraticTerm(x0, dt, x, xMin, eMin, expectedE, expectedDeDx);
    }

    @Test
    public void value_quadraticTermB() {
        final double x0 = 1.0;
        final double dt = 1.0;
        final double x = 0.0;
        final double xMin = 0.0;
        final double eMin = 0.0;
        final double expectedE = 0.0;
        final double expectedDeDx = 0.0;

        value_quadraticTerm(x0, dt, x, xMin, eMin, expectedE, expectedDeDx);
    }

    @Test
    public void value_quadraticTermC() {
        final double x0 = 0;
        final double dt = 2.0;
        final double x = 0.0;
        final double xMin = 0.0;
        final double eMin = 0.0;
        final double expectedE = 0.0;
        final double expectedDeDx = 0.0;

        value_quadraticTerm(x0, dt, x, xMin, eMin, expectedE, expectedDeDx);
    }

    @Test
    public void value_quadraticTermD() {
        final double x0 = 0;
        final double dt = 1.0;
        final double x = 1.0;
        final double xMin = 0.0;
        final double eMin = 0.0;
        final double expectedE = 1.0;
        final double expectedDeDx = 2.0;

        value_quadraticTerm(x0, dt, x, xMin, eMin, expectedE, expectedDeDx);
    }

    @Test
    public void value_quadraticTermE() {
        final double x0 = 0;
        final double dt = 1.0;
        final double x = 0.0;
        final double xMin = 1.0;
        final double eMin = 0.0;
        final double expectedE = 1.0;
        final double expectedDeDx = -2.0;

        value_quadraticTerm(x0, dt, x, xMin, eMin, expectedE, expectedDeDx);
    }

    @Test
    public void value_quadraticTermF() {
        final double x0 = 0;
        final double dt = 1.0;
        final double x = 0.0;
        final double xMin = 0.0;
        final double eMin = 1.0;
        final double expectedE = 1.0;
        final double expectedDeDx = 0.0;

        value_quadraticTerm(x0, dt, x, xMin, eMin, expectedE, expectedDeDx);
    }

    @Test
    public void value_quadraticTerms() {
        final double x0 = 0;
        final double dt = 1.0;
        final double x = 1.0;
        final double xMin = 0.0;
        final double eMin = 0.0;
        final double expectedE = 2.0;
        final double expectedDeDx = 4.0;

        final QuadraticTerm1 term = new QuadraticTerm1(xMin, eMin);
        final List<TimeStepEnergyErrorFunctionTerm> terms = Arrays.asList(term, term);
        final TimeStepEnergyErrorFunction f = new TimeStepEnergyErrorFunction(ImmutableVectorN.create(x0), dt, terms);

        final FunctionNWithGradientValue fx = value(f, ImmutableVectorN.create(x));// inherited

        assertEquals("value.f", expectedE, fx.getF(), Double.MIN_NORMAL);
        assertEquals("value.dfDx", expectedDeDx, fx.getDfDx().get(0), Double.MIN_NORMAL);
    }
}