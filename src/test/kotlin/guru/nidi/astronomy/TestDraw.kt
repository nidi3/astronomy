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

import java.time.ZoneId
import java.time.ZonedDateTime

fun main(args: Array<String>) {
    val draw = Draw(80, 30)
    var date = ZonedDateTime.of(2017, 6, 15, 12, 0, 0, 0, ZoneId.of("CET"))
    for (i in 0..10000) {
        draw.sun(date, 45.0, 6.0)
        Thread.sleep(30)
        date = date.plusMinutes(30)
    }
}