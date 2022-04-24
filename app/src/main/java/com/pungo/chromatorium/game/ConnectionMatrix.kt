package com.pungo.chromatorium.game

class ConnectionMatrix(val nodeNo: Int){
    private val values = MutableList<Int>(nodeNo*nodeNo){
        0
    }

    operator fun get(row: Int, col: Int): Int {
        return values[(row-1)*nodeNo+col-1]
    }

    operator fun set(row: Int, col: Int, n: Int){
        values[(row-1)*nodeNo+col-1] = n
    }

    fun getRow(r: Int): List<Int> {
        return (1..nodeNo).map { this[r,it] }
    }

    fun getColumn(r: Int): List<Int> {
        return (1..nodeNo).map { this[it,r] }
    }

    fun getConnections(): MutableList<Triple<Int, Int, Int>> {
        val l = mutableListOf<Triple<Int,Int, Int>>()
        for(i in 1..nodeNo){
            for(j in 1..nodeNo){
                if(this[i,j]>0){
                    l.add(Triple(i,j,this[i,j]))
                }
            }
        }
        return l
    }
}
