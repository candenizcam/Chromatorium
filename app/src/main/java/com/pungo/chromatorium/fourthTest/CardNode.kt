package com.pungo.chromatorium.fourthTest

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.ColorFilter.Companion.colorMatrix
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import com.pungo.chromatorium.tools.Point

/** Card note corresponds to an item of significance
 * other node dwellers can be inherited from this main class
 * a vector of all nodes are held in the level, links are not contained here
 * x, y: are position variables, given between 0f and 1f, y is from the top
 * it also comes with its own drawer (that draws to a canvas, cool huh?)
 */
open class CardNode(val x: Float, val y: Float) {
    val normalPoint = Point(x,y)
    var displayColour = Chromini.white
        protected set


    open fun paint(chromini: Chromini){
        displayColour = chromini
    }


    open fun draw(ds: DrawScope,  painter: Painter){
        ds.drawNode(  painter)
    }




    fun DrawScope.drawNode( painter: Painter){
        val v = normalPoint.scale(this.size.width,this.size.width)
        val w = this.size.width
        val h = this.size.height

        val generatedColour = displayColour.generateColour()
        val lighter  = displayColour.times(1.1).generateColour()
        val darker  = displayColour.times(0.8).generateColour()


        drawCircle(
            color = Color.White,
            radius = UniversalConstants.largeLedRadius,
            center = normalPoint.scale(this.size.width,this.size.height).offset
        )


        val tp = normalPoint.scale(this.size.width,this.size.height).translated(-UniversalConstants.largeLedRadius,-UniversalConstants.largeLedRadius)

        translate(tp.x.toFloat(),tp.y.toFloat()){
            with(painter) {

                draw(Size(2*UniversalConstants.largeLedRadius,2*UniversalConstants.largeLedRadius),
                colorFilter = tint(
                    generatedColour,
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