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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun DragTest(){
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var dragColour by remember {  mutableStateOf(0) }
    var whiteSquareColour by remember { mutableStateOf(0) }
    val redSquare = Offset(50f,50f)
    val blueSquare = Offset(50f,550f)
    val whiteSquare = Offset(250f,350f)
    val side = 50f


    Box(modifier = Modifier.fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit){
            detectTapGestures(onPress = {
                if((it.x>redSquare.x) && (it.x<redSquare.x+side) && (it.y>redSquare.y) && (it.y<redSquare.y+side)){
                    offsetX = it.x
                    offsetY = it.y
                    dragColour=1
                }else if((it.x>blueSquare.x) && (it.x<blueSquare.x+side) && (it.y>blueSquare.y) && (it.y<blueSquare.y+side)){
                    offsetX = it.x
                    offsetY = it.y
                    dragColour=2
                }else{
                    dragColour=0
                }
            })
        }
        .pointerInput(Unit) {
            detectDragGestures(onDrag = { change, dragAmount ->
                if (dragColour != 0) {
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
                onDragEnd = {
                    if((offsetX>whiteSquare.x) && (offsetX<whiteSquare.x+side) && (offsetY>whiteSquare.y) && (offsetY<whiteSquare.y+side)){
                        whiteSquareColour=dragColour
                    }
                    dragColour=0
                }
            )

        }.pointerInput(Unit){

        }
    ){

        val c = if(dragColour==1){
            Color.Red
        }else{
            Color.Blue
        }
        val c2 = if(whiteSquareColour==1){
            Color.Red
        }else if(whiteSquareColour==2){
            Color.Blue
        }else{
            Color.White
        }
        val fromCentre = if(dragColour==1){
            Offset(x = redSquare.x+side/2,y = redSquare.y + side/2)
        }else if(dragColour==2){
            Offset(x = blueSquare.x+side/2,y = blueSquare.y + side/2)
        }else{
            Offset(0f,0f)
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            if(dragColour!=0){
                drawLine(
                    start = fromCentre,
                    end = Offset(x = offsetX, y = offsetY),
                    color = c,
                    strokeWidth = 4f
                )

                drawCircle(
                    color = c,
                    radius = 25f,
                    center = Offset(offsetX,offsetY)

                )
            }


            drawRect(Color.Red, topLeft = redSquare, size = Size(side,side))
            drawRect(Color.Blue, topLeft = blueSquare, size = Size(side,side))
            drawRect(c2, topLeft = whiteSquare, size = Size(side,side))
        }

    }
}