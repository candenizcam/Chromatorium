package com.pungo.chromatorium.fourthTest

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.pungo.chromatorium.game.Line
import com.pungo.chromatorium.game.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CardLink(val from: Int, val to: Int) {
    private val points = mutableListOf<Point>() // 0f to 1f
    private val lines = mutableListOf<Line>()
    private val leds = mutableListOf<Point>()
    var state = 0 // 0-> closed, 1-> from2to, 2->to2from
        private set


    fun nextState(){
        state = if (state==2){
            0
        }else{
            state+1
        }
    }

    fun setPoints(newPoints: List<Point>,leds: List<Point>){
        points.clear()
        lines.clear()
        this.leds.clear()
        this.leds.addAll(leds)
        points.addAll(newPoints)
        for(i in 0 until points.size-1){
            lines.add(Line(points[i],points[i+1]))

        }
    }


    fun draw(ds: DrawScope){
        ds.drawLink()
    }

    fun distance(p: Point): Double {

        return lines.map {
            it.closestDistance(p)
        }.minOrNull()!!
    }

    fun closestPoint(p: Point): Point {
        val m1 = lines.map { it.closestPointAndDistancePair(p) }

        return m1.minByOrNull { it.second }!!.first

    }




    fun DrawScope.drawLink(){
        //val v = normalPoint.scale(this.size.width,this.size.height)
        val c = if (state==0){
            Color.Gray
        }else if(state==1){
            Color.Green
        }else{
            Color.Black
        }
        lines.forEach {

            val p1 = it.p1.scale(this.size.width,this.size.height)
            val p2 = it.p2.scale(this.size.width,this.size.height)


            /*
            drawLine(
                c
                ,
                it.p1.scale(this.size.width,this.size.height).offset,
                it.p2.scale(this.size.width,this.size.height).offset,
                strokeWidth = 12f
            )

             */

            leds.forEach {
                drawCircle(
                    c,
                    UniversalConstants.smallLedRadius,
                   it.scale(this.size.width,this.size.height).offset
                )
            }




        }


    }
}