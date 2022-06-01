package com.pungo.chromatorium.fifthTest.levelOpener

import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Rectangle
import com.pungo.chromatorium.tools.Size


class LevelData(val levelNo: String, val width: Int, val height: Int, val levelEllipses: List<LevelEllipseData>, val decorEllipses: List<DecorEllipseData>, val levelLines: List<LevelLineData>, val decorLines: List<DecorLineData>){

    val levelSize: Size
    get() {
        return Size(width,height)
    }

    class LevelLineData(val fromId: String, val toId: String, val points:  List<Point>)

    class DecorLineData(val fromId: String, val toId: String, val points:  List<Point>)

    class LevelEllipseData(val id: String, val strokeWidth: String, val fillColour: String, val centre: Point, val diametre: Double, val textRect: Rectangle)

    class DecorEllipseData(val centre: Point, val diametre: Double)
}

