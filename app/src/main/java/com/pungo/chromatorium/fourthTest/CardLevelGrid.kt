package com.pungo.chromatorium.fourthTest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.pungo.chromatorium.game.Point

/** This class contains the information for the grid in a level
 * it also hold various conventions for conversion between normalized and squared grid
 * it may also one day include a drawer for a background grid
 * it does not contain information regarding items or links
 *
 */
class CardLevelGrid(val r: Int, val c: Int, val padding: Float = 0f) {

    @Composable
    fun drawGrid( gridStep: Int= 1){
        Canvas(modifier = Modifier.fillMaxSize()){
            drawGrid(gridStep)
        }
    }

    fun DrawScope.drawGrid(gridStep: Int= 1){
        drawRect(Color( 3,40,67))
        for(i in 0..r step  gridStep){
            for(j in 0 .. c step  gridStep){
                val left = size.width/(c)*(j)
                val top = size.height/(r)*(i)
                drawCircle(
                    color = Color(218,165,32),
                    radius = 12f,
                    center = Point(left,top).offset
                )

                drawCircle(
                    color = Color.Black,
                    radius = 6f,
                    center = Point(left,top).offset
                )


            }

        }
    }

    fun ratedX(x: Int): Float {
        return (x.toFloat()+padding-1)/(c+padding*2-1)
    }

    fun ratedY(y: Int): Float {
        return (y.toFloat()+padding-1)/(r+padding*2-1)
    }

    fun ratedPair(x: Int, y: Int): Pair<Float, Float> {
        return Pair(ratedX(x),ratedY(y))
    }

    fun gridX(x: Float): Int {
        return (x*(c+padding*2)-padding).toInt()
    }

    fun gridY(y: Int): Int {
        return (y*(r+padding*2)-padding).toInt()
    }

    fun fitInto(w: Float, h: Float): Pair<Float, Float> {
        return if(h/r.toFloat()>w/c.toFloat()){
            Pair(w, w/c.toFloat()*r.toFloat())
        }else{
            Pair(h/r.toFloat()*c.toFloat(),h)
        }

    }
}