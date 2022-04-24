package com.pungo.chromatorium.game

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow

class Point(val x: Double, val y: Double){
    constructor(x: Float, y: Float): this(x.toDouble(), y.toDouble())
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())
    val offset: Offset
        get() {
            return Offset(x.toFloat(),y.toFloat())
        }

    fun translated(x: Double=0.0, y: Double=0.0): Point {
        return Point(this.x+x,this.y+y)
    }

    fun translated(x: Float = 0f,y: Float = 0f): Point {
        return Point(this.x+x.toDouble(),this.y+y.toDouble())
    }

    fun scale(x: Float, y: Float): Point {
        return Point(this.x*x,this.y*y)
    }

    fun distance(other: Point): Double {
        return kotlin.math.sqrt((other.x - x).pow(2.0) + (other.y - y).pow(2.0))
    }

    fun length(): Double {
        return kotlin.math.sqrt((x).pow(2.0) + (y).pow(2.0))
    }
}