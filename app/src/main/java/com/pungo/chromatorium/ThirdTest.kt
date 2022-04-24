package com.pungo.chromatorium

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.pungo.chromatorium.game.Chroma
import com.pungo.chromatorium.game.Point
import com.pungo.chromatorium.game.Shape
import com.pungo.chromatorium.game.Test2Shape

@Composable
fun ThirdTest() {
    val level = Level("")

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        level.draw( 0f,0f,LocalConfiguration.current.screenWidthDp.toFloat(),LocalConfiguration.current.screenHeightDp.toFloat())
    }


}



class Level(s: String){
    val levelItems = mutableListOf<LevelItem>()
    val levelGrid: LevelGrid
    init {
        levelGrid= LevelGrid(5,3)
        val colourDepth = 2
        val colours = listOf(
            Chroma(colourDepth, colourDepth,0,0),
            Chroma(colourDepth,0,0,colourDepth),
            Chroma(colourDepth,colourDepth,colourDepth,colourDepth),
            Chroma(colourDepth,colourDepth,colourDepth,colourDepth))
        val shapes = listOf(
            Shape(4),
            Shape(5),
            Shape(4),
            Shape(6)
        )
        val centres = listOf(
            Pair(1,1),
            Pair(2,3),
            Pair(3,2),
            Pair(4,2)
        )

        for(i in colours.indices){
            levelItems.add(
                LevelItem(shapes[i], colours[i],centres[i].first,centres[i].second, 0.6f, 0.5f )
            )
        }

    }


    fun dragModifier(left: Float, top: Float): Modifier {
        return Modifier.pointerInput(Unit){
            detectTapGestures {
                val x = it.x//-left
                val y = it.y// - top
                println("point location: $x $y")
            }
            /*
            detectDragGestures(onDragStart = {
                    val x = it.x//-left
                    val y = it.y// - top
                println("point location: $x $y")

                },
                onDrag = { change: PointerInputChange, dragAmount: Offset ->

                },
                onDragEnd = {

                }
            )

             */
        }
    }


    @Composable
    fun draw(left: Float, top: Float, width: Float, height: Float){
        val drawWidth = levelGrid.gridWidth(width,height)
        val drawHeight  = drawWidth/levelGrid.widthToHeight

        val innerTop = (height- drawHeight)*0.5f
        val innerLeft = (width- drawWidth)*0.5f


        Box(modifier = Modifier.size(drawWidth.dp, drawHeight.dp).then(dragModifier(innerLeft+left, innerTop+top))){
            levelGrid.Checkerboard(Color(0xFF061626),Color(0xFF160626))


            Canvas(modifier = Modifier.fillMaxSize()){
                levelItems.forEach {
                    val y = levelGrid.verticalPoint(it.r,this.size.height)
                    val x = levelGrid.horizontalPoint(it.c,this.size.width)
                    drawPath(it.shape.getPath(Point(x,y),levelGrid.singleSide(drawWidth)*it.radius), color = it.chroma.generateColour())
                }
            }
        }
    }
}


class LevelItem(val shape: Shape, var chroma: Chroma, val r: Int, val c: Int, val radius: Float, val hitBox: Float){

}



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

    fun topLeftPoint(r: Int, c: Int, width: Float, height: Float): Point {
        return Point(width/cols.toFloat()*(c-1f),height/rows.toFloat()*(r-1f))
    }

    fun singleSide(width: Float): Float {
        return width/cols
    }

    fun getValue(r: Int, c: Int){

    }

    @Composable
    fun Checkerboard(c1: Color = Color.LightGray, c2: Color= Color.DarkGray){
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