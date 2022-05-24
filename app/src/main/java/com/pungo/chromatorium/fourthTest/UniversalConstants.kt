package com.pungo.chromatorium.fourthTest

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object UniversalConstants {

    val smallLedRadius= 8f
    val largeLedRadius = 22f

    @Composable
    fun widthScaled(f: Float): Float {
        return f/360f* LocalConfiguration.current.screenWidthDp
    }

    @Composable
    fun heightScaled(f: Float): Float {
        return f/640f* LocalConfiguration.current.screenHeightDp
    }
}