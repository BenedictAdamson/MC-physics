package uk.badamson.mc.physics.dynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.physics.AbstractTimeStepEnergyErrorFunctionTermTest;

/**
 * <p>
 * Unit tests for the class {@link Newton2Error}.
 * </p>
 */
public class Newton2ErrorTest {

    private static final double MASS_REFERENCE_1 = 1.0;

    private static final double MASS_REFERENCE_2 = 12.0;

    private static final double TIME_REFERNCE_1 = 1.0;

    private static final double TIME_REFERNCE_2 = 1.0E3;

    public static void assertInvariants(final Newton2Error term) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term);// inherited

        final int spaceDimension = term.getSpaceDimension();
        final int numberOfForces = term.getNumberOfForces();
        final int numberOfMassTransfers = term.getNumberOfMassTransfers();
        final double massReference = term.getMassReference();
        final double timeReference = term.getTimeReference();

        assertTrue(0 <= numberOfMassTransfers, "numberOfMassTransfers not negative");
        assertTrue(0 <= numberOfForces, "numberOfForces not negative");
        assertTrue(0 < spaceDimension, "spaceDimension is positive");

        assertTrue(0.0 < massReference && Double.isFinite(massReference), "massReference is positive and finite");
        assertTrue(0.0 < timeReference && Double.isFinite(timeReference), "timeReference is positive and finite");

        assertTrue(0 <= term.getMassTerm(), "massTerm is not negative");
        for (int i = 0; i < spaceDimension; ++i) {
            assertTrue(0 <= term.getAccelerationTerm(i), "accelerationTerm is not negative");
            assertTrue(0 <= term.getVelocityTerm(i), "velocityTerm is not negative");
            for (int j = 0; j < numberOfMassTransfers; ++j) {
                assertTrue(0 <= term.getAdvectionVelocityTerm(j, i), "advectionVelocityTerm is not negative");
            }
            for (int k = 0; k < numberOfForces; ++k) {
                assertTrue(0 <= term.getForceTerm(k, i), "forceTerm is not negative");
            }
        }
        for (int j = 0; j < numberOfMassTransfers; ++j) {
            assertTrue(0 <= term.getAdvectionMassRateTerm(j), "advectionMassRateTerm is not negative");
        }
    }

    public static void assertInvariants(final Newton2Error term1, final Newton2Error term2) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term1, term2);// inherited
    }

    private static Newton2Error constructor(final double massReference, final double timeReference, final int massTerm,
            final int[] velocityTerm, final int[] accelerationTerm, final boolean[] massTransferInto,
            final int[] advectionMassRateTerm, final int[] advectionVelocityTerm, final boolean[] forceOn,
            final int[] forceTerm) {
        final Newton2Error term = new Newton2Error(massReference, timeReference, massTerm, velocityTerm,
                accelerationTerm, massTransferInto, advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);

        assertInvariants(term);

        assertEquals(massReference, term.getMassReference(), Double.MIN_NORMAL, "massReference");
        assertEquals(timeReference, term.getTimeReference(), Double.MIN_NORMAL, "timeReference");

        assertEquals(velocityTerm.length, term.getSpaceDimension(), "spaceDimension");
        assertEquals(massTransferInto.length, term.getNumberOfMassTransfers(), "numberOfMassTransfers");
        assertEquals(forceOn.length, term.getNumberOfForces(), "numberOfForces");

        assertEquals(massTerm, term.getMassTerm(), "massTerm");
        for (int i = 0; i < velocityTerm.length; ++i) {
            assertEquals(velocityTerm[i], term.getVelocityTerm(i), "velocityTerm[" + i + "]");
            assertEquals(accelerationTerm[i], term.getAccelerationTerm(i), "accelerationTerm[" + i + "]");
        }
        for (int j = 0; j < massTransferInto.length; ++j) {
            assertEquals(massTransferInto[j], term.isMassTransferInto(j), "massTransferInto[" + j + "]");
            assertEquals(advectionMassRateTerm[j], term.getAdvectionMassRateTerm(j),
                    "advectionMassRateTerm[" + j + "]");
            for (int i = 0; i < velocityTerm.length; ++i) {
                assertEquals(advectionVelocityTerm[j * velocityTerm.length + i], term.getAdvectionVelocityTerm(j, i),
                        "advectionVelocityTerm[" + j + "," + i + "]");
            }
        }
        for (int k = 0; k < forceOn.length; ++k) {
            assertEquals(forceOn[k], term.isForceOn(k), "forceOn[" + k + "]");
            for (int i = 0; i < velocityTerm.length; ++i) {
                assertEquals(forceTerm[k * velocityTerm.length + i], term.getForceTerm(k, i),
                        "forceTerm[" + k + "," + i + "]");
            }
        }

        return term;
    }

    private static double evaluate(final Newton2Error term, final double[] dedx, final ImmutableVectorN state0,
            final ImmutableVectorN state, final double dt) {
        final double e = AbstractTimeStepEnergyErrorFunctionTermTest.evaluate(term, dedx, state0, state, dt);// inherited

        assertInvariants(term);
        assertTrue(0.0 <= e, "value is not negative");

        return e;
    }

    private static void evaluate_1Advection(final double massReference, final double timeReference,
            final boolean massTransferInto, final double m0, final double v0, final double mrate0, final double u0,
            final double a0, final double m, final double v, final double a, final double mrate, final double u,
            final double dt, final double dedmrate0, final double dedu0, final double expectedE,
            final double expectedDedm, final double expectedDedv, final double expectedDeda,
            final double expectedDedmrate, final double expectedDedu) {
        final int massTerm = 0;
        final int[] velocityTerm = { 1 };
        final int[] accelerationTerm = { 2 };
        final int[] advectionMassRateTerm = { 3 };
        final int[] advectionVelocityTerm = { 4 };
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        final double[] dedx = { 0.0, 0.0, 0.0, dedmrate0, dedu0 };
        final ImmutableVectorN state0 = ImmutableVectorN.create(m0, v0, a0, mrate0, u0);
        final ImmutableVectorN state = ImmutableVectorN.create(m, v, a, mrate, u);

        final Newton2Error term = new Newton2Error(massReference, timeReference, massTerm, velocityTerm,
                accelerationTerm, new boolean[] { massTransferInto }, advectionMassRateTerm, advectionVelocityTerm,
                forceOn, forceTerm);

        final double e = evaluate(term, dedx, state0, state, dt);

        assertEquals(expectedE, e, 1E-8, "e");
        assertEquals(expectedDedm, dedx[0], 1E-8, "dedm");
        assertEquals(expectedDedv, dedx[1], 1E-8, "dedv");
        assertEquals(expectedDeda, dedx[2], 1E-8, "deda");
        assertEquals(expectedDedmrate, dedx[3], 1E-8, "dedmrate");
        assertEquals(expectedDedu, dedx[4], 1E-8, "dedu");
    }

    private static void evaluate_1Closed(final double massReference, final double timeReference, final double dedm0,
            final double dedv0, final double deda0, final double m0, final double v0, final double a0, final double m,
            final double v, final double a, final double dt, final double expectedE, final double expectedDedm,
            final double expectedDedv, final double expectedDeda) {
        final int massTerm = 0;
        final int[] velocityTerm = { 1 };
        final int[] accelerationTerm = { 2 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        final double[] dedx = { dedm0, dedv0, deda0 };
        final ImmutableVectorN state0 = ImmutableVectorN.create(m0, v0, a0);
        final ImmutableVectorN state = ImmutableVectorN.create(m, v, a);

        final Newton2Error term = new Newton2Error(massReference, timeReference, massTerm, velocityTerm,
                accelerationTerm, massTransferInto, advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);

        final double e = evaluate(term, dedx, state0, state, dt);

        assertEquals(expectedE, e, 1E-8, "e");
        assertEquals(expectedDedm, dedx[0], 1E-8, "dedm");
        assertEquals(expectedDedv, dedx[1], 1E-8, "dedv");
        assertEquals(expectedDeda, dedx[2], 1E-8, "deda");
    }

    private static void evaluate_1Force(final double massReference, final double timeReference, final boolean forceOn,
            final double m0, final double v0, final double a0, final double f0, final double m, final double v,
            final double a, final double f, final double dt, final double dedf0, final double expectedE,
            final double expectedDedm, final double expectedDedv, final double expectedDeda,
            final double expectedDedf) {
        final int massTerm = 0;
        final int[] velocityTerm = { 1 };
        final int[] accelerationTerm = { 2 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final int[] forceTerm = { 3 };

        final double[] dedx = { 0.0, 0.0, 0.0, dedf0 };
        final ImmutableVectorN state0 = ImmutableVectorN.create(m0, v0, a0, f0);
        final ImmutableVectorN state = ImmutableVectorN.create(m, v, a, f);

        final Newton2Error term = new Newton2Error(massReference, timeReference, massTerm, velocityTerm,
                accelerationTerm, massTransferInto, advectionMassRateTerm, advectionVelocityTerm,
                new boolean[] { forceOn }, forceTerm);

        final double e = evaluate(term, dedx, state0, state, dt);

        assertEquals(expectedE, e, 1E-8, "e");
        assertEquals(expectedDedm, dedx[0], 1E-8, "dedm");
        assertEquals(expectedDedv, dedx[1], 1E-8, "dedv");
        assertEquals(expectedDeda, dedx[2], 1E-8, "deda");
        assertEquals(expectedDedf, dedx[3], 1E-8, "dedf");
    }

    @Test
    public void constructor_1A() {
        final int massTerm = 0;
        final int[] velocityTerm = { 2 };
        final int[] accelerationTerm = { 3 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        constructor(MASS_REFERENCE_1, TIME_REFERNCE_1, massTerm, velocityTerm, accelerationTerm, massTransferInto,
                advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);
    }

    @Test
    public void constructor_1B() {
        final int massTerm = 3;
        final int[] velocityTerm = { 5 };
        final int[] accelerationTerm = { 7 };
        final boolean[] massTransferInto = { false };
        final int[] advectionMassRateTerm = { 11 };
        final int[] advectionVelocityTerm = { 13 };
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        constructor(MASS_REFERENCE_2, TIME_REFERNCE_2, massTerm, velocityTerm, accelerationTerm, massTransferInto,
                advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);
    }

    @Test
    public void constructor_1C() {
        final int massTerm = 0;
        final int[] velocityTerm = { 2 };
        final int[] accelerationTerm = { 3 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final boolean[] forceOn = { true };
        final int[] forceTerm = { 7 };

        constructor(MASS_REFERENCE_1, TIME_REFERNCE_1, massTerm, velocityTerm, accelerationTerm, massTransferInto,
                advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);
    }

    @Test
    public void evaluate_1AdvectionA() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 2.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = 4.0;
        final double expectedDedv = 2.0;
        final double expectedDeda = 2.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -2.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionA0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 2.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionBase() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionDedmrate0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 2.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 2.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionDedu0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 2.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = 1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionDt() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 2.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionM() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 2.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = 2.0;
        final double expectedDedv = 2.0;
        final double expectedDeda = 4.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -2.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionM0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 2.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionMassReference() {
        final double massReference = 2.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.0625;
        final double expectedDedm = 0.25;
        final double expectedDedv = 0.25;
        final double expectedDeda = 0.25;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -0.25;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionMassTransferInto() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = false;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = -1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = 1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionMRate() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 2.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 2.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -2.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionMrate0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 2.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionTimeReference() {
        final double massReference = 1.0;
        final double timeReference = 2.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = 4.0;
        final double expectedDedv = 4.0;
        final double expectedDeda = 4.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -4.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionU() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 2.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.0;
        final double expectedDedm = 0.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 0.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = 0.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionU0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 2.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionV() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 2.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = 2.0;
        final double expectedDedv = 2.0;
        final double expectedDeda = 2.0;
        final double expectedDedmrate = 2.0;
        final double expectedDedu = -2.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionV0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 2.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;
        final double a0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 1.0;
        final double expectedDeda = 1.0;
        final double expectedDedmrate = 0.0;
        final double expectedDedu = -1.0;

        evaluate_1Advection(massReference, timeReference, massTransferInto, m0, v0, mrate0, u0, a0, m, v, a, mrate, u,
                dt, dedmrate0, dedu0, expectedE, expectedDedm, expectedDedv, expectedDeda, expectedDedmrate,
                expectedDedu);
    }

    @Test
    public void evaluate_1ClosedA() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 2.0;
        final double dt = 1.0;
        final double expectedE = 2.0;
        final double expectedDedm = 4.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 2.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedA0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 2.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedBase() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedDeda0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 2.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 3.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedDedm0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 2.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 3.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedDedv0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 2.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 2.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedDt() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 2.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedM() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 2.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 2.0;
        final double expectedDedm = 2.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 4.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedM0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 2.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedTimeReference() {
        final double massReference = 1.0;
        final double timeReference = 2.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 2.0;
        final double expectedDedm = 4.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 4.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedV() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 2.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ClosedV0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 2.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 1.0;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

    @Test
    public void evaluate_1ForceA() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 2.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.0;
        final double expectedDedm = 0.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 0.0;
        final double expectedDedf = 0.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceA0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 2.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceBase() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceDedf0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 1.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 2.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceDt() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 2.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceF() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 3.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = -2.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -2.0;
        final double expectedDedf = 2.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceF0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 2.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceForceOn() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = false;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 4.5;
        final double expectedDedm = 3.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 3.0;
        final double expectedDedf = 3.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceM() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 2.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.0;
        final double expectedDedm = 0.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = 0.0;
        final double expectedDedf = 0.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceMassReference() {
        final double massReference = 2.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.0625;
        final double expectedDedm = -0.25;
        final double expectedDedv = 0.0;
        final double expectedDeda = -0.25;
        final double expectedDedf = 0.25;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceTimeReference() {
        final double massReference = 1.0;
        final double timeReference = 2.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = -4.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -4.0;
        final double expectedDedf = 4.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceV() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 2.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1ForceV0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 2.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1M0() {
        final double massReference = 1.0;
        final double timeReference = 1.0;
        final boolean forceOn = true;

        final double m0 = 2.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.0;
        final double expectedDedv = 0.0;
        final double expectedDeda = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(massReference, timeReference, forceOn, m0, v0, a0, f0, m, v, a, f, dt, dedf0, expectedE,
                expectedDedm, expectedDedv, expectedDeda, expectedDedf);
    }

    @Test
    public void evaluate_1MassReference() {
        final double massReference = 2.0;
        final double timeReference = 1.0;
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double deda0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double a0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double a = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.0625;
        final double expectedDedm = 0.25;
        final double expectedDedv = 0.0;
        final double expectedDeda = 0.25;

        evaluate_1Closed(massReference, timeReference, dedm0, dedv0, deda0, m0, v0, a0, m, v, a, dt, expectedE,
                expectedDedm, expectedDedv, expectedDeda);
    }

}
