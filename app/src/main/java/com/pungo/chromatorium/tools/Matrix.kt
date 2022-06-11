package com.pungo.chromatorium.tools

import java.util.DoubleSummaryStatistics
import kotlin.math.abs


open class Matrix(val values: MutableList<Double>, val rowNo: Int, val colNo: Int){
    constructor(rowNo: Int,colNo: Int): this(MutableList<Double>(rowNo*colNo){
        0.0
    }   , rowNo,colNo
    )






    operator fun get(row: Int, col: Int): Double {
        return values[(row-1)*colNo+col-1]
    }

    operator fun set(row: Int, col: Int, n: Double){
        values[(row-1)*colNo+col-1] = n
    }

    fun getRow(r: Int): List<Double> {
        return (1..colNo).map { this[r,it] }
    }

    fun getColumn(r: Int): List<Double> {
        return (1..rowNo).map { this[it,r] }
    }

    fun frobenius(): Double{
        return values.sumOf { abs(it) }
    }

    fun copy(): Matrix {
        return Matrix(values.map { it }.toMutableList(),rowNo,colNo)
    }

    fun setRow(r: Int, n: Double){
        (1..colNo).forEach {
            this[r,it] = n
        }
    }

    fun setCol(c: Int, n: Double){
        (1..rowNo).forEach {
            this[it,c] = n
        }
    }

    /** returns sum for A + A^2 + A^3 ...
     *
     */
    fun powerSeriesSum(n:Int): Matrix {
        if(rowNo!=colNo) Exception("Error, this function only works for square matrices")
        var u = Matrix(rowNo,colNo)
        var powerHolder = Matrix.unitMatrix(rowNo)
        for (i in 1..n){
            powerHolder = powerHolder * this
            if(powerHolder .frobenius()>0.001){
                u += powerHolder
            }else{
                break
            }
        }
        return u
    }



    operator fun plus(other: Matrix): Matrix {

        return Matrix(
        this.values.indices.map {
            values[it] + other.values[it]
        }.toMutableList(), rowNo, colNo
        )

    }

    operator fun minus(other: Matrix): Matrix{
        return Matrix(
            this.values.indices.map {
                values[it] - other.values[it]
            }.toMutableList(), rowNo, colNo
        )
    }

    operator fun times(other: Matrix): Matrix {
        if(colNo!=other.rowNo){
            throw  Exception("Matrix dimentions don't match")
        }
        val new = Matrix(rowNo = this.rowNo, colNo = other.colNo)
        for(i in 1..rowNo){
            for(j in 1.. other.colNo){
                var s  =0.0
                for (k in 1..colNo){
                    s += this[i,k]*other[k,j]
                }
                new[i,j] = s
            }
        }
        return new
    }

    operator fun times(other: Double): Matrix {
        return Matrix(this.values.map { it*other }.toMutableList(), rowNo, colNo)
    }

    override fun toString(): String {
        var s = ""
        for(i in 1..rowNo){
            for(j in 1.. colNo){
                s += this[i,j].toString() + ", "
            }
            s = s.substring(0..s.length-3) + "\n"
        }
        return s
    }


    companion object{
        fun unitMatrix(n: Int): Matrix {
            val m = Matrix(n,n)
            for(i in 1..n){
                m[i,i] = 1.0
            }
            return m
        }
    }

}