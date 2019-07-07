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

import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import java.time.ZonedDateTime

class Draw(val width: Int, val height: Int) {
    private var sun = Coord(0, 0)
    private var cursor = Coord(0, 0)

    init {
        AnsiConsole.systemInstall()

        val semiLine = "-".repeat((width - 4) / 8)
        val semiNix = " ".repeat((width - 4) / 8)
        val line = "-".repeat((width - 4) / 4)
        val nix = " ".repeat((width - 4) / 4)
        println("----$line$line$line$line")
        (1..height).forEach { println() }
        println("$semiLine+$line+$line+$line+$semiLine")
        println("${semiNix}N${nix}O${nix}S${nix}W")
    }

    fun sun(date: ZonedDateTime, latitude: Double, longitude: Double) {
        val (el, az) = Sun.positionAt(date, latitude, longitude)
        if (sun.y > 2) print(sun, " ")
        sun = Coord((el * height / 90 + 2).toInt(), width - ((az + 675) % 360 * width / 360).toInt())
        if (sun.y > 2) print(sun, "o")
        print(Coord(0, 10), "$date $az")
        System.out.flush()
    }

    private fun print(c: Coord, s: String) {
        print(ansi().apply {
            if (c.y > cursor.y) cursorUp(c.y - cursor.y) else if (c.y < cursor.y) cursorDown(cursor.y - c.y)
            if (c.x > cursor.x) cursorRight(c.x - cursor.x) else if (c.x < cursor.x) cursorLeft(cursor.x - c.x)
        })
        print(s)
        cursor = Coord(c.y, c.x + s.length)
    }
}

private data class Coord(val y: Int, val x: Int)