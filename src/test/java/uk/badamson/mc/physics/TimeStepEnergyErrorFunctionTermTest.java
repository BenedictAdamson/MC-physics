package uk.badamson.mc.physics;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests classes that implement the interface
 * {@link TimeStepEnergyErrorFunctionTerm}.
 * </p>
 */
public class TimeStepEnergyErrorFunctionTermTest {

    public static void assertInvariants(final TimeStepEnergyErrorFunctionTerm t) {
        ObjectTest.assertInvariants(t);// inherited
    }

    public static void assertInvariants(final TimeStepEnergyErrorFunctionTerm t1,
            final TimeStepEnergyErrorFunctionTerm t2) {
        ObjectTest.assertInvariants(t1, t2);// inherited
    }

    public static double evaluate(final TimeStepEnergyErrorFunctionTerm term, final double[] dedx,
            final ImmutableVectorN x0, final ImmutableVectorN x, final double dt) {
        final double e = term.evaluate(dedx, x0, x, dt);

        assertInvariants(term);

        return e;
    }
}