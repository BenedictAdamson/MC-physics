package uk.badamson.mc.physics.solver;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.FunctionNWithGradientValue;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests for the class {@link EnergyErrorFunction}.
 * </p>
 */
public class EnergyErrorFunctionTest {

    private static final class QuadraticTerm1 implements EnergyErrorFunctionTerm {

        private final double xMin;
        private final double eMin;

        QuadraticTerm1(final double xMin, final double eMin) {
            this.xMin = xMin;
            this.eMin = eMin;
        }

        @Override
        public final double evaluate(final double[] dedx, final ImmutableVectorN x) {
            Objects.requireNonNull(dedx, "dsdx");

            final double xr = x.get(0) - xMin;
            dedx[0] += 2.0 * xr;
            return eMin + xr * xr;
        }

        @Override
        public final boolean isValidForDimension(final int n) {
            return n == 1;
        }

    }// class

    private static final class ZeroTerm implements EnergyErrorFunctionTerm {

        @Override
        public double evaluate(final double[] dedx, final ImmutableVectorN x) {
            Objects.requireNonNull(dedx, "dsdx");
            return 0;
        }

        @Override
        public final boolean isValidForDimension(final int n) {
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

    public static void assertInvariants(final EnergyErrorFunction f) {
        ObjectTest.assertInvariants(f);// inherited

        final Collection<EnergyErrorFunctionTerm> terms = f.getTerms();

        assertNotNull(terms, "Always have a collection of terms.");// guard

        for (final EnergyErrorFunctionTerm term : terms) {
            assertNotNull(term, "The collection of terms does not contain any null elements.");// guard
            EnergyErrorFunctionTermTest.assertInvariants(term);
        }
    }

    public static void assertInvariants(final EnergyErrorFunction f1, final EnergyErrorFunction f2) {
        ObjectTest.assertInvariants(f1, f2);// inherited
    }

    private static EnergyErrorFunction constructor(final int dimension, final List<EnergyErrorFunctionTerm> terms) {
        final EnergyErrorFunction f = new EnergyErrorFunction(dimension, terms);

        assertInvariants(f);
        assertEquals(terms, f.getTerms(), "terms");

        return f;
    }

    private static FunctionNWithGradientValue value(final EnergyErrorFunction f, final ImmutableVectorN x) {
        final FunctionNWithGradientValue fx = f.value(x);

        return fx;
    }

    private static void value_0(final ImmutableVectorN x0, final double dt, final ImmutableVectorN x) {
        final int dimension = x.getDimension();
        final List<EnergyErrorFunctionTerm> terms = Collections.emptyList();
        final EnergyErrorFunction f = new EnergyErrorFunction(dimension, terms);

        final FunctionNWithGradientValue fx = f.value(x);

        assertEquals(fx.getF(), 0.0, Double.MIN_NORMAL, "value.f");
        assertEquals(fx.getDfDx().magnitude(), 0.0, Double.MIN_NORMAL, "value.dfDx <" + fx.getDfDx() + "> magnitude");
    }

    private static void value_quadraticTerm(final double x0, final double dt, final double x, final double xMin,
            final double eMin, final double expectedE, final double expectedDeDx) {
        final int dimension = 1;
        final QuadraticTerm1 term = new QuadraticTerm1(xMin, eMin);
        final List<EnergyErrorFunctionTerm> terms = Collections.singletonList(term);
        final EnergyErrorFunction f = new EnergyErrorFunction(dimension, terms);

        final FunctionNWithGradientValue fx = value(f, ImmutableVectorN.create(x));// inherited

        assertEquals(expectedE, fx.getF(), Double.MIN_NORMAL, "value.f");
        assertEquals(expectedDeDx, fx.getDfDx().get(0), Double.MIN_NORMAL, "value.dfDx");
    }

    @Test
    public void constructor_A() {
        final List<EnergyErrorFunctionTerm> terms = Collections.emptyList();
        constructor(1, terms);
    }

    @Test
    public void constructor_B() {
        final List<EnergyErrorFunctionTerm> terms = Collections.singletonList(TERM_0A);
        constructor(1, terms);
    }

    @Test
    public void constructor_C() {
        final List<EnergyErrorFunctionTerm> terms = Arrays.asList(TERM_0A, TERM_0B);
        constructor(1, terms);
    }

    @Test
    public void constructor_D() {
        final List<EnergyErrorFunctionTerm> terms = Arrays.asList(TERM_0A);
        constructor(2, terms);
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
        final double x = 1.0;
        final double xMin = 0.0;
        final double eMin = 0.0;
        final double expectedE = 2.0;
        final double expectedDeDx = 4.0;

        final QuadraticTerm1 term = new QuadraticTerm1(xMin, eMin);
        final List<EnergyErrorFunctionTerm> terms = Arrays.asList(term, term);
        final EnergyErrorFunction f = new EnergyErrorFunction(1, terms);

        final FunctionNWithGradientValue fx = value(f, ImmutableVectorN.create(x));// inherited

        assertEquals(expectedE, fx.getF(), Double.MIN_NORMAL, "value.f");
        assertEquals(expectedDeDx, fx.getDfDx().get(0), Double.MIN_NORMAL, "value.dfDx");
    }
}
