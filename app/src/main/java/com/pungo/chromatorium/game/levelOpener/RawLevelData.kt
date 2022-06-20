package com.pungo.chromatorium.game.levelOpener

import com.pungo.chromatorium.game.levelData.*
import kotlin.math.max
import kotlin.math.min

class RawLevelData(levelName: String, ellipseLine: String, lineLine: String, paddingCoeff: Float = 9.0f/4.0f){
    val levelNo: String
    val left: Int
    val top: Int
    val right: Int
    val bottom: Int
    val width: Int
    val height: Int
    val levelTitle: String
    val star3: Int
    val star2: Int
    val theseEllipses: List<RawEllipseData>
    val theseLines: List<RawLineData>
    init {


        val ln = levelName.split(":")
        val (title, s3, s2) = (1..3).map{i->
            try{
                ln[i]
            }catch(e: Exception) {
                "-1"
            }
        }
        levelTitle = title
        star3 = s3.toInt()
        star2 = s2.toInt()


        theseEllipses = ellipseLine.split(";").map { RawEllipseData(it) }
        theseLines = lineLine.split(";").map { RawLineData(it) }
        levelNo = (100 - theseEllipses[0].opacity.toInt()).toString()

        val padding = (theseEllipses[0].text_height.toInt() * paddingCoeff).toInt()

        left = min(theseEllipses.minOf { it.left }, theseLines.minOf { it.left }) - padding
        top = min(theseEllipses.minOf { it.top }, theseLines.minOf { it.top  }) - padding
        right = max(theseEllipses.maxOf { it.right  }, theseLines.maxOf { it.right  }) + padding
        bottom = max(theseEllipses.maxOf { it.bottom }, theseLines.maxOf { it.bottom }) + padding

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