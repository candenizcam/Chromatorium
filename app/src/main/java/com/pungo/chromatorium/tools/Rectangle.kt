package com.pungo.chromatorium.tools

import android.util.Size

class Rectangle(val x: Double, val y: Double, val w: Double, val h: Double) {
    constructor(topLeft: Point, w: Double, h: Double): this(topLeft.x, topLeft.y, w, h)



    val centre: Point
        get() {
            return Point(x + w/2.0,y + h/2.0)
        }

    val topLeft: Point
        get() {
            return Point(x,y)
        }

    val bottom: Double
    get() {
        return y + h
    }

    val right: Double
    get() {
        return x + w
    }




    companion object{
        fun fromIntString(s: String): Rectangle {
            val (x,y,w,h) = s.split(",").map { it.toDouble() }
            return Rectangle(x,y,w,h)
        }
    }
}