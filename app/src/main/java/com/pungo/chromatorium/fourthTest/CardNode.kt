package com.pungo.chromatorium.fourthTest

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScopeMarker
import com.pungo.chromatorium.game.Point

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


    open fun draw(ds: DrawScope){
        ds.drawNode()
    }




    fun DrawScope.drawNode(){
        val v = normalPoint.scale(this.size.width,this.size.width)
        val w = this.size.width
        val h = this.size.height
        drawCircle(
            color = displayColour.generateColour(),
            radius = UniversalConstants.largeLedRadius,
            center = normalPoint.scale(this.size.width,this.size.height).offset
        )
    }

}