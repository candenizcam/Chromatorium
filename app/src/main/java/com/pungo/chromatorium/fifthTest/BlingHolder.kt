package com.pungo.chromatorium.fifthTest

import androidx.compose.runtime.mutableStateOf
import com.pungo.chromatorium.fifthTest.levelData.LevelLineData
import kotlin.math.abs

/** Holds bling for dotted lines
 *
 */
class BlingHolder(val line: LevelLineData, val firstId: String, val secondId: String, val step: Int = 1, val finished: ()->Unit) {

    var lit = mutableStateOf(0)
    var garbage = false
    val forward = line.fromId == firstId

    fun nextLight(){

        lit.value+= abs(step)
        if (lit.value==line.allPoints.size){
            finished()
            garbage = true
        }
    }

    fun isLit(n: Int): Boolean {
        return if (step>0){
            if(forward){
                n<=lit.value
            }else{
                line.allPoints.size - lit.value <= n
            }
        }else{
            if(forward){
                line.allPoints.size - lit.value >= n
                //n>=lit.value
            }else{
                n>=lit.value
            }
        }

    }

}