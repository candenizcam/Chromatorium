package com.pungo.chromatorium.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.R
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.height1920
import com.pungo.chromatorium.tools.width1080

@Composable
fun BoxScope.TopHud(hudTop: Double, levelChromini: Chromini, activeLevel: Int){
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween){

        Box(modifier = Modifier
            .fillMaxHeight()
            .width((hudTop).dp)
            .clickable {
                // back button

            }
        ){
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {

                drawCircle(Color.White, 3*36f/1080f*this.size.width , Point(this.size.width*0.8,this.size.height/2.0).offset)

                drawCircle(
                    Color.White, 1.5f*36f/1080f*this.size.width ,
                    Point(this.size.width*0.55,this.size.height/2.0).offset)

                drawCircle(
                    Color.White, 1.5f*36f/1080f*this.size.width ,
                    Point(this.size.width*0.4,this.size.height/2.0).offset)

                drawCircle(
                    Color.White, 1.5f*36f/1080f*this.size.width ,
                    Point(this.size.width*0.25,this.size.height/2.0).offset)

                drawCircle(
                    Color.White, 1.5f*36f/1080f*this.size.width ,
                    Point(this.size.width*0.1,this.size.height/2.0).offset)
            })



        }


        Column(modifier = Modifier
            .fillMaxHeight()
            .width(width1080(306).dp)
            .clip(RoundedCornerShape(0.dp, 0.dp, width1080(36).dp, width1080(36).dp))
            .background(
                levelChromini.generateColour()
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text("LEVEL ${activeLevel+1}", fontFamily = FontFamily(
                Font(R.font.sharetechmonoregular, FontWeight.Normal)
            ),
                fontSize = height1920(v = 36).sp,
                color = if(levelChromini.useLightText) Color.White else Color.Black
            )

            Text(levelChromini.hexString,
                fontFamily = FontFamily(
                    Font(R.font.sharetechmonoregular, FontWeight.Normal)
                ),
                fontSize = height1920(v = 72).sp,
                letterSpacing = width1080(-10).sp,
                color = if(levelChromini.useLightText) Color.White else Color.Black
            )
        }


        Box(modifier = Modifier
            .fillMaxHeight()
            .width((hudTop).dp)
        ) {
        }
    }
}