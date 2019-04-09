# Dockerfile for the use in the Jenkinsfile for the MC-physics project,
# to set up the build environment for Jenkins to use.

# © Copyright Benedict Adamson 201819.
# 
# This file is part of MC-physics.
#
# MC-physics is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# MC-physics is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with MC-physics.  If not, see <https://www.gnu.org/licenses/>.
#

FROM debian:stretch-backports
RUN apt-get -y update && apt-get -y install \
   maven \
   openjdk-11-jdk-headless
   