package com.pungo.chromatorium.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.pungo.chromatorium.tools.Point

class LevelGrid(val rows: Int, val cols: Int){
    val widthToHeight: Float
        get() {
            return cols.toFloat()/rows.toFloat()
        }

    fun gridWidth( width: Float, height: Float): Float {
        return if(height*widthToHeight>width){
            width
        }else{
            height*widthToHeight
        }
    }



    fun horizontalPoint(c: Int, width: Float): Float {
        return width/cols.toFloat()*(c-0.5f)
    }

    fun verticalPoint(r: Int, height: Float): Float {
        return height/rows.toFloat()*(r-0.5f)
    }

    fun gridToRatedCoordinates(p: Point) : Point {
        return p.scale(1f/cols.toFloat(), 1f/rows.toFloat())
    }


    /** This function takes a float point (0-1.0) and converts it to grid coordinates
     */
    fun ratedToGridCoordinates(p: Point): Point {
        return p.scale(cols.toFloat(),rows.toFloat() )
    }

    fun topLeftPoint(r: Int, c: Int, width: Float, height: Float): Point {
        return Point(width/cols.toFloat()*(c-1f),height/rows.toFloat()*(r-1f))
    }

    fun singleSide(width: Float): Float {
        return width/cols
    }

    fun getValue(r: Int, c: Int){

    }


    @Composable
    fun Checkerboard(c1: Color = Color.LightGray, c2: Color = Color.DarkGray){
        Canvas(modifier = Modifier.fillMaxSize()){
            for(i in 0 until rows){
                for(j in 0 until cols){
                    val c = if((i+j)%2==0){
                        c1
                    }else{
                        c2
                    }
                    val p = topLeftPoint(i+1,j+1, size.width,size.height)

                    drawRect(c, p.offset, size= Size(singleSide(size.width),singleSide(size.width)))

                }

            }
        }
    }

}