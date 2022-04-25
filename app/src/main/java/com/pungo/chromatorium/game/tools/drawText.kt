package com.pungo.chromatorium.game.tools

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.nativeCanvas

fun drawText(drawContext: DrawContext, text: String, x: Float, y: Float, align: Paint.Align= Paint.Align.CENTER, color: Color = Color.White, typeface: Typeface? = null){
    val paint = Paint()
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 28f
    paint.color = color.hashCode()
    if(typeface!=null){
        paint.typeface = typeface
    }

    drawContext.canvas.nativeCanvas.drawText(
        text,
        x, y, paint
    )
}