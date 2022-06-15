package com.pungo.chromatorium.game

import android.content.Context
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.pungo.chromatorium.R
import com.pungo.chromatorium.game.levelOpener.NodeType
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.dragModifier

@Composable
fun drawGameV(gameLevel: GameLevel){
    val context = LocalContext.current
    val (touchStartPoint, touchEndPoint, startIndex) = remember{
        Triple(mutableStateOf(Point(-1.0,-1.0)), mutableStateOf(Point(0.0,0.0)), mutableStateOf(""))
    }
    val dragging = remember{ mutableStateOf(false) }
    val levelData = gameLevel.levelData
    val gameNetwork = gameLevel.gameNetwork

    val dm = dragModifier(
        firstContact = {x: Float,y: Float->
            gameLevel.levelData.closestEllipseOrNull(Point(x,y)).let {
                if (it!=null){
                    touchStartPoint.value = Point(x,y)
                    touchEndPoint.value = Point(it.centre.x, it.centre.y)
                    startIndex.value = it.id
                }else{
                    val lin = levelData.closestLineOrNull(Point(x,y))
                    if(lin!= null){
                        val fw = gameNetwork.relationsMatrix[ gameLevel.ellipseIdIndex(lin.fromId)+1, gameLevel.ellipseIdIndex(lin.toId)+1  ]!=0.0
                        val bw = gameNetwork.relationsMatrix[  gameLevel.ellipseIdIndex(lin.toId)+1 ,gameLevel.ellipseIdIndex(lin.fromId)+1 ]!=0.0
                        if (bw.xor(fw)){
                            val (thisId, otherId) = if (fw){
                                Pair(lin.fromId, lin.toId)
                            }else{
                                Pair(lin.toId, lin.fromId)
                            }
                            gameLevel.addBlinger(
                                BlingHolder(lin,thisId, otherId, -1 ){

                                }
                            )
                            gameNetwork.cutConnection(gameLevel.ellipseIdIndex(thisId), gameLevel.ellipseIdIndex(otherId))
                            gameLevel.updateColours()
                        }else{
                            val bh = gameLevel.blingHolders.firstOrNull { it.line == lin }
                            if (bh!= null){
                                gameLevel.addBlinger(
                                    BlingHolder(lin,bh.firstId, bh.secondId, -1 ){}
                                )
                            }
                        }
                    }
                }
            }
        },
        dragStart = { _,_->
            if(touchStartPoint.value.x != -1.0) {
                dragging.value = true
            }
        },
        drag = {x,y->
            if(dragging.value){
                touchEndPoint.value = touchEndPoint.value.translated(x,y)
            }
        },
        dragEnd = {
            dragging.value = false
            levelData.levelEllipses.firstOrNull {
                touchEndPoint.value.distance(it.centre)<it.diametre*0.8
            }.let {
                if (it!=null){
                    if(startIndex.value!=it.id){
                        val relLine = levelData.lineFromId(startIndex.value,it.id)
                        if (relLine!=null){
                            val startIndexValue = startIndex.value
                            gameLevel.addBlinger(
                                BlingHolder(relLine,startIndexValue,it.id, 1 ){
                                    gameLevel.moveCounter.value += 1
                                    val id1 = gameLevel.ellipseIdIndex(startIndexValue)
                                    val id2 =  gameLevel.ellipseIdIndex(it.id)

                                    gameNetwork.connect(id1,id2)
                                    gameLevel.updateColours()
                                }
                            )
                        }

                    }
                }
            }
            touchStartPoint.value = Point(-1.0,-1.0)
        }
    )

    Box(modifier = Modifier
        .size(gameLevel.levelSize.width.dp, gameLevel.levelSize.height.dp)){

        levelCanvas(context,gameLevel ,dm, dragging.value, touchStartPoint.value,touchEndPoint.value)
    }
}


@Composable
fun BoxScope.levelCanvas(context: Context, gameLevel: GameLevel, dm: Modifier, dragging: Boolean, touchStartPoint: Point, touchEndPoint: Point  ) {
    val levelData = gameLevel.levelData
    val gameNetwork = gameLevel.gameNetwork
    Canvas(modifier = Modifier
        .fillMaxSize()
        .then(if (gameLevel.levelCompleted.value) Modifier else dm)
        //.clipToBounds()
    ){
        levelData.drawDecor(this)
        val defLineColour = Color(0.9f,.9f,.9f)
        levelData.levelLines.forEach {
            val givenLineColour = gameNetwork.getConnectionChromini(gameLevel.ellipseIdIndex(it.toId),gameLevel.ellipseIdIndex(it.fromId))?.generateColour()
            val blinging = gameLevel.blingHolders.firstOrNull { it2-> it2.line == it }
            for (i in 1 until it.allPoints.size){
                val c = if(blinging?.isLit(i) == true){
                    gameNetwork.getFillColor(gameLevel.ellipseIdIndex(blinging.firstId))
                }else{
                    givenLineColour?: defLineColour

                }

                drawCircle(
                    color = c,
                    radius = 6f,
                    center = it.allPoints[i].scale(this.size.width,this.size.height).offset
                )
            }
        }

        levelData.levelEllipses.forEachIndexed {index, it->
            gameNetwork.getBorderChromini(index).let {it2->
                val v = if(it.nodeType == NodeType.NONE)  5f else 10f
                if(it2!=null){
                    drawCircle(
                        color = it2.generateColour(),

                        radius = ((it.diametre*this.size.width).toFloat())/2f+ v,
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
                                typeface = ResourcesCompat.getFont(context , R.font.sharetechmonoregular)
                            }
                        )
                    }
                }
            }


            if (dragging){
                drawLine(
                    Color(0.4f,0.4f,0.4f),
                    touchStartPoint.scale(this.size.width,this.size.height).offset,
                    touchEndPoint.scale(this.size.width,this.size.height).offset,
                    strokeWidth = 5f
                )

                drawCircle(
                    Color(0.4f,0.4f,0.4f),
                    20f,
                    touchEndPoint.scale(this.size.width,this.size.height).offset
                )
            }
        }
    }
}