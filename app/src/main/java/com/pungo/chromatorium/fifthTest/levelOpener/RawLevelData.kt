package com.pungo.chromatorium.fifthTest.levelOpener

import com.pungo.chromatorium.fifthTest.levelData.*
import kotlin.math.max
import kotlin.math.min

class RawLevelData(ellipseLine: String, lineLine: String){
    val levelNo: String
    val left: Int
    val top: Int
    val right: Int
    val bottom: Int
    val width: Int
    val height: Int
    val theseEllipses: List<RawEllipseData>
    val theseLines: List<RawLineData>
    init {
        theseEllipses = ellipseLine.split(";").map { RawEllipseData(it) }
        theseLines = lineLine.split(";").map { RawLineData(it) }
        levelNo = (100 - theseEllipses[0].opacity.toInt()).toString()

        val padding = theseEllipses[0].text_height.toInt()

        left = min(theseEllipses.minOf { it.left }, theseLines.minOf { it.left }) - padding*3
        top = min(theseEllipses.minOf { it.top }, theseLines.minOf { it.top  }) - padding*3
        right = max(theseEllipses.maxOf { it.right  }, theseLines.maxOf { it.right  }) + padding*3
        bottom = max(theseEllipses.maxOf { it.bottom }, theseLines.maxOf { it.bottom }) + padding*3

        width = right-left
        height = bottom-top
    }

    fun getLevelEllipses(): List<LevelEllipseData> {
        return theseEllipses.map { it.getLevelEllipse(left, top, width,height) }
    }

    fun getLevelLines(): List<LevelLineData>{
        return theseLines.map { it.getLevelLine(left, top, width,height) }
    }

    /** Returns empty list if there is nothing
     *
     */
    fun getDecorEllipses(left: Int, top: Int, width: Int, height: Int): List<DecorEllipseData> {
        if(this.right<left || this.bottom < top || this.left> left+width || this.top> top+height){
            // level size don't overlap, automatic fail
            return listOf()
        }

        return theseEllipses.mapNotNull { it.getDecorEllipse(left, top, width, height) }
    }

    fun getDecorLines(left: Int, top: Int, width: Int, height: Int): List<DecorLineData> {
        return theseLines.mapNotNull { it.getDecorLine(left, top, width, height) }
    }

}