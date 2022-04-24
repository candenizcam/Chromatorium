package com.pungo.chromatorium.game

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

/** This class controls colour affairs in the game
 * it has two main purposes, first is handling arithmetic operations between colours
 * Second is converting colour to math and math to colour
 */
class Chroma(val redDepth: Int, val greenDepth: Int, val blueDepth: Int, val red: Int, val green: Int, val blue: Int){
    constructor(depth: Int, red: Int, green: Int, blue: Int): this(depth,depth,depth,red,green,blue)
    constructor(redDepth: Int, greenDepth: Int, blueDepth: Int, colour: Color): this(redDepth,greenDepth,blueDepth,(colour.red*redDepth).toInt(),(colour.green*greenDepth).toInt(), (colour.blue*blueDepth).toInt() )
    constructor(depth: Int, colour: Color): this(depth,depth,depth,colour)


    fun generateColour(): Color {
        return Color(red = red.toFloat()/redDepth.toFloat(), green = green.toFloat()/greenDepth.toFloat(), blue = blue.toFloat()/blueDepth.toFloat())
    }




    fun linSum(other: Chroma, l: Float=0.5f): Chroma {
        return Companion.linSum(this,other, l)

    }

    fun inequalDepth(other: Chroma): Boolean {
        return Companion.inequalDepth(this,other)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun equals(other: Chroma): Boolean {
        return (red==other.red).and(green==other.green).and(blue == other.blue)
    }

    companion object{
        fun vectorSum(chromas: List<Chroma>, vector: List<Int>): Chroma {
            val s = vector.sum().toFloat()
            var black = Chroma(chromas[0].redDepth,chromas[0].greenDepth,chromas[0].blueDepth,0,0,0)
            if(s==0f){
                return black
            }
            val normalized = vector.map { it.toFloat()/s }
            for (i in chromas.indices) {
                black = chromas[i].linSum(black,normalized[i])
            }
            return black


        }



        fun inequalDepth(c1: Chroma, c2: Chroma): Boolean {
            return !(c1.redDepth==c2.redDepth).and(c1.greenDepth==c2.greenDepth).and(c1.blueDepth==c2.blueDepth)
        }

        fun linSum(c1: Chroma, c2: Chroma, l: Float=0.5f): Chroma {
            val l = l.coerceIn(0f..1f)
            if(inequalDepth(c1,c2)){
                throw Exception("Chroma depths do not match")
            }

            return Chroma(c1.redDepth, c1.greenDepth, c1.blueDepth,
                (l*c1.red.toFloat() + (1f-l)*c2.red.toFloat()).roundToInt(),
                (l*c1.green.toFloat() + (1f-l)*c2.green.toFloat()).roundToInt(),
                (l*c1.blue.toFloat() + (1f-l)*c2.blue.toFloat()).roundToInt(),
            )
        }
    }

}