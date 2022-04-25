package com.pungo.chromatorium.game

class LevelItem(val shape: Shape, var chroma: Chroma, val r: Int, val c: Int, val radius: Float, val hitBox: Float, var mutable: Boolean, var targetColour: Chroma? = null){
    fun hitContains(x: Float, y: Float): Boolean {
        return Point(x-(c-0.5),y-(r-0.5)).length()>=hitBox
    }

    fun hitContains(x: Double, y: Double): Boolean {
        return Point(x-(c-0.5),y-(r-0.5)).length()<=hitBox
    }

    fun hitContains(p: Point): Boolean{
        return hitContains(p.x,p.y)
    }

    val gridCoordinatesCentre: Point
        get() {
            return Point(c-0.5,r-0.5)
        }


}