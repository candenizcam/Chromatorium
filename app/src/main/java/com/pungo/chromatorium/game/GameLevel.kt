package com.pungo.chromatorium.game

import com.pungo.chromatorium.game.levelData.LevelData
import com.pungo.chromatorium.game.levelData.LevelLineData
import com.pungo.chromatorium.game.levelOpener.NodeType
import com.pungo.chromatorium.tools.Size
import java.util.*

class GameLevel(val levelData: LevelData, val gameFieldSize: Size,
                val mutation: (Mutator) -> Unit
) {
    val levelSize: Size = levelData.levelSize.scaleToFit(gameFieldSize)
    //val blingHolders =  mutableStateListOf<BlingHolder>() //mutableListOf<BlingHolder>()
    val blingHolders = mutableListOf<BlingHolder>()

    val gameNetwork = GameNetwork(
        levelData.levelEllipses.map {
            it.fillColour
        },
        levelData.levelEllipses.map{
            it.nodeType
        }
    )
    var moveCounter = 0
    set(value) {
        field = value
        mutation(Mutator(moveCounter,levelCompleted, blingHolders.toList()))
    }
    //var levelCompleted = mutableStateOf(false)
    var levelCompleted = false
    set(value) {
        field = value
        mutation(Mutator(moveCounter,levelCompleted, blingHolders.toList()))
    }

    data class Mutator(val moveCounter: Int, val levelCompleted: Boolean, val blingHolders: List<BlingHolder>)



    init {



    }

    fun getStars(): Int {
        val v = moveCounter
        return if (v>levelData.star2){
            1
        }else if(v>levelData.star3){
            2
        }else{
            3
        }
    }


    fun addBlinger(lin: LevelLineData, thisId: String, otherId: String, step: Int,finished: ()->Unit ){
        val b = BlingHolder(lin,thisId, otherId, step , mutated = {
            mutation(Mutator(moveCounter,levelCompleted, blingHolders.toList()))

        },finished)


        val b2 = blingHolders.firstOrNull { it.line == b.line }
        if (b2!=null){
            blingHolders.remove(b2)
            mutation(Mutator(moveCounter,levelCompleted, blingHolders.toList()))
            if( b.firstId == b2.firstId){

                if ((b.step >0).xor(b2.step>0)){
                    b.lit = b.line.allPoints.size - b2.lit
                }else{
                    b.lit = b2.lit
                }


            }
        }
        blingHolders.add(
            b

        )
        mutation(Mutator(moveCounter,levelCompleted, blingHolders.toList()))
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
        levelCompleted = false
        moveCounter = 0
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
        levelCompleted = levelComplete()
    }

    fun updateBlingers(){
        blingHolders.forEach { it.nextLight() }
        blingHolders.removeAll { it.garbage }
        mutation(Mutator(moveCounter, levelCompleted, blingHolders.toList()))
    }
}