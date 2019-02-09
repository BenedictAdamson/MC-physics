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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.mc.math.ImmutableVector3;
import uk.badamson.mc.physics.HarmonicVector3;
import uk.badamson.mc.physics.HarmonicVector3Test;

/**
 * <p>
 * Unit tests for the class {@link HarmonicVector3EnergyErrorFunctionTerms}
 * </p>
 */
public class HarmonicVector3EnergyErrorFunctionTermsTest {

    @Nested
    public class Zero {

        @Test
        public void a() {
            apply(v1);
        }

        private void apply(final HarmonicVector3 v) {
            final var result = HarmonicVector3EnergyErrorFunctionTermsTest
                    .apply(HarmonicVector3EnergyErrorFunctionTerms.ZERO, v);

            assertSame(HarmonicVector3EnergyErrorValueAndGradients.ZERO, result, "always zero.");
        }

        @Test
        public void b() {
            apply(v1);
        }
    }// class

    private static HarmonicVector3 v1;

    private static HarmonicVector3 v2;

    private static HarmonicVector3EnergyErrorValueAndGradients apply(
            final Function<HarmonicVector3, HarmonicVector3EnergyErrorValueAndGradients> f, final HarmonicVector3 v) {
        final var result = f.apply(v);

        assertNotNull(result, "Not null, result");
        HarmonicVector3Test.assertInvariants(v);// check for side effects
        HarmonicVector3EnergyErrorValueAndGradientsTest.assertInvariants(result);

        return result;
    }

    @BeforeAll
    public static void setUp() {
        v1 = new HarmonicVector3(Duration.ofSeconds(2), ImmutableVector3.J, ImmutableVector3.K, ImmutableVector3.I,
                ImmutableVector3.J, ImmutableVector3.K, 3, 4);
        v2 = new HarmonicVector3(Duration.ofSeconds(3), ImmutableVector3.K, ImmutableVector3.I, ImmutableVector3.J,
                ImmutableVector3.K, ImmutableVector3.I, 5, 7);
    }
}
