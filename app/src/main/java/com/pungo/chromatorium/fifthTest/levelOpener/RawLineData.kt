package com.pungo.chromatorium.fifthTest.levelOpener

import com.pungo.chromatorium.fifthTest.levelData.DecorLineData
import com.pungo.chromatorium.fifthTest.levelData.LevelData
import com.pungo.chromatorium.fifthTest.levelData.LevelLineData
import com.pungo.chromatorium.tools.Line
import com.pungo.chromatorium.tools.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


class RawLineData(s: String){
    // line: lin:opacity:fr.id,to.id:point_first:...:point_last
    val opacity: String
    val fromId: String
    val toId: String
    val points: List<String>

    init {
        val v = s.split(":")
        opacity = v[1]
        v[2].split(",").also {
            fromId = it[0].split(".")[1]
            toId = it[1].split(".")[1]
        }
        points = (3 until v.size).map { v[it] }

    }

    val left: Int
        get() {
            return points.minOf { it.split(",")[0].toInt() }
        }
    val top: Int
        get() {
            return points.minOf { it.split(",")[1].toInt() }
        }
    val right: Int
        get() {
            return points.maxOf { it.split(",")[0].toInt() }
        }
    val bottom: Int
        get() {
            return points.maxOf { it.split(",")[1].toInt() }
        }



    fun getCorners(left: Int, top: Int, width: Int, height: Int): List<Point> {
        return points.map {
            val (x,y) = it.split(",").map { it.toDouble() }

            val adjusted_centre_x = (x.toDouble() - left.toDouble())/ width.toDouble()
            val adjusted_centre_y = (y.toDouble() - top.toDouble())/height.toDouble()
            Point(adjusted_centre_x,adjusted_centre_y)
        }
    }

    fun getAllPoints(left: Int, top: Int, width: Int, height: Int, space: Int =8): List<Point> {
        val l = mutableListOf<Point>()
        for (i in 1 until points.size){
            val (x1,y1) = points[i-1].split(",").map { it.toInt() }
            val (x2,y2) = points[i].split(",").map { it.toInt() }
            val (minX,maxX) = Pair(min(x1,x2),max(x1,x2))
            val (minY,maxY) = Pair(min(y1,y2),max(y1,y2))

            val line = Line(Point(x1,y1),Point(x2,y2))

            val xPoints = (minX until maxX).map { Point(it.toDouble(), line.yFromX(it.toDouble())) }
            val yPoints = (minY until maxY).map { Point(line.xFromY(it.toDouble()), it.toDouble()) }


            val intYPoints = yPoints.filter { abs(it.x - it.x.roundToInt())<0.1 }
            val intXPoints = xPoints.filter { abs(it.y - it.y.roundToInt())<0.1 }

            val allPoints = intXPoints.toMutableList().also {
                it.addAll(intYPoints.filterNot {
                    ((intXPoints.map { it2 -> it.distance(it2) }).minOrNull() ?: 1.0) < 0.1
                })
            }


            val sortedAllPoints = allPoints.sortedBy { it.distance(Point(x1,y1)) }

            val finalPoints = mutableListOf<Point>()
            finalPoints.add(l.lastOrNull()?: sortedAllPoints.first())
            sortedAllPoints.forEach {
                if ((finalPoints.lastOrNull()?.distance(it)?.compareTo(space) ?: 1) > 0){
                    finalPoints.add(it)
                }
            }


            l.addAll(finalPoints)



        }




        return l.map {
            val adjusted_centre_x = (it.x.toDouble() - left.toDouble())/ width.toDouble()
            val adjusted_centre_y = (it.y.toDouble() - top.toDouble())/height.toDouble()
            Point(adjusted_centre_x,adjusted_centre_y)
        }

    }

    fun getLevelLine(left: Int, top: Int, width: Int, height: Int): LevelLineData {
        val levelPoints = getCorners(left,top,width,height)
        val allPoints = getAllPoints(left,top,width,height)


        return LevelLineData(fromId, toId, levelPoints, allPoints)

    }

    fun getDecorLine(left: Int, top: Int, width: Int, height: Int): DecorLineData? {
        val decorPoints = getCorners(left,top,width,height)

        val d = decorPoints.any { (it.x in 0.0..1.0) || (it.y in 0.0..1.0) }

        return if(d){
            DecorLineData(fromId, toId, decorPoints)
        }else{
            null
        }

    }
}