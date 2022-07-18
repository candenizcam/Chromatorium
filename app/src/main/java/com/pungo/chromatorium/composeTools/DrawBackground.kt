package com.pungo.chromatorium.tools

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun drawBackground(s: Float = .3f, v: Float = .7f, duration: Int = 36000){
    val infiniteTransition = rememberInfiniteTransition()
    val value = infiniteTransition.animateFloat(
        initialValue =0.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration,easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Chromini
                .fromHSV(value.value, s, v)
                .generateColour()
        )){
    }
}