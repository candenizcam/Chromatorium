package com.pungo.chromatorium.fifthTest

import android.content.Context
import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.pungo.chromatorium.fifthTest.levelOpener.LevelData
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Size

class GameLevel(val levelData: LevelData, val gameFieldSize: Size) {
    val levelSize: Size = levelData.levelSize.scaleToFit(gameFieldSize)
    /*
    val chrominiList = levelData.levelEllipses.map {
        if(it.fillColour=="none"){
            Chromini.white
        }else{
            Chromini.fromHex(it.fillColour)
        }

    }

     */
    val gameNetwork = GameNetwork(
        levelData.levelEllipses.map {
            it.fillColour
        },
        levelData.levelEllipses.map{
            it.nodeType
        }
    )

    init {


    }


    fun ellipseIdIndex(id: String): Int {
        levelData.levelEllipses.indexOfFirst { it.id==id }.also {
            if(it==-1){
                throw Exception("id not found in ellipses")
            }
            return it
        }
    }



    fun dragModifier(firstContact: (Float, Float)->Unit, dragStart: (Float, Float)->Unit,drag: (Float, Float)->Unit, dragEnd: ()->Unit): Modifier {
        return Modifier
            .pointerInput(Unit) {
                val h = this.size.height
                val w = this.size.width


                detectTapGestures(onPress = {
                    val x = it.x//-left
                    val y = it.y// - top
                    firstContact(x / w, y / h)
                })


            }
            .pointerInput(Unit) {
                val h = this.size.height
                val w = this.size.width

                detectDragGestures(onDragEnd = {
                    dragEnd()
                }, onDragStart = {
                    dragStart(it.x / w, it.y / h)
                }) { change, dragAmount ->
                    change.consumeAllChanges()
                    drag(dragAmount.x / w, dragAmount.y / h)
                }
            }
    }

    @Composable
    fun drawBackground(){
        val infiniteTransition = rememberInfiniteTransition()
        val value by infiniteTransition.animateFloat(
            initialValue =0.0f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(36000,easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Chromini
                    .fromHSV(value, 0.7f, 0.3f)
                    .generateColour()
            )){
        }
    }



    @Composable
    fun BoxScope.levelCanvas(context: Context) {

        drawBackground()

        val (touchStartPoint, touchEndPoint, startIndex) = remember{
            Triple(mutableStateOf(Point(-1.0,-1.0)),mutableStateOf(Point(0.0,0.0)), mutableStateOf(""))
        }
        val dragging = remember{ mutableStateOf(false) }


        val dm = remember {
            mutableStateOf(
                dragModifier(
                    firstContact = {x,y->
                        levelData.closestEllipseOrNull(Point(x,y)).let {
                            if (it!=null){
                                touchStartPoint.value = Point(it.centre.x,it.centre.y)
                                touchEndPoint.value = Point(x,y)
                                startIndex.value = it.id
                            }else{
                                val lin = levelData.closestLineOrNull(Point(x,y))
                                if(lin!= null){
                                    gameNetwork.cutConnection(ellipseIdIndex(lin.toId),ellipseIdIndex(lin.fromId))
                                }
                                gameNetwork.updateColours()

                            }
                        }


                    },
                    dragStart = { x,y->
                        if(touchStartPoint.value.x != -1.0) {
                            dragging.value = true
                        }


                        //if(touchStartPoint.value.x!=-1.0){
                        //    touchEndPoint.value = Point(x,y)
                        //}
                    },
                    drag = {x,y->
                        if(dragging.value){
                            touchEndPoint.value = touchEndPoint.value.translated(x,y)
                        }

                    },
                    dragEnd = {

                        dragging.value=false
                        levelData.levelEllipses.firstOrNull {
                            touchEndPoint.value.distance(it.centre)<0.1
                        }.let {
                            if (it!=null){
                                gameNetwork.connect(ellipseIdIndex(startIndex.value), ellipseIdIndex(it.id))
                            }else{

                            }
                        }
                        touchStartPoint.value = Point(-1.0,-1.0)
                        gameNetwork.updateColours()
                    }
                )
            )

        }



        Canvas(modifier = Modifier
            .fillMaxSize()
            .then(dm.value)
            //.clipToBounds()
        ){
            levelData.drawDecor(this)



            levelData.levelLines.forEach {
                for (i in 1 until it.allPoints.size){
                    drawCircle(
                        color = gameNetwork.getConnectionChromini(ellipseIdIndex(it.toId),ellipseIdIndex(it.fromId))?.generateColour() ?: Color(0.1f,.1f,.1f),
                        radius = 4f,
                        center = it.allPoints[i].scale(this.size.width,this.size.height).offset
                    )
                }
            }

            levelData.levelEllipses.forEachIndexed {index, it->

                gameNetwork.getBorderChromini(index).let {it2->
                    if(it2!=null){
                        drawCircle(
                            color = it2.generateColour(),
                            radius = ((it.diametre*this.size.width).toFloat())/2f+ 5f,
                            center = it.centre.scale(this.size.width,this.size.height).offset
                        )
                    }
                }



                gameNetwork.getFillChromini(index).let {it2->

                    drawCircle(
                        color = it2?.generateColour() ?: Color(0.1f,0.1f,0.1f),
                        radius = (it.diametre*this.size.width).toFloat()/2f,
                        center = it.centre.scale(this.size.width,this.size.height).offset
                    )

                    if(it2!=null){
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                it2.hexString,
                                (it.textRect.centre.x*this@Canvas.size.width).toFloat(),
                                ((it.textRect.bottom)*this@Canvas.size.height).toFloat(),
                                Paint().apply {
                                    letterSpacing = -0.1f
                                    textSize = 30f
                                    color = it2.generateColour().toArgb()
                                    textAlign = Paint.Align.CENTER
                                    typeface = ResourcesCompat.getFont(context , com.pungo.chromatorium.R.font.sharetechmonoregular)
                                }
                            )
                        }
                    }

                }


                if (dragging.value){
                    drawLine(
                        Color(0.4f,0.4f,0.4f),
                        touchStartPoint.value.scale(this.size.width,this.size.height).offset,
                        touchEndPoint.value.scale(this.size.width,this.size.height).offset,
                        strokeWidth = 5f
                    )

                    drawCircle(
                        Color(0.4f,0.4f,0.4f),
                        20f,
                        touchEndPoint.value.scale(this.size.width,this.size.height).offset
                    )
                }





                /*
                drawRect(
                    color = Color(0.3f,0.05f,0.2f),
                    topLeft = it.textRect.topLeft.scale(this.size.width,this.size.height).offset,
                    size = Size(it.textRect.w*this.size.width, it.textRect.h*this.size.height).androidSize


                )

                 */







            }
        }
    }



    @Composable
    fun draw(){
        val context = LocalContext.current

        Box(modifier = Modifier
            .background(Color.Black)
            .size(levelSize.width.dp, levelSize.height.dp)){

            levelCanvas(context)

        }





    }

}