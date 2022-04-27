package com.pungo.chromatorium.game

/** This class holds information about objects in a level
 * shape: a class that is used to draw the item (has a method for path), won't change in level
 * chroma: a class that holds information about colour, and its associated needs will change in level
 * r & c: row and column of the item in the grid, this class assumes each item is snapped to the grid (overlapping is not checked on this level)
 * radius: the radius that the shape will be drawn, given as a grid size float, that is 1f means side of a single square in a grid
 * hitBox: the click distance applicable, given in grid size like radius
 * mutable: if true, the colour of this level item can be changed
 * targetColor: if not null, this item must be painted to the given chroma in order for the episode to be complete
 * something may be added here if we have bonus target colours
 */
class LevelItem(val shape: Shape, var chroma: Chroma, val r: Int, val c: Int, val radius: Float, val hitBox: Float, var mutable: Boolean, var targetColour: Chroma? = null){
    fun hitContains(x: Float, y: Float): Boolean {
        return Point(x-(c-0.5),y-(r-0.5)).length()<=hitBox
    }

    fun hitContains(x: Double, y: Double): Boolean {
        val  l = Point(x-(c-0.5),y-(r-0.5)).length()
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