package com.pungo.chromatorium.game

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
    val hudTop = height1920(v = 200)
    val hudBottom = height1920(v = 100)
    val screenSize = Size(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp-hudTop.toInt()-hudBottom.toInt())
    //var loadedString by remember {
    //    mutableStateOf(false)
    //}
    var activeLevelComplete by remember{
        mutableStateOf(false)
    }
    var activeLevelMoveCounter by remember{
        mutableStateOf(0)
    }
    var activeLevelBlingers = remember{
        mutableStateListOf<BlingHolder>()
    }

    val levelSetOpener by remember{
        mutableStateOf(
            LevelSetOpener(LevelStrings.levelList[0],screenSize, levelMutation = { mutator ->
                activeLevelComplete = mutator.levelCompleted
                activeLevelMoveCounter = mutator.moveCounter
                activeLevelBlingers.clear()
                activeLevelBlingers.addAll(mutator.blingHolders)

            })

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




    LaunchedEffect(Unit) {
        while(true) {
            delay(50)
            try {
                levelSetOpener.updateBlingers(activeLevel)
                if(!levelSetOpener.activeLevelComplete(activeLevel)){
                    timeRecorder.value += 0.05
                }


            }catch (_: Exception){

            }

        }
    }






    if(false){
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
                BottomHud(hudBottom = hudBottom, moveCounter = activeLevelMoveCounter)
            }

            // game
            Box(modifier = Modifier
                .fillMaxSize()
                , contentAlignment = Alignment.Center) {
                drawGameV(gameLevel = levelSetOpener.gameLevels[activeLevel], activeLevelBlingers)
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


                TopHud(hudTop = hudTop, levelChromini = levelChromini, activeLevel = activeLevel, activeTitle = levelSetOpener.gameLevels[activeLevel].levelData.levelTitle)
            }


            if (activeLevelComplete){

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
                    BetweenLevels(timeRecorder.value, activeLevel+1, activeLevelMoveCounter,levelChromini,levelSetOpener.gameLevels[activeLevel].getStars(),
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




