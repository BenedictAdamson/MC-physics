package uk.badamson.mc.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests for the class {@link MomentumConservationError}.
 * </p>
 */
public class MomentumConservationErrorTest {

    public static void assertInvariants(final MomentumConservationError term) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term);// inherited

        final int spaceDimension = term.getSpaceDimension();
        final int numberOfForces = term.getNumberOfForces();
        final int numberOfMassTransfers = term.getNumberOfMassTransfers();

        assertTrue(0 <= numberOfMassTransfers, "numberOfMassTransfers not negative");
        assertTrue(0 <= numberOfForces, "numberOfForces not negative");
        assertTrue(0 < spaceDimension, "spaceDimension is positive");

        assertTrue(0 <= term.getMassTerm(), "massTerm is not negative");
        for (int i = 0; i < spaceDimension; ++i) {
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

    public static void assertInvariants(final MomentumConservationError term1, final MomentumConservationError term2) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term1, term2);// inherited
    }

    private static MomentumConservationError constructor(final int massTerm, final int[] velocityTerm,
            final boolean[] massTransferInto, final int[] advectionMassRateTerm, final int[] advectionVelocityTerm,
            final boolean[] forceOn, final int[] forceTerm) {
        final MomentumConservationError term = new MomentumConservationError(massTerm, velocityTerm, massTransferInto,
                advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);

        assertInvariants(term);

        assertEquals(velocityTerm.length, term.getSpaceDimension(), "spaceDimension");
        assertEquals(massTransferInto.length, term.getNumberOfMassTransfers(), "numberOfMassTransfers");
        assertEquals(forceOn.length, term.getNumberOfForces(), "numberOfForces");

        assertEquals(massTerm, term.getMassTerm(), "massTerm");
        for (int i = 0; i < velocityTerm.length; ++i) {
            assertEquals(velocityTerm[i], term.getVelocityTerm(i), "velocityTerm[" + i + "]");
        }
        for (int j = 0; j < massTransferInto.length; ++j) {
            assertEquals(Boolean.valueOf(massTransferInto[j]), Boolean.valueOf(term.isMassTransferInto(j)),
                    "massTransferInto[" + j + "]");
            assertEquals(advectionMassRateTerm[j], term.getAdvectionMassRateTerm(j),
                    "advectionMassRateTerm[" + j + "]");
            for (int i = 0; i < velocityTerm.length; ++i) {
                assertEquals(advectionVelocityTerm[j * velocityTerm.length + i], term.getAdvectionVelocityTerm(j, i),
                        "advectionVelocityTerm[" + j + "," + i + "]");
            }
        }
        for (int k = 0; k < forceOn.length; ++k) {
            assertEquals(Boolean.valueOf(forceOn[k]), Boolean.valueOf(term.isForceOn(k)), "forceOn[" + k + "]");
            for (int i = 0; i < velocityTerm.length; ++i) {
                assertEquals(forceTerm[k * velocityTerm.length + i], term.getForceTerm(k, i),
                        "forceTerm[" + k + "," + i + "]");
            }
        }

        return term;
    }

    private static double evaluate(final MomentumConservationError term, final double[] dedx,
            final ImmutableVectorN state0, final ImmutableVectorN state, final double dt) {
        final double e = AbstractTimeStepEnergyErrorFunctionTermTest.evaluate(term, dedx, state0, state, dt);// inherited

        assertInvariants(term);
        assertTrue(0.0 <= e, "value is not negative");

        return e;
    }

    private static void evaluate_1Advection(final boolean massTransferInto, final double m0, final double v0,
            final double mrate0, final double u0, final double m, final double v, final double mrate, final double u,
            final double dt, final double dedmrate0, final double dedu0, final double expectedE,
            final double expectedDedm, final double expectedDedv, final double expectedDedmrate,
            final double expectedDedu) {
        final int massTerm = 0;
        final int[] velocityTerm = { 1 };
        final int[] advectionMassRateTerm = { 2 };
        final int[] advectionVelocityTerm = { 3 };
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        final double[] dedx = { 0.0, 0.0, dedmrate0, dedu0 };
        final ImmutableVectorN state0 = ImmutableVectorN.create(m0, v0, mrate0, u0);
        final ImmutableVectorN state = ImmutableVectorN.create(m, v, mrate, u);

        final MomentumConservationError term = new MomentumConservationError(massTerm, velocityTerm,
                new boolean[] { massTransferInto }, advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);

        final double e = evaluate(term, dedx, state0, state, dt);

        assertEquals(expectedE, e, 1E-8, "e");
        assertEquals(expectedDedm, dedx[0], 1E-8, "dedm");
        assertEquals(expectedDedv, dedx[1], 1E-8, "dedv");
        assertEquals(expectedDedmrate, dedx[2], 1E-8, "dedmrate");
        assertEquals(expectedDedu, dedx[3], 1E-8, "dedu");
    }

    private static void evaluate_1Closed(final double dedm0, final double dedv0, final double m0, final double v0,
            final double m, final double v, final double dt, final double expectedE, final double expectedDedm,
            final double expectedDedv) {
        final int massTerm = 0;
        final int[] velocityTerm = { 1 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        final double[] dedx = { dedm0, dedv0 };
        final ImmutableVectorN state0 = ImmutableVectorN.create(m0, v0);
        final ImmutableVectorN state = ImmutableVectorN.create(m, v);

        final MomentumConservationError term = new MomentumConservationError(massTerm, velocityTerm, massTransferInto,
                advectionMassRateTerm, advectionVelocityTerm, forceOn, forceTerm);

        final double e = evaluate(term, dedx, state0, state, dt);

        assertEquals(expectedE, e, 1E-8, "e");
        assertEquals(expectedDedm, dedx[0], 1E-8, "dedm");
        assertEquals(expectedDedv, dedx[1], 1E-8, "dedv");
    }

    private static void evaluate_1Force(final boolean forceOn, final double m0, final double v0, final double f0,
            final double m, final double v, final double f, final double dt, final double dedf0, final double expectedE,
            final double expectedDedm, final double expectedDedv, final double expectedDedf) {
        final int massTerm = 0;
        final int[] velocityTerm = { 1 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final int[] forceTerm = { 2 };

        final double[] dedx = { 0.0, 0.0, dedf0 };
        final ImmutableVectorN state0 = ImmutableVectorN.create(m0, v0, f0);
        final ImmutableVectorN state = ImmutableVectorN.create(m, v, f);

        final MomentumConservationError term = new MomentumConservationError(massTerm, velocityTerm, massTransferInto,
                advectionMassRateTerm, advectionVelocityTerm, new boolean[] { forceOn }, forceTerm);

        final double e = evaluate(term, dedx, state0, state, dt);

        assertEquals(expectedE, e, 1E-8, "e");
        assertEquals(expectedDedm, dedx[0], 1E-8, "dedm");
        assertEquals(expectedDedv, dedx[1], 1E-8, "dedv");
        assertEquals(expectedDedf, dedx[2], 1E-8, "dedf");
    }

    @Test
    public void constructor_1A() {
        final int massTerm = 0;
        final int[] velocityTerm = { 2 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        constructor(massTerm, velocityTerm, massTransferInto, advectionMassRateTerm, advectionVelocityTerm, forceOn,
                forceTerm);
    }

    @Test
    public void constructor_1B() {
        final int massTerm = 3;
        final int[] velocityTerm = { 5 };
        final boolean[] massTransferInto = { false };
        final int[] advectionMassRateTerm = { 11 };
        final int[] advectionVelocityTerm = { 13 };
        final boolean[] forceOn = {};
        final int[] forceTerm = {};

        constructor(massTerm, velocityTerm, massTransferInto, advectionMassRateTerm, advectionVelocityTerm, forceOn,
                forceTerm);
    }

    @Test
    public void constructor_1C() {
        final int massTerm = 0;
        final int[] velocityTerm = { 2 };
        final boolean[] massTransferInto = {};
        final int[] advectionMassRateTerm = {};
        final int[] advectionVelocityTerm = {};
        final boolean[] forceOn = { true };
        final int[] forceTerm = { 7 };

        constructor(massTerm, velocityTerm, massTransferInto, advectionMassRateTerm, advectionVelocityTerm, forceOn,
                forceTerm);
    }

    @Test
    public void evaluate_1AdvectionBase() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -0.5;
        final double expectedDedv = -0.50;
        final double expectedDedmrate = 0.50;
        final double expectedDedu = 0.25;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionDedmrate0() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 1.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -0.5;
        final double expectedDedv = -0.50;
        final double expectedDedmrate = 1.50;
        final double expectedDedu = 0.25;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionDedu0() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 1.0;

        final double expectedE = 0.5;
        final double expectedDedm = -0.5;
        final double expectedDedv = -0.50;
        final double expectedDedmrate = 0.50;
        final double expectedDedu = 1.25;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionDt() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 2.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 2.0;
        final double expectedDedm = -2.0;
        final double expectedDedv = 0.0;
        final double expectedDedmrate = 2.0;
        final double expectedDedu = 0.5;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionM() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 2.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.25;
        final double expectedDedm = -0.125;
        final double expectedDedv = -0.75;
        final double expectedDedmrate = 0.25;
        final double expectedDedu = 0.125;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionMRate() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 2.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -1.125;
        final double expectedDedv = 0.0;
        final double expectedDedmrate = 0.75;
        final double expectedDedu = 0.75;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionMrate0() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 2.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -1.125;
        final double expectedDedv = -0.75;
        final double expectedDedmrate = 0.75;
        final double expectedDedu = 0.375;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionU() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 2.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -1.125;
        final double expectedDedv = -0.75;
        final double expectedDedmrate = 1.5;
        final double expectedDedu = 0.375;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionU0() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 2.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -1.125;
        final double expectedDedv = -0.75;
        final double expectedDedmrate = 0.75;
        final double expectedDedu = 0.375;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionV() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 0.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 0.125;
        final double expectedDedm = 0.375;
        final double expectedDedv = 0.25;
        final double expectedDedmrate = 0;
        final double expectedDedu = -0.125;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1AdvectionV0() {
        final boolean massTransferInto = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double mrate0 = 1.0;
        final double u0 = 1.0;

        final double m = 1.0;
        final double v = 0.0;
        final double mrate = 1.0;
        final double u = 1.0;

        final double dt = 1.0;
        final double dedmrate0 = 0.0;
        final double dedu0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -1.125;
        final double expectedDedv = -0.75;
        final double expectedDedmrate = 0.75;
        final double expectedDedu = 0.375;

        evaluate_1Advection(massTransferInto, m0, v0, mrate0, u0, m, v, mrate, u, dt, dedmrate0, dedu0, expectedE,
                expectedDedm, expectedDedv, expectedDedmrate, expectedDedu);
    }

    @Test
    public void evaluate_1ClosedBase() {
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.0;
        final double expectedDedm = 0.0;
        final double expectedDedv = 0.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedDedm0() {
        final double dedm0 = 2.0;
        final double dedv0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.0;
        final double expectedDedm = 2.0;
        final double expectedDedv = 0.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedDedv0() {
        final double dedm0 = 0.0;
        final double dedv0 = 2.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.0;
        final double expectedDedm = 0.0;
        final double expectedDedv = 2.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedDt() {
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double dt = 2.0;
        final double expectedE = 0.0;
        final double expectedDedm = 0.0;
        final double expectedDedv = 0.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedM() {
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double m = 2.0;
        final double v = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.25;
        final double expectedDedm = 0.375;
        final double expectedDedv = 1.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedM0() {
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double m0 = 2.0;
        final double v0 = 1.0;
        final double m = 1.0;
        final double v = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = -1.5;
        final double expectedDedv = -1.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedV() {
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 1.0;
        final double m = 1.0;
        final double v = 2.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = 1.5;
        final double expectedDedv = 1.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ClosedV0() {
        final double dedm0 = 0.0;
        final double dedv0 = 0.0;
        final double m0 = 1.0;
        final double v0 = 2.0;
        final double m = 1.0;
        final double v = 1.0;
        final double dt = 1.0;
        final double expectedE = 0.5;
        final double expectedDedm = -1.5;
        final double expectedDedv = -1.0;

        evaluate_1Closed(dedm0, dedv0, m0, v0, m, v, dt, expectedE, expectedDedm, expectedDedv);
    }

    @Test
    public void evaluate_1ForceBase() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.125;
        final double expectedDedm = -0.625;
        final double expectedDedv = -0.5;
        final double expectedDedf = 0.25;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceDedf0() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 1.0;

        final double expectedE = 0.125;
        final double expectedDedm = -0.625;
        final double expectedDedv = -0.5;
        final double expectedDedf = 1.25;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceDt() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 2.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.5;
        final double expectedDedv = -1.0;
        final double expectedDedf = 1.0;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceF() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 2.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.5;
        final double expectedDedv = -1.0;
        final double expectedDedf = 0.5;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceF0() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 1.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.5;
        final double expectedDedm = -1.5;
        final double expectedDedv = -1.0;
        final double expectedDedf = 0.5;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceForceOn() {
        final boolean forceOn = false;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.125;
        final double expectedDedm = 0.375;
        final double expectedDedv = 0.5;
        final double expectedDedf = 0.25;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceM() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 2.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.0625;
        final double expectedDedm = 0.21875;
        final double expectedDedv = 0.5;
        final double expectedDedf = -0.125;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceM0() {
        final boolean forceOn = true;

        final double m0 = 2.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -2.625;
        final double expectedDedv = -1.5;
        final double expectedDedf = 0.75;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceV() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 1.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 2.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 0.125;
        final double expectedDedm = 0.875;
        final double expectedDedv = 0.5;
        final double expectedDedf = -0.25;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

    @Test
    public void evaluate_1ForceV0() {
        final boolean forceOn = true;

        final double m0 = 1.0;
        final double v0 = 2.0;
        final double f0 = 0.0;

        final double m = 1.0;
        final double v = 1.0;
        final double f = 1.0;

        final double dt = 1.0;
        final double dedf0 = 0.0;

        final double expectedE = 1.125;
        final double expectedDedm = -2.625;
        final double expectedDedv = -1.5;
        final double expectedDedf = 0.75;

        evaluate_1Force(forceOn, m0, v0, f0, m, v, f, dt, dedf0, expectedE, expectedDedm, expectedDedv, expectedDedf);
    }

}
