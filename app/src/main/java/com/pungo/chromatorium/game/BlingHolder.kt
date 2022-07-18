package com.pungo.chromatorium.game

import androidx.compose.runtime.mutableStateOf
import com.pungo.chromatorium.game.levelData.LevelLineData
import kotlin.math.abs

/** Holds bling for dotted lines
 *
 */
class BlingHolder(val line: LevelLineData, val firstId: String, val secondId: String, val step: Int = 1, val mutated: (Int)->Unit, val finished: ()->Unit  ) {

    var lit = 0
    set(value) {
        field = value
        //mutated(field)
    }
    var garbage = false
    val forward = line.fromId == firstId

    fun nextLight(){

        lit += abs(step)
        if (lit >=line.allPoints.size){
            finished()
            garbage = true
        }
    }

    fun isLit(n: Int): Boolean {
        return if (step>0){
            if(forward){
                n<=lit
            }else{
                line.allPoints.size - lit <= n
            }
        }else{
            if(forward){
                line.allPoints.size - lit >= n
                //n>=lit.value
            }else{
                n>=lit
            }
        }

    }

}