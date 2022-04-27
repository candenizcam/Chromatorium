package com.pungo.chromatorium

import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.fonts.Font
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.pungo.chromatorium.game.*
import kotlin.math.roundToInt

@Composable
fun ThirdTest() {
    val level by remember{mutableStateOf(Level("", Level.ColorTextFormat.HEX))}
    //val level = Level("")

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        level.draw( LocalConfiguration.current.screenWidthDp.toFloat(),LocalConfiguration.current.screenHeightDp.toFloat())
    }


}










