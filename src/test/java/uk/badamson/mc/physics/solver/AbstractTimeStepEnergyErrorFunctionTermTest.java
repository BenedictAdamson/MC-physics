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
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import uk.badamson.mc.ObjectTest;
import uk.badamson.mc.math.ImmutableVectorN;

/**
 * <p>
 * Unit tests for classes derived from
 * {@link AbstractTimeStepEnergyErrorFunctionTerm}.
 * </p>
 */
public class AbstractTimeStepEnergyErrorFunctionTermTest {

    private static class IsFinite extends TypeSafeMatcher<Double> {

        @Override
        public void describeMismatchSafely(final Double item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" is not finite");
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a finite value");
        }

        @Override
        public boolean matchesSafely(final Double item) {
            return item != null && Double.isFinite(item.doubleValue());
        }
    }// class

    private static final IsFinite IS_FINITE = new IsFinite();

    public static void assertInvariants(final AbstractTimeStepEnergyErrorFunctionTerm term) {
        ObjectTest.assertInvariants(term);// inherited
        TimeStepEnergyErrorFunctionTermTest.assertInvariants(term);// inherited
    }

    public static void assertInvariants(final AbstractTimeStepEnergyErrorFunctionTerm term1,
            final AbstractTimeStepEnergyErrorFunctionTerm term2) {
        ObjectTest.assertInvariants(term1, term2);// inherited
        TimeStepEnergyErrorFunctionTermTest.assertInvariants(term1, term2);// inherited
    }

    public static void assertIsReferenceScale(final String name, final double scale) {
        assertThat(name, Double.valueOf(scale), allOf(greaterThan(Double.valueOf(0.0)), IS_FINITE));
    }

    public static double evaluate(final AbstractTimeStepEnergyErrorFunctionTerm term, final double[] dedx,
            final ImmutableVectorN state0, final ImmutableVectorN state, final double dt) {
        final double e = TimeStepEnergyErrorFunctionTermTest.evaluate(term, dedx, state0, state, dt);

        assertInvariants(term);

        return e;
    }
}
