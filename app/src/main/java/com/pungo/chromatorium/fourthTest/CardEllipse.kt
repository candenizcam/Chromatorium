package com.pungo.chromatorium.fourthTest

import android.graphics.Typeface
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import com.pungo.chromatorium.game.tools.drawText
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Rectangle

class CardEllipse(id: String, x: Float, y: Float, colourString: String, val textRect: Rectangle?=null): CardNode(id, x,y) {
    val assignedColour: Chromini?
    init {
        assignedColour = if(colourString=="none"){
            null
        }else{
            val a = Chromini.fromHex(colourString)


            if(a.equals(Chromini.white)){

                null
            }else{
                paint(a)
                a
            }
        }

    }

    override fun paint(chromini: Chromini){
        displayColour = if(assignedColour!=null){
            assignedColour
        }else{
            chromini
        }
    }

    override fun DrawScope.drawNode(painter: Painter,  font: Typeface) {
        val generatedColour = displayColour.generateColour()

        drawCircle(
            color = generatedColour,
            radius = UniversalConstants.largeLedRadius,
            center = normalPoint.scale(this.size.width,this.size.height).offset
        )

        drawCircle(
            color = Color(0f,0f,0f,0.5f),
            radius = UniversalConstants.largeLedRadius,
            center = normalPoint.scale(this.size.width,this.size.height).offset
        )


        val tp = normalPoint.scale(this.size.width,this.size.height).translated(-UniversalConstants.largeLedRadius,-UniversalConstants.largeLedRadius)

        translate(tp.x.toFloat(),tp.y.toFloat()){
            with(painter) {

                draw(
                    Size(2*UniversalConstants.largeLedRadius,2*UniversalConstants.largeLedRadius),
                    colorFilter = ColorFilter.tint(
                        generatedColour,
                        //generatedColour,
                        blendMode = BlendMode.Modulate
                    )
                )
            }

        }
        if(textRect!=null){
            val textRectTopLeft = textRect.topLeft.scale(this.size.width,this.size.height)
            val textRectCentre = textRect.centre.scale(this.size.width,this.size.height)
            //drawRect(generatedColour,
            //    textRectTopLeft.offset,
            //    Size(textRect.w.toFloat()* this.size.width,textRect.h.toFloat()*this.size.height)
            //)

            val s = displayColour.rgbIntString
            drawText(drawContext, s, textRectCentre .x.toFloat(), textRectCentre .y.toFloat(),
                color= generatedColour,
                typeface = font
            )

        }



    }
}