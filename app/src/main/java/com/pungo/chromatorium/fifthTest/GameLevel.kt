package com.pungo.chromatorium.fifthTest

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.pungo.chromatorium.fifthTest.levelOpener.LevelData
import com.pungo.chromatorium.fourthTest.UniversalConstants
import com.pungo.chromatorium.game.tools.drawText
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Size

class GameLevel(val levelData: LevelData, val gameFieldSize: Size) {
    val levelSize: Size = levelData.levelSize.scaleToFit(gameFieldSize)
    val chrominiList = levelData.levelEllipses.map {
        if(it.fillColour=="none"){
            Chromini.white
        }else{
            Chromini.fromHex(it.fillColour)
        }

    }
    init {


    }


    @Composable
    fun draw(){
        val context = LocalContext.current

        Box(modifier = Modifier
            .background(Color.Black)
            .size(levelSize.width.dp, levelSize.height.dp)){
            Canvas(modifier = Modifier.fillMaxSize()
                //.clipToBounds()
            ){
                levelData.decorEllipses.forEach {
                    drawCircle(
                        color = Color(0.05f,0.05f,0.05f),
                        radius = (it.diametre*this.size.width).toFloat()/2f,
                        center = it.centre.scale(this.size.width,this.size.height).offset
                    )
                }

                levelData.decorLines.forEach {
                    for (i in 1 until it.points.size){
                        drawLine(
                            color = Color(0.05f,0.05f,0.05f),
                            it.points[i-1].scale(this.size.width,this.size.height).offset,
                            it.points[i].scale(this.size.width,this.size.height).offset,
                            strokeWidth = 5f
                        )
                    }
                }


                levelData.levelLines.forEach {
                    for (i in 1 until it.points.size){
                        drawLine(
                            color = Color.Red,
                            it.points[i-1].scale(this.size.width,this.size.height).offset,
                            it.points[i].scale(this.size.width,this.size.height).offset,
                            strokeWidth = 5f
                        )
                    }
                }

                levelData.levelEllipses.forEachIndexed {index, it->

                    drawCircle(
                        color = chrominiList[index].generateColour(),
                        radius = (it.diametre*this.size.width).toFloat()/2f,
                        center = it.centre.scale(this.size.width,this.size.height).offset
                    )


                    drawRect(
                        color = Color(0.3f,0.05f,0.2f),
                        topLeft = it.textRect.topLeft.scale(this.size.width,this.size.height).offset,
                        size = Size(it.textRect.w*this.size.width, it.textRect.h*this.size.height).androidSize


                    )



                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            chrominiList[index].toFloatString(),
                            (it.textRect.centre.x*this@Canvas.size.width).toFloat(),
                            ((it.textRect.bottom)*this@Canvas.size.height).toFloat(),
                            Paint().apply {
                                textSize = 30f
                                color = chrominiList[index].generateColour().toArgb()
                                textAlign = Paint.Align.CENTER
                                typeface = ResourcesCompat.getFont(context , com.pungo.chromatorium.R.font.breeserifregular)
                            }
                        )
                    }


/*
                    drawText(
                        drawContext = drawContext,
                        text =  chrominiList[index].toFloatString(),
                        x = (it.textRect.centre.x*this.size.width).toFloat(),
                        y = ((it.textRect.bottom)*this.size.height).toFloat(),
                        Paint().apply {
                            textSize = 100
                            color = Color.BLUE
                            textAlign = Paint.Align.CENTER
                        },
                        typeface = ResourcesCompat.getFont(context , com.pungo.chromatorium.R.font.breeserifregular)
                    )

 */






                }




            }

        }





    }

}