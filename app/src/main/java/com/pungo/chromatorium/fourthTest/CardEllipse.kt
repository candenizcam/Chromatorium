package com.pungo.chromatorium.fourthTest

import com.pungo.chromatorium.tools.Rectangle
import java.util.StringJoiner

class CardEllipse( x: Float, y: Float, colourString: String, textRect: Rectangle?=null): CardNode(x,y) {
    val assignedColour: Chromini?
    init {
        val a = Chromini.fromHex(colourString)


        assignedColour = if(a.equals(Chromini.white)){

            null
        }else{
            paint(a)
            a
        }
    }

    override fun paint(chromini: Chromini){
        displayColour = if(assignedColour!=null){
            assignedColour
        }else{
            chromini
        }
    }
}