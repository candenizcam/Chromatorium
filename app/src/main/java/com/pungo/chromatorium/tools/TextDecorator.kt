package com.pungo.chromatorium.tools

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.R

@Composable
fun TextDecorator(text: String, color: Color = Color.White, fontNo: Int= R.font.sharetechmonoregular, fontSize: Int=36, letterSpacing: Int=0, modifier: Modifier = Modifier){
    Text(modifier=modifier,
        text = text,
        color = color,
        fontFamily = FontFamily(
            Font(fontNo, FontWeight.Normal)
        ),
        fontSize = height1920(v = fontSize).sp,
        letterSpacing = width1080(letterSpacing).sp
    )
}