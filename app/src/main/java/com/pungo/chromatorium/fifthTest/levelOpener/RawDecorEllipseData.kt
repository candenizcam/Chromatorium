package com.pungo.chromatorium.fifthTest.levelOpener


import com.pungo.chromatorium.tools.Point

class RawDecorEllipseData(s: String){
    val id: String
    val fillColour: String
    val centreX: String
    val centreY: String
    val diametre: String
    init {
        // decor: decor.id:fill_colour:opacity:centre_x,centre_y,diameter
        val v = s.split(":")
        id = v[0].split(".")[1]
        fillColour = v[1]
        v[3].split(",").also {
            centreX = it[0]
            centreY = it[1]
            diametre = it[2]
        }


    }

    fun getDecorEllipseData(left: Int, top: Int, width: Int, height: Int): LevelData.DecorEllipseData? {
        return if (this.right<left || this.bottom < top || this.left> left+width || this.top> top+height){
            null
        }else{
            val adjusted_centre_x = (centreX.toDouble() - left.toDouble())/width.toDouble()
            val adjusted_centre_y = (centreY.toDouble() - top.toDouble())/height.toDouble()
            LevelData.DecorEllipseData(
                Point(adjusted_centre_x, adjusted_centre_y),
                diametre.toDouble() / width.toDouble()
            )
        }
    }


    val left: Int
        get() {
            val c = centreX.toInt() - diametre.toInt()/2
            return c
        }

    val top: Int
        get() {
            val c = centreY.toInt() - diametre.toInt()/2
            return c
        }

    val right: Int
        get() {
            val c = centreX.toInt() + diametre.toInt()/2
            return c
        }

    val bottom: Int
        get() {
            val c = centreY.toInt() + diametre.toInt()/2
            return c
        }
}