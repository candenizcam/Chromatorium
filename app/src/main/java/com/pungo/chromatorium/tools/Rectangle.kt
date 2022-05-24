package com.pungo.chromatorium.tools

class Rectangle(val x: Double, val y: Double, val w: Double, val h: Double) {
    constructor(topLeft: Point, w: Double, h: Double): this(topLeft.x, topLeft.y, w, h)


    val centre: Point
        get() {
            return Point(x + w/2.0,y + h/2.0)
        }


    companion object{
        fun fromIntString(s: String): Rectangle {
            val (x,y,w,h) = s.split(",").map { it.toDouble() }
            return Rectangle(x,y,w,h)
        }
    }
}