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
}
