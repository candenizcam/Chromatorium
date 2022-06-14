package com.pungo.chromatorium.fifthTest

import android.content.Context
import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.pungo.chromatorium.fifthTest.levelData.LevelData
import com.pungo.chromatorium.fifthTest.levelOpener.NodeType
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.Size
import kotlinx.coroutines.delay
import java.util.*
import kotlin.time.seconds

class GameLevel(val levelData: LevelData, val gameFieldSize: Size) {
    val levelSize: Size = levelData.levelSize.scaleToFit(gameFieldSize)
    val blingHolders =  mutableStateListOf<BlingHolder>() //mutableListOf<BlingHolder>()

    val gameNetwork = GameNetwork(
        levelData.levelEllipses.map {
            it.fillColour
        },
        levelData.levelEllipses.map{
            it.nodeType
        }
    )
    var moveCounter = mutableStateOf(0)
    var levelCompleted = mutableStateOf(false)

    init {


    }


    fun addBlinger(b: BlingHolder){
        val b2 = blingHolders.firstOrNull { it.line == b.line }
        if (b2!=null){
            blingHolders.remove(b2)
            if( b.firstId == b2.firstId){
                val bt = b.step >0
                val bt2 = b2.step >0

                if ((b.step >0).xor(b2.step>0)){
                    b.lit.value = b.line.allPoints.size - b2.lit.value
                }else{
                    b.lit.value = b2.lit.value
                }


            }
        }
        blingHolders.add(
            b

        )
    }


    fun ellipseIdIndex(id: String): Int {
        levelData.levelEllipses.indexOfFirst { it.id==id }.also {
            if(it==-1){
                throw Exception("id not found in ellipses")
            }
            return it
        }
    }




    fun resetLevel(){
        gameNetwork.resetConnections()
        gameNetwork.updateColours()
        levelCompleted.value = false
        moveCounter.value = 0
    }



    fun levelComplete(): Boolean {
        var complete = true
        levelData.levelEllipses.forEachIndexed { index, it->
            if(it.nodeType==NodeType.SINK){
                val s = it.fillColour.lowercase(Locale.ENGLISH)
                gameNetwork.getFillChromini(index)?.hexString.let {it2->
                    when (it2) {
                        it.fillColour.lowercase(Locale.ENGLISH) -> {
                        }
                        else -> {
                            complete = false
                        }
                    }
                }


            }
        }
        return complete

    }


    fun updateColours(){
        gameNetwork.updateColours()
        levelCompleted.value = levelComplete()
    }

}