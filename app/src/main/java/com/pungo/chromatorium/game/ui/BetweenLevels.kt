package com.pungo.chromatorium.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.pungo.chromatorium.R
import com.pungo.chromatorium.tools.Chromini
import com.pungo.chromatorium.tools.TextDecorator
import com.pungo.chromatorium.tools.height1920
import com.pungo.chromatorium.tools.width1080

@Composable
fun BetweenLevels(timeRecorder: Double, levelNo: Int, moves: Int, chromini: Chromini, stars: Int, nextLevel: ()->Unit, restart: ()->Unit, eye: ()->Unit){

    val stroke = Stroke(width = 12f,
        //pathEffect = PathEffect.stampedPathEffect(shape = )

        pathEffect = PathEffect.stampedPathEffect(Path().apply {
            addOval(Rect(Offset(-5f,-5f), Offset(5f,5f)))
        },18f,0f, StampedPathEffectStyle.Rotate)
        //pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 18f), 0f)
    )


    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0.05f, 0.05f, 0.05f, 0.50f)), contentAlignment = Alignment.Center) {

        Box(
            Modifier
            .size(width1080(v = 700).dp, height1920(v = 926).dp)

        ){
            val cr = width1080(36).toFloat()

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(color = Color(15, 9, 9), cornerRadius = CornerRadius(cr,cr),
                )
                drawRoundRect(color = chromini.generateColour(),style = stroke, cornerRadius = CornerRadius(cr,cr),
                )

            }

            Column(
                Modifier.padding(width1080(v = 52.5).dp,6.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                // 1: middle
                // 2: centre
                // 3: right
                //val vector = ImageVector.vectorResource(id = R.drawable.ic_spark)
                //val painter = rememberVectorPainter(image = vector)
                val ib = ImageVector.vectorResource(R.drawable.ic_spark)
                val painter = rememberVectorPainter(image = ib)
                Row(modifier = Modifier.size(width1080(v = 270).dp, height1920(v = 300).dp)){
                    Column(modifier = Modifier
                        .width(width1080(v = 90).dp)
                        .fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                        if (stars>1){
                            Image(painter = painter, contentDescription = "s1")
                        }

                    }
                    Column(modifier = Modifier
                        .width(width1080(v = 90).dp)
                        .fillMaxHeight(), verticalArrangement = Arrangement.Top) {
                        Image(painter = painter, contentDescription = "s2")
                    }
                    Column(modifier = Modifier
                        .width(width1080(v = 90).dp)
                        .fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                        if (stars>2){
                            Image(painter = painter, contentDescription = "s3")
                        }

                    }
                }




                TextDecorator(text = "Perfect!", fontSize = 96)
                TextDecorator(text = "Level ${levelNo} Complete!", fontSize = 56, modifier = Modifier.padding(0.dp, 0.dp, 0.dp, height1920(v = 36).dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, height1920(v = 12).dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextDecorator(text = "Moves", fontSize = 56)
                    TextDecorator(text = moves.toString(), fontSize = 56)
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, height1920(v = 12).dp, 0.dp, height1920(v = 48).dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextDecorator(text = "Time", fontSize = 56)
                    TextDecorator(text = "%.1f".format(timeRecorder), fontSize = 56)
                }

                Row(Modifier.width(width1080(v = 520).dp).height(height1920(v = 120).dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        Modifier
                            .clickable {
                                eye()
                            }
                    ) {
                        Image(
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_eye)), "eye", contentScale = ContentScale.Fit
                        )
                        //Text(text = "AY",color = Color.White, fontSize = 24.sp)
                    }

                    Box(
                        Modifier
                            .clickable {
                                restart()
                            }
                    ) {
                        Image(
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_restart)), "restart", contentScale = ContentScale.Fit
                        )
                    }



                    Box(
                        Modifier
                            .clickable {
                                nextLevel()

                            }
                    ) {
                        Image(
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_next)), "next", contentScale = ContentScale.Fit
                        )
                    }
                }


            }
        }


    }
}