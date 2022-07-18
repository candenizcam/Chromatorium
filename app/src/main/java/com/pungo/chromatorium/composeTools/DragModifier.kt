package com.pungo.chromatorium.tools

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput


fun dragModifier(firstContact: (Float, Float)->Unit, dragStart: (Float, Float)->Unit,drag: (Float, Float)->Unit, dragEnd: ()->Unit): Modifier {
    return Modifier
        .pointerInput(Unit) {
            val h = this.size.height
            val w = this.size.width


            detectTapGestures(onPress = {
                val x = it.x//-left
                val y = it.y// - top
                firstContact(x / w, y / h)
            })


        }
        .pointerInput(Unit) {
            val h = this.size.height
            val w = this.size.width

            detectDragGestures(onDragEnd = {
                dragEnd()
            }, onDragStart = {
                dragStart(it.x / w, it.y / h)
            }) { change, dragAmount ->
                change.consumeAllChanges()
                drag(dragAmount.x / w, dragAmount.y / h)
            }
        }
}