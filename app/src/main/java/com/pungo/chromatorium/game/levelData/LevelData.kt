package com.pungo.chromatorium.game.levelData

import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Size


class LevelData(val levelTitle: String, val star3: Int, val star2: Int, val levelNo: String, val width: Int, val height: Int, val levelEllipses: List<LevelEllipseData>, val decorEllipses: List<DecorEllipseData>, val levelLines: List<LevelLineData>, val decorLines: List<DecorLineData>){

    val levelSize: Size
    get() {
        return Size(width,height)
    }


    fun ellipseFromId(id: String): LevelEllipseData {
        return levelEllipses.firstOrNull {it.id == id }!!
    }

    fun lineFromId(id1: String, id2: String): LevelLineData?{
        return levelLines.firstOrNull { (it.fromId == id1 && it.toId == id2).or(it.fromId == id2 && it.toId == id1) }
    }


    fun closestEllipseOrNull(p: Point): LevelEllipseData? {
        return levelEllipses.firstOrNull {
            it.centre.distance(p)< it.diametre*0.6
        }
    }

    fun closestLineOrNull(p: Point, cut: Double=0.1): LevelLineData? {

        val distances = levelLines.map {
            it.getLines().minOf { it.closestDistance(p) }
        }

        val smallestIndex = distances.withIndex().minByOrNull { (_, f) -> f }?.index

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





}

