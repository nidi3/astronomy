/*
 * Copyright © 2017 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.astronomy

import java.lang.Math.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

val JULIAN_YEAR_DAYS = 365.25
val DAY_SECS = 24 * 60 * 60
val TAU = 2 * PI

object Sun {
    fun positionAt(date: ZonedDateTime, latitude: Double, longitude: Double): Pair<Double, Double> {
        val lat = toRadians(latitude)
        val janurayFirst = ZonedDateTime.of(LocalDateTime.of(date.year, 1, 1, 0, 0), date.zone)
        val fracYearRad = TAU * (janurayFirst.until(date, ChronoUnit.SECONDS)) / DAY_SECS / JULIAN_YEAR_DAYS
        val declination = toRadians(0.396372 +
                -22.91327 * cos(fracYearRad) + 4.02543 * sin(fracYearRad) +
                -0.387205 * cos(2 * fracYearRad) + 0.051967 * sin(2 * fracYearRad) +
                -0.154527 * cos(3 * fracYearRad) + 0.084798 * sin(3 * fracYearRad))
        val timeCorrect = 0.004297 +
                0.107029 * cos(fracYearRad) - 1.837877 * sin(fracYearRad) +
                -0.837378 * cos(2 * fracYearRad) - 2.340475 * sin(2 * fracYearRad)

        val utc = date.withZoneSameInstant(ZoneOffset.UTC)
        val hourAngle = normLongitude(toRadians((utc.hour + utc.minute / 60.toDouble() - 12) * 15 + longitude + timeCorrect))
        val zenith = acos(satUnit(sin(lat) * sin(declination) + cos(lat) * cos(declination) * cos(hourAngle)))
        val azimuth = acos((sin(declination) - sin(lat) * cos(zenith)) / (cos(lat) * sin(zenith)))
        return Pair(90.0 - toDegrees(zenith), toDegrees(if (hourAngle < 0) -azimuth else azimuth))
    }
}

fun normLongitude(longitude: Double) = if (longitude < -PI) longitude + TAU else if (longitude > PI) longitude - TAU else longitude
fun satUnit(u: Double) = if (u < -1) -1.0 else if (u > 1) 1.0 else u