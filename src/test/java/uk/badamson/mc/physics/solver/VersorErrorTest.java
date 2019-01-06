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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVectorN;
import uk.badamson.mc.math.Quaternion;
import uk.badamson.mc.physics.solver.mapper.QuaternionStateSpaceMapper;

/**
 * <p>
 * Units tests for the class {@link VersorError}.
 * </p>
 */
public class VersorErrorTest {

    private static final double SMALL = 1E-3;

    private static final double LENGTH_1 = 1.0;
    private static final double LENGTH_2 = 1E7;

    private static final double MASS_1 = 1.0;
    private static final double MASS_2 = 1E24;

    private static final double DT_1 = 1.0;
    private static final double DT_2 = 1E-3;

    public static void assertInvariants(final VersorError term) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term);// inherited

        final double mass = term.getMass();
        final double length = term.getLength();
        final QuaternionStateSpaceMapper quaternionMapper = term.getQuaternionMapper();

        assertNotNull(quaternionMapper, "quaternionMapper");// guard

        AbstractTimeStepEnergyErrorFunctionTermTest.assertIsReferenceScale("mass", mass);
        AbstractTimeStepEnergyErrorFunctionTermTest.assertIsReferenceScale("length", length);
    }

    public static void assertInvariants(final VersorError term1, final VersorError term2) {
        AbstractTimeStepEnergyErrorFunctionTermTest.assertInvariants(term1, term2);// inherited
    }

    private static VersorError constructor(final double length, final double mass,
            final QuaternionStateSpaceMapper quaternionMapper) {
        final VersorError term = new VersorError(length, mass, quaternionMapper);

        assertInvariants(term);

        assertEquals(length, term.getLength(), Double.MIN_NORMAL, "length");
        assertEquals(mass, term.getMass(), Double.MIN_NORMAL, "mass");
        assertSame(quaternionMapper, term.getQuaternionMapper(), "quaternionMapper");

        return term;
    }

    private static double evaluate(final VersorError term, final double[] dedx, final ImmutableVectorN x0,
            final ImmutableVectorN x, final double dt) {
        final double e = AbstractTimeStepEnergyErrorFunctionTermTest.evaluate(term, dedx, x0, x, dt);

        assertInvariants(term);

        return e;
    }

    private static void evaluate_smallError(final double length, final double mass, final Quaternion versor,
            final Quaternion dq, final double dt) {
        final Quaternion q = versor.plus(dq);
        final double qe = q.norm() - 1.0;
        final double le = qe * length;
        final double ve = le / dt;
        final double eExpected = 0.5 * mass * ve * ve;
        final double deda2 = -mass * ve * length / dt;
        final double eTolerance = tolerance(eExpected) * 5.0;
        final double dedxTolerance = 1E-6;

        final QuaternionStateSpaceMapper quaternionMapper = new QuaternionStateSpaceMapper(0);
        final VersorError term = new VersorError(length, mass, quaternionMapper);
        final double[] dedx = new double[4];
        final ImmutableVectorN x0 = ImmutableVectorN.create0(4);
        final ImmutableVectorN x = ImmutableVectorN.create(q.getA(), q.getB(), q.getC(), q.getD());

        final double e = evaluate(term, dedx, x0, x, dt);

        assertInvariants(term);

        assertThat("energy error", Double.valueOf(e), closeTo(eExpected, eTolerance));
        assertThat("dedex[0]", Double.valueOf(dedx[0]), closeTo(deda2 * versor.getA(), dedxTolerance));
        assertThat("dedex[1]", Double.valueOf(dedx[1]), closeTo(deda2 * versor.getB(), dedxTolerance));
        assertThat("dedex[2]", Double.valueOf(dedx[2]), closeTo(deda2 * versor.getC(), dedxTolerance));
        assertThat("dedex[3]", Double.valueOf(dedx[3]), closeTo(deda2 * versor.getD(), dedxTolerance));
    }

    private static void evaluate_versor(final double length, final double mass, final Quaternion versor,
            final double dt) {
        final QuaternionStateSpaceMapper quaternionMapper = new QuaternionStateSpaceMapper(0);
        final VersorError term = new VersorError(length, mass, quaternionMapper);
        final double[] dedx = new double[4];
        final ImmutableVectorN x0 = ImmutableVectorN.create0(4);
        final ImmutableVectorN x = ImmutableVectorN.create(versor.getA(), versor.getB(), versor.getC(), versor.getD());

        final double e = evaluate(term, dedx, x0, x, dt);

        assertInvariants(term);

        assertThat("energy error", Double.valueOf(e), closeTo(0, Double.MIN_NORMAL));
        assertThat("dedex[0]", Double.valueOf(dedx[0]), closeTo(0, Double.MIN_NORMAL));
        assertThat("dedex[1]", Double.valueOf(dedx[1]), closeTo(0, Double.MIN_NORMAL));
        assertThat("dedex[2]", Double.valueOf(dedx[2]), closeTo(0, Double.MIN_NORMAL));
        assertThat("dedex[3]", Double.valueOf(dedx[3]), closeTo(0, Double.MIN_NORMAL));
    }

    private static final double tolerance(final double expected) {
        final double a = Math.max(1.0, Math.abs(expected));
        return Math.nextAfter(a, Double.POSITIVE_INFINITY) - a;
    }

    @Test
    public void constructor_A() {
        constructor(LENGTH_1, MASS_1, new QuaternionStateSpaceMapper(0));
    }

    @Test
    public void constructor_B() {
        constructor(LENGTH_2, MASS_2, new QuaternionStateSpaceMapper(1));
    }

    @Test
    public void evaluate_smallError_11A() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.ONE.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_11B() {
        evaluate_smallError(LENGTH_2, MASS_1, Quaternion.ONE, Quaternion.ONE.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_11C() {
        evaluate_smallError(LENGTH_1, MASS_2, Quaternion.ONE, Quaternion.ONE.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_11D() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.ONE.scale(1E-6), DT_1);
    }

    @Test
    public void evaluate_smallError_11E() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.ONE.scale(SMALL), DT_2);
    }

    @Test
    public void evaluate_smallError_11Smaller() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.ONE.scale(-SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_1i() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.I.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_1j() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.J.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_1k() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.ONE, Quaternion.K.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_ii() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.I, Quaternion.I.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_jj() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.J, Quaternion.J.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_smallError_kk() {
        evaluate_smallError(LENGTH_1, MASS_1, Quaternion.K, Quaternion.K.scale(SMALL), DT_1);
    }

    @Test
    public void evaluate_versor_1a() {
        evaluate_versor(LENGTH_1, MASS_1, Quaternion.ONE, DT_1);
    }

    @Test
    public void evaluate_versor_1b() {
        evaluate_versor(LENGTH_2, MASS_2, Quaternion.ONE, DT_2);
    }

    @Test
    public void evaluate_versor_i() {
        evaluate_versor(LENGTH_1, MASS_1, Quaternion.I, DT_1);
    }

    @Test
    public void evaluate_versor_j() {
        evaluate_versor(LENGTH_1, MASS_1, Quaternion.J, DT_1);
    }

    @Test
    public void evaluate_versor_k() {
        evaluate_versor(LENGTH_1, MASS_1, Quaternion.K, DT_1);
    }
}
