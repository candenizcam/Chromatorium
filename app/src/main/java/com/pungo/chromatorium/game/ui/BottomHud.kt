package com.pungo.chromatorium.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.R
import com.pungo.chromatorium.tools.height1920

@Composable
fun BoxScope.BottomHud(hudBottom: Double, moveCounter: Int){
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {

        // move counter
        Row(modifier = Modifier
            .fillMaxHeight()
            .padding(12.dp, 0.dp)
            , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

            Text(text ="MOVES: ${moveCounter}",
                fontFamily = FontFamily(
                    Font(R.font.sharetechmonoregular, FontWeight.Normal)
                ),
                fontSize = height1920(v = 56).sp,
                color = Color.White
            )
        }

        Box(modifier = Modifier
            .fillMaxHeight()
            .width(hudBottom.dp)
            .padding(4.dp)
            //.background(Color.Green)
            .clickable {
                // TODO: HINT
            }
        ) {
            Image(painter = painterResource(id = R.drawable.ic_hint), contentDescription = "hint", contentScale = ContentScale.Fit)
        }

    }
}

