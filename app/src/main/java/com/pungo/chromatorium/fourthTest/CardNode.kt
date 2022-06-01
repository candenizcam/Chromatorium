package com.pungo.chromatorium.fourthTest

import android.graphics.Typeface
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Point

/** Card note corresponds to an item of significance
 * other node dwellers can be inherited from this main class
 * a vector of all nodes are held in the level, links are not contained here
 * x, y: are position variables, given between 0f and 1f, y is from the top
 * it also comes with its own drawer (that draws to a canvas, cool huh?)
 */
open class CardNode(val id: String, val x: Float, val y: Float) {
    val normalPoint = Point(x,y)
    var displayColour = Chromini.white
        protected set


    open fun paint(chromini: Chromini){
        displayColour = chromini
    }


    open fun draw(ds: DrawScope,  painter: Painter, font: Typeface){

        ds.drawNode(  painter, font)
    }




    open fun DrawScope.drawNode( painter: Painter, font: Typeface){
        val v = normalPoint.scale(this.size.width,this.size.width)
        val w = this.size.width
        val h = this.size.height

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

                draw(Size(2*UniversalConstants.largeLedRadius,2*UniversalConstants.largeLedRadius),
                colorFilter = tint(
                    generatedColour,
                    //generatedColour,
                    blendMode = BlendMode.Modulate
                ))
            }

        }







        /*
        val b  = Brush.radialGradient(
            0.0f to lighter,
            0.5f to generatedColour,
            0.8f to generatedColour,
            0.85f to darker,
            //0.9f to generatedColour,
            center = normalPoint.scale(this.size.width,this.size.height).offset,
            radius = UniversalConstants.largeLedRadius,

            )

        drawCircle(
            brush = b,
            radius = UniversalConstants.largeLedRadius,
            center = normalPoint.scale(this.size.width,this.size.height).offset
        )

         */
    }

}