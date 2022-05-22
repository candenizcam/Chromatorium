package com.pungo.chromatorium.fourthTest

import androidx.compose.ui.graphics.Color
import com.pungo.chromatorium.game.Chroma
import java.util.StringJoiner
import kotlin.math.roundToInt

/** This is a lightweight alternative to Chroma, notably we've dropped the depth
 *
 */
class Chromini
    constructor(red: Float, green: Float, blue: Float) {
    val red: Float
    val green: Float
    val blue: Float
    init {
        this.red = red.coerceIn(0f..1f)
        this.green = green.coerceIn(0f..1f)
        this.blue = blue.coerceIn(0f..1f)

    }
    constructor(red: Int, green: Int, blue: Int): this(red/255f, green/255f, blue/255f)
    constructor(color: Color): this(color.red,color.green,color.blue)




    fun generateColour(): Color {
        return Color(red = red, green = green, blue = blue)
    }


    operator fun plus(other: Chromini): Chromini {
        return Chromini(red + other.red, green+other.green, blue + other.blue)
    }

    operator fun times(other: Double): Chromini{
        return Chromini((red*other).toFloat(),(green*other).toFloat(),(blue*other).toFloat())
    }

    fun equals(other: Chromini?): Boolean{
        if(other==null){
            return false
        }
        return (this.red==other.red).and(this.green==other.green).and(this.blue == other.blue)
    }

    override fun toString(): String {
        return "${(red*255).roundToInt()},${(green*255).roundToInt()},${(blue*255).roundToInt()}"
    }



    companion object{
        fun fromHex(s: String): Chromini {
            val s2 = s.replace("#","")
            val (r,g,b) = (0..2).map {

                s2.substring((it*2)..(it*2+1)).toInt(16)

            }
            return Chromini(r,g,b)

        }

        val white: Chromini
            get() {
                return Chromini(1f,1f,1f)
            }

        val black: Chromini
            get() {
                return Chromini(0f,0f,0f)
            }
    }


}