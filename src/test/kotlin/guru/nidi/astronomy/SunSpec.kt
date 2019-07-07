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

import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class SunSpec : StringSpec() {
    init {
        "sun" {
            val values = table(
                    headers("date", "lat", "lng", "result"),
                    row(ZonedDateTime.of(2006, 11, 15, 10, 35, 0, 0, ZoneOffset.UTC), 48.8166666, 2.2897222, Pair(21.5, 164.5)),
                    row(ZonedDateTime.of(2017, 8, 15, 20, 30, 0, 0, ZoneId.of("CET")), 48.0, 7.0, Pair(1.8, 70.8)),
                    row(ZonedDateTime.of(2017, 6, 21, 0, 0, 0, 0, ZoneId.of("CET")), 70.0, 19.0, Pair(3.8, 10.4)),
                    row(ZonedDateTime.of(2017, 3, 21, 12, 0, 0, 0, ZoneOffset.UTC), 0.0, 0.0, Pair(88.1, 86.7))
            )
            forAll(values) { date, lat, lng, (el, az) ->
                val (elevation, azimuth) = Sun.positionAt(date, lat, lng)
                elevation shouldEqual (el plusOrMinus .1)
                azimuth shouldEqual (az plusOrMinus .1)
            }
        }
    }
}