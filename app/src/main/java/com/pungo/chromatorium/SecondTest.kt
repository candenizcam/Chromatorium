package com.pungo.chromatorium

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import com.pungo.chromatorium.game.Chroma
import com.pungo.chromatorium.game.ConnectionMatrix
import com.pungo.chromatorium.game.Point
import com.pungo.chromatorium.game.Test2Shape

@Composable
fun SecondTest(){
    val radius = 50.0
    val colourDepth = 2
    val colours = listOf(
        Chroma(colourDepth, colourDepth,0,0),
        Chroma(colourDepth,0,0,colourDepth),
        Chroma(colourDepth,colourDepth,colourDepth,colourDepth),
        Chroma(colourDepth,colourDepth,colourDepth,colourDepth))
    val centres = listOf(Point(100.0,100.0), Point(500.0,100.0), Point(250.0,300.0),Point(250.0,500.0))


    val shapes = (centres.indices).map { Test2Shape(6,centres[it],colours[it], radius = radius, it in listOf(2,3)) }
    val connections = ConnectionMatrix(centres.size)
    var selectedIndex by remember { mutableStateOf(-1) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black)
        .pointerInput(Unit){
            detectTapGestures(onPress = {
                selectedIndex=-1
                shapes.forEachIndexed { index, shape->
                    if(shape.circleContains(Point(it.x.toDouble(),it.y.toDouble()))){
                        if(shape.colour.generateColour()!=Color.White){
                            selectedIndex=index
                            offsetX = it.x
                            offsetY = it.y
                        }

                    }
                }
            })
        }.pointerInput(Unit){
            detectDragGestures(onDrag = { change, dragAmount ->
                if (selectedIndex >=0) {
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
                onDragEnd = {
                    if(selectedIndex>=0){
                        shapes.forEachIndexed { index, shape->
                            if(shape.circleContains(Point(offsetX.toDouble(),offsetY.toDouble()))){
                                if(shape.mutable){
                                    connections[selectedIndex+1,index+1] += 1


                                    shapes[index].colour = Chroma.vectorSum(
                                        shapes.map { it.colour }
                                        ,
                                        (1..connections.nodeNo).map { connections[it,index+1] })


                                }
                            }
                        }
                        selectedIndex=-1
                    }

                }
            )
        }
    ){
        Canvas(modifier = Modifier.fillMaxSize()) {


            val connectionWidth = 4f




            for(i in 1..shapes.size){
                for (j in 1..shapes.size){
                    val connectionNo = connections[i,j]
                    for (k in 0 until connectionNo){
                        drawLine(
                            color = shapes[i-1].colour.generateColour(),
                            shapes[i-1].centre.translated(2*connectionWidth*(k*2.0-connectionNo)).offset,shapes[j-1].centre.translated(2*connectionWidth*(k*2.0-connectionNo)).offset,
                            strokeWidth = connectionWidth
                        )
                    }
                }
            }

            shapes.forEach {

                drawPath(it.getPath(), color = it.colour.generateColour())
            }

            if(selectedIndex> -1){
                drawLine(
                    start = shapes[selectedIndex].centre.offset,
                    end = Offset(x = offsetX, y = offsetY),
                    color = shapes[selectedIndex].colour.generateColour(),
                    strokeWidth = 4f
                )

                drawCircle(
                    color = shapes[selectedIndex].colour.generateColour(),
                    radius = 25f,
                    center = Offset(offsetX,offsetY)

                )
            }




            //drawPath(path, color=Color.Cyan)
            //drawPoints(listOf(Offset(500f,500f),Offset(500f,700f),Offset(600f,600f),Offset(500f,500f)), PointMode.Polygon, color = Color.Cyan)

        }
    }



}







