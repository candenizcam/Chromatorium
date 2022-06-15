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
import androidx.compose.ui.layout.ContentScale
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
import com.pungo.chromatorium.game.ui.BetweenLevels
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
    var eye = remember {
        mutableStateOf(false)
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

                if (eye.value){
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable {
                                eye.value = false
                            }, contentAlignment = Alignment.Center) {
                        Image(
                            rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_eye_big)),
                            "next",
                            colorFilter = ColorFilter.tint(Color(1f,1f,1f,0.2f)),
                            contentScale = ContentScale.Fit
                        )

                    }
                }else{
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
                        },
                        eye = {
                            eye.value = true
                        }
                    )



                }


            }


        }

    }
}




