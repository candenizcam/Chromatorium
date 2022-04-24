package com.pungo.chromatorium.game

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

class Shape(val sideNumber: Int){

    fun getPath(centre: Point, radius: Float): Path {
        if(sideNumber>2){
            val step = 2* Math.PI /sideNumber
            val path = Path().apply {
                moveTo(centre.x.toFloat()+radius.toFloat(),centre.y.toFloat())
                for(i in 1..sideNumber){
                    lineTo(centre.x.toFloat()+(radius* kotlin.math.cos(i * step)).toFloat(),centre.y.toFloat()+ (radius* kotlin.math.sin(i * step)).toFloat())
                }
                close()
            }
            return path
        }else{
            return Path().apply {
                this.addArc(Rect(centre.offset, radius = radius.toFloat()),0f,360f)
            }

        }


    }
}