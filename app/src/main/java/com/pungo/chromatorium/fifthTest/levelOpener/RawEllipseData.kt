package com.pungo.chromatorium.fifthTest.levelOpener

import com.pungo.chromatorium.fifthTest.levelData.DecorEllipseData
import com.pungo.chromatorium.fifthTest.levelData.LevelData
import com.pungo.chromatorium.fifthTest.levelData.LevelEllipseData
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Rectangle
import kotlin.math.max
import kotlin.math.min


class RawEllipseData(s: String){
    // ellipse: ell.id:fill_colour,stroke_colour:opacity:stroke_width:centre_x,centre_y,diameter:top,left,width,height
    val id: String
    val fillColour: String
    val strokeColour: String
    val opacity: String
    val strokeWidth: String
    val centre_x: String
    val centre_y: String
    val diametre: String
    val text_left: String
    val text_top: String
    val text_width: String
    val text_height: String
    init {
        val v = s.split(":")
        id = v[0].split(".")[1]
        v[1].split(",").also {
            fillColour = it[0]
            strokeColour = it[1]
        }
        opacity = v[2]
        strokeWidth = v[3]
        v[4].split(",").also {
            centre_x = it[0]
            centre_y = it[1]
            diametre = it[2]
        }
        v[5].split(",").also {
            text_left = it[0]
            text_top = it[1]
            text_width = it[2]
            text_height = it[3]
        }


    }

    fun getDecorEllipse(left: Int, top: Int, width: Int, height: Int): DecorEllipseData? {
        if(this.right<left || this.left> left+height || this.bottom < top || this.bottom> top+height){
            // outside the level area
            return null
        }

        val adjusted_centre_x = (centre_x.toDouble() - left.toDouble())/width.toDouble()
        val adjusted_centre_y = (centre_y.toDouble() - top.toDouble())/height.toDouble()
        return DecorEllipseData(
            Point(adjusted_centre_x, adjusted_centre_y),
            diametre.toDouble() / width.toDouble()
        )
    }

    fun getLevelEllipse(left: Int, top: Int, width: Int, height: Int): LevelEllipseData {
        val doubleWidth = width.toDouble()
        val doubleHeight = height.toDouble()
        val adjusted_centre_x = (centre_x.toDouble() - left.toDouble())/doubleWidth
        val adjusted_centre_y = (centre_y.toDouble() - top.toDouble())/doubleHeight
        val textRect_x = (text_left.toDouble() - left.toDouble())/doubleWidth
        val textRect_y = (text_top.toDouble() - top.toDouble())/doubleHeight
        val textWidth = text_width.toDouble()/doubleWidth
        val textHeight = text_height.toDouble()/doubleHeight

        val textRect = Rectangle(textRect_x,textRect_y,textWidth,textHeight)


        return LevelEllipseData(
            id = this.id,
            nodeType = NodeType.fromString(strokeWidth),
            fillColour = this.fillColour,
            centre = Point(adjusted_centre_x, adjusted_centre_y),
            diametre = diametre.toDouble() / width.toDouble(),
            textRect = textRect
        )
    }

    val left: Int
        get() {
            val c = centre_x.toInt() - diametre.toInt()/2
            val h = text_left.toInt()
            return min(c,h)
        }

    val top: Int
        get() {
            val c = centre_y.toInt() - diametre.toInt()/2
            val h = text_top.toInt()
            return min(c,h)
        }

    val right: Int
        get() {
            val c = centre_x.toInt() + diametre.toInt()/2
            val h = text_left.toInt() + text_width.toInt()
            return max(c,h)
        }

    val bottom: Int
        get() {
            val c = centre_y.toInt() + diametre.toInt()/2
            val h = text_top.toInt() + text_height.toInt()
            return max(c,h)
        }
}