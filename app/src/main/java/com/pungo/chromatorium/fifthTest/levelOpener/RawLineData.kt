package com.pungo.chromatorium.fifthTest.levelOpener

import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Rectangle


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


    fun getLevelLine(left: Int, top: Int, width: Int, height: Int): LevelData.LevelLineData {
        val levelPoints = points.map {
            val (x,y) = it.split(",").map { it.toDouble() }

            val adjusted_centre_x = (x.toDouble() - left.toDouble())/ width.toDouble()
            val adjusted_centre_y = (y.toDouble() - top.toDouble())/height.toDouble()
            Point(adjusted_centre_x,adjusted_centre_y)
        }

        return LevelData.LevelLineData(fromId, toId, levelPoints)

    }

    fun getDecorLine(left: Int, top: Int, width: Int, height: Int): LevelData.DecorLineData? {
        val decorPoints = points.map {
            val (x,y) = it.split(",").map { it.toDouble() }

            val adjusted_centre_x = (x.toDouble() - left.toDouble())/ width.toDouble()
            val adjusted_centre_y = (y.toDouble() - top.toDouble())/height.toDouble()
            Point(adjusted_centre_x,adjusted_centre_y)
        }

        val d = decorPoints.any { (it.x in 0.0..1.0) || (it.y in 0.0..1.0) }

        return if(d){
            LevelData.DecorLineData(fromId, toId, decorPoints)
        }else{
            null
        }

    }
}