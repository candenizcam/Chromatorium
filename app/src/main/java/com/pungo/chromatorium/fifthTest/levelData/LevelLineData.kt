package com.pungo.chromatorium.fifthTest.levelData

import com.pungo.chromatorium.tools.Line
import com.pungo.chromatorium.tools.Point

class LevelLineData(val fromId: String, val toId: String, val points:  List<Point>, val allPoints: List<Point>){
    fun getLines(): List<Line> {
        return (1 until points.size).map {
            Line(points[it-1],points[it])
        }
    }
}