package com.pungo.chromatorium.game

import androidx.compose.runtime.*
import com.pungo.chromatorium.game.levelData.LevelData
import com.pungo.chromatorium.game.levelOpener.NodeType
import com.pungo.chromatorium.tools.Size
import java.util.*

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

    fun getStars(): Int {
        val v = moveCounter.value
        return if (v>levelData.star2){
            1
        }else if(v>levelData.star3){
            2
        }else{
            3
        }
    }


    fun addBlinger(b: BlingHolder){
        val b2 = blingHolders.firstOrNull { it.line == b.line }
        if (b2!=null){
            blingHolders.remove(b2)
            if( b.firstId == b2.firstId){

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