package com.pungo.chromatorium.game

import androidx.compose.ui.graphics.Color
import com.pungo.chromatorium.game.levelOpener.NodeType
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Matrix

class GameNetwork(chrominiList: List<String>, val typeList: List<NodeType>) {
    val networkSize = chrominiList.size
    var relationsMatrix = Matrix(networkSize,networkSize)
    var borderList = chrominiList.indices.map {
        when(typeList[it]){
            NodeType.NONE ->{ Chromini.white}
            else ->{Chromini.fromHex(chrominiList[it])}
        }
    }
    var sourceList = chrominiList.indices.map {
        when(typeList[it]){
            NodeType.SOURCE -> {Chromini.fromHex(chrominiList[it])}
            else -> { null }
        }
    }
    var activeColours = List(networkSize){Chromini.black}
    init {
        updateColours()

    }


    fun updateColours(){
        val pss = relationsMatrix.powerSeriesSum(20)
        var r = sourceList.map { it?:Chromini.black }

        typeList.forEachIndexed { index, nodeType ->

            if (nodeType ==NodeType.SOURCE){
                val v = Matrix(1,networkSize)
                v[1,index+1] = 1.0

                val m = v*pss
                r = MutableList(networkSize){
                    r[it] + sourceList[index]!! * m[1,it+1]
                }


            }

        }

        activeColours = r

    }

    /** n1 and n2 are not ordered
     *
     */
    fun getConnectionChromini(n1: Int, n2: Int): Chromini? {
        return if(relationsMatrix[n1+1,n2+1]!=0.0){
            getFillChromini(n1)
        }else if(relationsMatrix[n2+1,n1+1]!=0.0){
            getFillChromini(n2)
        }else{
            null
        }
    }

    fun getBorderChromini(n: Int): Chromini?{
        return borderList[n]
    }

    fun getFillChromini(n: Int): Chromini?{
        return activeColours[n]
    }

    fun getFillColor(n: Int): Color {
        return activeColours[n].generateColour()
    }



    fun connect(n1: Int, n2: Int){
        cutConnection(n1,n2)
        if (typeList[n1] == NodeType.SOURCE){
            relationsMatrix[n1+1,n2+1] = 1.0
        }else{
            relationsMatrix[n1+1,n2+1] = .5
        }
    }

    fun cutConnection(n1:Int,n2:Int){
        relationsMatrix[n1+1,n2+1] = 0.0
        relationsMatrix[n2+1,n1+1] = 0.0
    }


    fun resetConnections(){
        relationsMatrix = Matrix(networkSize,networkSize)
    }


}