/*
 * © Copyright Benedict Adamson 2018.
 *
 * This file is part of MC-physics.
 *
 * MC-physics is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * MC-physics is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * MC-physics. If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * <p>
 * A physics engine suitable for parallel discrete event simulation.
 * </p>
 */
module uk.badamson.mc.physics {
    exports uk.badamson.mc.physics;

    requires com.github.spotbugs.annotations;
    requires uk.badamson.mc.math;

    // Automatic modules:
    requires jcip.annotations;
}