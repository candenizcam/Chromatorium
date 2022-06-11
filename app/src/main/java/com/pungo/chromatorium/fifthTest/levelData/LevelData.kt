package com.pungo.chromatorium.fifthTest.levelData

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.pungo.chromatorium.fifthTest.levelOpener.NodeType
import com.pungo.chromatorium.tools.Line
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Rectangle
import com.pungo.chromatorium.tools.Size


class LevelData(val levelNo: String, val width: Int, val height: Int, val levelEllipses: List<LevelEllipseData>, val decorEllipses: List<DecorEllipseData>, val levelLines: List<LevelLineData>, val decorLines: List<DecorLineData>){

    val levelSize: Size
    get() {
        return Size(width,height)
    }








    fun ellipseFromId(id: String): LevelEllipseData {
        return levelEllipses.firstOrNull {it.id == id }!!
    }

    fun lineFromId(id1: String, id2: String): LevelLineData{
        return levelLines.firstOrNull { (it.fromId == id1 && it.toId == id2).or(it.fromId == id2 && it.toId == id1) }!!
    }


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

