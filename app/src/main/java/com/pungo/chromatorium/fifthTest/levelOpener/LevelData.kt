package com.pungo.chromatorium.fifthTest.levelOpener

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScopeMarker
import com.pungo.chromatorium.tools.Line
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Rectangle
import com.pungo.chromatorium.tools.Size


class LevelData(val levelNo: String, val width: Int, val height: Int, val levelEllipses: List<LevelEllipseData>, val decorEllipses: List<DecorEllipseData>, val levelLines: List<LevelLineData>, val decorLines: List<DecorLineData>){

    val levelSize: Size
    get() {
        return Size(width,height)
    }

    class LevelLineData(val fromId: String, val toId: String, val points:  List<Point>, val allPoints: List<Point>){
        fun getLines(): List<Line> {
            return (1 until points.size).map {
                Line(points[it-1],points[it])
            }
        }
    }

    class DecorLineData(val fromId: String, val toId: String, val points:  List<Point>)

    class LevelEllipseData(val id: String, val nodeType: NodeType, val fillColour: String, val centre: Point, val diametre: Double, val textRect: Rectangle)

    class DecorEllipseData(val centre: Point, val diametre: Double)


    fun closestEllipseOrNull(p: Point): LevelEllipseData? {
        return levelEllipses.firstOrNull {
            it.centre.distance(p)<0.1
        }
    }

    fun closestLineOrNull(p: Point, cut: Double=0.1): LevelLineData? {

        val d = levelLines.map { it.getLines() }

        val distances = levelLines.map {
            it.getLines().minOf { it.closestDistance(p) }
        }

        val smallestIndex = distances.withIndex().minByOrNull { (index, f) -> f }?.index

        return if( smallestIndex==null){
            null
        }else{
            if (distances[smallestIndex]<cut){
                levelLines[smallestIndex]
            }else{
                null
            }
        }



    }






    fun drawDecor(context: DrawScope){
        context.drawDecorContext()
    }

    fun DrawScope.drawDecorContext(){
        decorEllipses.forEach {
            drawCircle(
                color = Color(0.05f,0.05f,0.05f),
                radius = (it.diametre*this.size.width).toFloat()/2f,
                center = it.centre.scale(this.size.width,this.size.height).offset
            )
        }

        decorLines.forEach {
            for (i in 1 until it.points.size){
                drawLine(
                    color = Color(0.05f,0.05f,0.05f),
                    it.points[i-1].scale(this.size.width,this.size.height).offset,
                    it.points[i].scale(this.size.width,this.size.height).offset,
                    strokeWidth = 5f
                )
            }
        }
    }
}

