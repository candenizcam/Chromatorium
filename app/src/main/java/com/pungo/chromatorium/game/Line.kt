package com.pungo.chromatorium.game

import kotlin.math.sign

class Line(val p1: Point, val p2: Point) {
    val a: Double
    val b: Double
    val special: Line.Special
    init {
        if(p1.y - p2.y == 0.0){
            special = Special.HORIZONTAL
        }else if(p1.x - p2.x == 0.0){
            special = Special.VERTICAL
        }else{
            special= Special.NONE
        }



        val d  = if(p1.x - p2.x == 0.0){

            0.000001
        }else{
            (p1.x - p2.x)
        }
        a = (p1.y - p2.y)/d
        b = p1.y- a*p1.x
    }

    fun yFromX(x: Double): Double {
        return when(special){
            Special.NONE->a*x + b
            Special.VERTICAL->a*x + b
            Special.HORIZONTAL->b
        }

    }

    fun xFromY(y: Double): Double{
        return when(special){
            Special.NONE->(y-b)/a
            Special.VERTICAL->(y-b)/a
            Special.HORIZONTAL->(y-b)/a
        }
    }

    fun pointFromX(x: Double): Point{
        return Point(x,yFromX(x))
    }

    fun pointFromY(y: Double): Point{
        return Point(xFromY(y),y)
    }

    fun length(): Double {
        return p1.distance(p2)
    }



    fun closestPointAndDistancePair(p: Point): Pair<Point, Double> {
        val cp  =closestPointOnTheLine(p)
        return Pair(cp,cp.distance(p))
    }

    /** min of |l_p-p|
     *
     */
    fun closestDistance(p: Point): Double {
        return closestPointOnTheLine(p).distance(p)
    }

    /** argmin of |l_p-p|
     *
     */
    fun closestPointOnTheLine(p: Point): Point {
        val k = a
        val l = -1
        val m= b

        val x =  (l*(l*p.x - k*p.y) - k*m)/(k*k + l*l)

        //val x = (p.x+p.y - a*b)/(1 + a*a)
        val closestPoint = when(special){
            Special.NONE->pointFromX(x)
            Special.VERTICAL->Point(p1.x,p.y)
            Special.HORIZONTAL->Point(p.x,b)
        }



        val p1Dist = p1.distance(closestPoint)
        val p2Dist = p2.distance(closestPoint)

        val pl = p1Dist+p2Dist
        val leng = length()
        return if(p1Dist+p2Dist<=length()){
            closestPoint
        }else{
            if(p1Dist<p2Dist){
                p1
            }else{
                p2
            }
        }


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

    enum class Special{
        NONE {
    },VERTICAL {
    },HORIZONTAL {
    };

    }



}