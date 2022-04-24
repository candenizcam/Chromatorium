package com.pungo.chromatorium.game

import kotlin.math.sign

class Line(val p1: Point, val p2: Point) {
    val a: Double
    val b: Double
    init {
        a = (p1.y - p2.y)/(p1.x - p2.x)
        b = p1.y- a*p1.x
    }

    fun yFromX(x: Double): Double {
        return a*x + b
    }

    fun xFromY(y: Double): Double{
        return (y-b)/a
    }

    fun pointFromX(x: Double): Point{
        return Point(x,yFromX(x))
    }

    fun pointFromY(y: Double): Point{
        return Point(xFromY(y),y)
    }

    /** Returns true if the points are on opposite sides of the line, false otherwise (false if point is on the direction)
     * this function does not look at the boundaries, only the direction
     */
    fun splitsPoints(p1: Point, p2: Point): Boolean {
        val yd1 = yFromX(p1.x) - p1.y
        val yd2 = yFromX(p2.x) - p2.y
        val s1 = sign(yFromX(p1.x) - p1.y)
        val s2 = sign(yFromX(p2.x) - p2.y)
        if((s1==0.0).or(s2==0.0)){
            return false
        }
        return s1!=s2
    }

    fun intersect(other: Line): Boolean {
        val b1 = other.splitsPoints(p1,p2)
        val b2 = splitsPoints(other.p1,other.p2)
        return b1.and(b2)
    }
}