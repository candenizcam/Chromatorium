package com.pungo.chromatorium.game

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.R
import com.pungo.chromatorium.game.levelOpener.LevelSetOpener
import com.pungo.chromatorium.game.levelOpener.NodeType
import com.pungo.chromatorium.game.ui.BottomHud
import com.pungo.chromatorium.game.ui.TopHud
import com.pungo.chromatorium.tools.*
import kotlinx.coroutines.delay

@Composable
fun GameApp() {
    var loadedString by remember {
        mutableStateOf(false)
    }
    val levelSetOpener by remember{
        mutableStateOf(
            LevelSetOpener()
        )
    }
    var activeLevel by rememberSaveable{
        mutableStateOf(0)
    }
    var timeRecorder = rememberSaveable() {
        mutableStateOf(0.0)
    }

    val hudTop = height1920(v = 200)
    val hudBottom = height1920(v = 100)


    val screenSize = Size(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp-hudTop.toInt()-hudBottom.toInt())


    ReadDataFileWithCallback(path = "batches/batch_1.txt"){
        levelSetOpener.deploy(it)
        levelSetOpener.generateGameLevels(screenSize)
        loadedString = true
        //activeLevel = 0
    }


    LaunchedEffect(Unit) {
        while(true) {
            delay(50)
            try {
                levelSetOpener.gameLevels[activeLevel].blingHolders.forEach { it.nextLight() }
                levelSetOpener.gameLevels[activeLevel].blingHolders.removeAll { it.garbage }
                if(!levelSetOpener.gameLevels[activeLevel].levelCompleted.value){
                    timeRecorder.value += 0.05
                }


            }catch (_: Exception){

            }

        }
    }






    if(!loadedString){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "loading")
        }


    }else{
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {


            drawBackground(.9f, .15f)

            // bottom hud
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(hudBottom.dp)
                .align(Alignment.BottomCenter)) {
                BottomHud(hudBottom = hudBottom, moveCounter = levelSetOpener.gameLevels[activeLevel].moveCounter.value)
            }

            // game
            Box(modifier = Modifier
                .fillMaxSize()
                , contentAlignment = Alignment.BottomCenter) {
                drawGameV(gameLevel = levelSetOpener.gameLevels[activeLevel])
            }
            val levelChromini = levelSetOpener.gameLevels[activeLevel].levelData.levelEllipses.firstOrNull { it.nodeType==NodeType.SINK }?.fillColour.let {
                if (it==null){
                    Chromini.white
                }else{
                    Chromini.fromHex(it)
                }
            }
            // top hud
            Box(modifier = Modifier
                .height(hudTop.dp)
                .fillMaxWidth()
                , contentAlignment = Alignment.Center){


                TopHud(hudTop = hudTop, levelChromini = levelChromini, activeLevel = activeLevel)
            }


            if (levelSetOpener.gameLevels[activeLevel].levelCompleted.value){

                BetweenLevels(timeRecorder.value, activeLevel+1, levelSetOpener.gameLevels[activeLevel].moveCounter.value,levelChromini,3,
                    nextLevel = {
                        timeRecorder.value = 0.0
                        activeLevel += 1
                        if (activeLevel >= levelSetOpener.gameLevels.size) {
                            activeLevel = 0
                        }
                        levelSetOpener.gameLevels[activeLevel].resetLevel()
                    },
                    restart = {
                        timeRecorder.value = 0.0

                        levelSetOpener.gameLevels[activeLevel].resetLevel()
                    }
                )

            }


        }

    }
}


@Composable
fun BetweenLevels(timeRecorder: Double, levelNo: Int, moves: Int, chromini: Chromini, stars: Int, nextLevel: ()->Unit, restart: ()->Unit){

    val stroke = Stroke(width = 12f,
        //pathEffect = PathEffect.stampedPathEffect(shape = )

        pathEffect = PathEffect.stampedPathEffect(Path().apply {
            addOval(Rect(Offset(-5f,-5f),Offset(5f,5f)))
        },18f,0f, StampedPathEffectStyle.Rotate)
        //pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 18f), 0f)
    )


    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0.05f, 0.05f, 0.05f, 0.20f)), contentAlignment = Alignment.Center) {

        Box(Modifier
            .size(width1080(v = 700).dp, height1920(v = 926).dp)
            //.background(Color(15, 9, 9))
            //.clip(RoundedCornerShape(width1080(36).dp))

        ){
            val cr = width1080(36).toFloat()

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(color = Color(15, 9, 9), cornerRadius = CornerRadius(cr,cr),
                )
                drawRoundRect(color = chromini.generateColour(),style = stroke, cornerRadius = CornerRadius(cr,cr),
                )

            }

            Column(
                Modifier.padding(width1080(v = 52.5).dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                //val vector = ImageVector.vectorResource(id = R.drawable.ic_spark)
                //val painter = rememberVectorPainter(image = vector)
                val ib = ImageVector.vectorResource(R.drawable.ic_spark)
                val painter = rememberVectorPainter(image = ib)
                Row(modifier = Modifier.size(width1080(v = 270).dp, height1920(v = 270).dp)){
                    Column(modifier = Modifier.width(width1080(v = 90).dp).fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                        Image(painter = painter, contentDescription = "s1")
                    }
                    Column(modifier = Modifier.width(width1080(v = 90).dp).fillMaxHeight(), verticalArrangement = Arrangement.Top) {
                        Image(painter = painter, contentDescription = "s2")
                    }
                    Column(modifier = Modifier.width(width1080(v = 90).dp).fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                        Image(painter = painter, contentDescription = "s3")
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
                    .padding(0.dp, height1920(v = 12).dp,0.dp, height1920(v = 48).dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextDecorator(text = "Time", fontSize = 56)
                    TextDecorator(text = "%.1f".format(timeRecorder), fontSize = 56)
                }

                Row(Modifier.width(width1080(v = 520).dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        Modifier
                            .clickable {
                                // TODO EYE
                            }
                    ) {
                        Image(
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_eye)), "eye"
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
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_restart)), "restart"
                        )
                    }



                    Box(
                        Modifier
                            .clickable {
                                nextLevel()

                            }
                    ) {
                        Image(
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_next)), "next"
                        )
                        //Text(text = "NXT",color = Color.White, fontSize = 24.sp)
                    }
                }


            }
        }


    }
}

