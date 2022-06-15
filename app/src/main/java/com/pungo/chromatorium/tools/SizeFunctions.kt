package com.pungo.chromatorium.tools
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun width1080(v: Double): Double {
    return v/1080.0*LocalConfiguration.current.screenWidthDp
}

@Composable
fun width1080(v: Float): Double {
    return width1080(v = v.toDouble())
}

@Composable
fun width1080(v: Int): Double {
    return width1080(v = v.toDouble())
}

@Composable
fun height1920(v: Double): Double {
    return v/1920.0*LocalConfiguration.current.screenHeightDp
}

@Composable
fun height1920(v: Float): Double{
    return height1920(v = v.toDouble())
}

@Composable
fun height1920(v: Int): Double{
    return height1920(v = v.toDouble())
}