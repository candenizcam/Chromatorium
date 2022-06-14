package com.pungo.chromatorium.fifthTest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.fifthTest.levelOpener.LevelSetOpener
import com.pungo.chromatorium.tools.ReadDataFileWithCallback
import com.pungo.chromatorium.tools.Size
import com.pungo.chromatorium.tools.drawBackground
import kotlinx.coroutines.delay

@Composable
fun FifthTest() {
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

    val hudTop = 200


    val screenSize = Size(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp-hudTop)


    ReadDataFileWithCallback(path = "batches/inp_Chroma_6.txt"){
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
            .fillMaxSize()



                ,
            contentAlignment = Alignment.TopCenter
        ) {


            drawBackground(.9f, .15f)

            Box(modifier = Modifier
                .fillMaxSize()
                , contentAlignment = Alignment.BottomCenter) {
                
                drawGameV(gameLevel = levelSetOpener.gameLevels[activeLevel])
                //levelSetOpener.gameLevels[activeLevel].draw(levelSetOpener.gameLevels[activeLevel].levelData)

            }

            Box(modifier = Modifier
                .height(hudTop.dp)
                .fillMaxWidth()
                , contentAlignment = Alignment.Center){
                var main_text = "level ${activeLevel+1}"
                if(levelSetOpener.gameLevels[activeLevel].levelCompleted.value){
                    main_text += " complete"
                }else{
                    main_text += " moves: ${levelSetOpener.gameLevels[activeLevel].moveCounter.value}"

                }
                Column() {
                    Text(text = main_text,color = Color.White, fontSize = 24.sp)


                }



            }


            if (levelSetOpener.gameLevels[activeLevel].levelCompleted.value){

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        Modifier
                            .size(300.dp, 300.dp)
                            .background(Color(0.2f, 0.4f, 0.3f))

                    ) {
                        Text(text = "Level Complete",color = Color.White, fontSize = 24.sp)

                        Box(
                            Modifier
                                .background(Color(0.4f, 0.5f, 0.4f))
                                .clickable {
                                    activeLevel += 1
                                    if (activeLevel >= levelSetOpener.gameLevels.size) {
                                        activeLevel = 0
                                    }
                                    levelSetOpener.gameLevels[activeLevel].resetLevel()
                                }
                        ) {
                            Text(text = "Next Level",color = Color.White, fontSize = 24.sp)
                        }

                        Box(
                            Modifier
                                .background(Color(0.4f, 0.5f, 0.4f))
                                .clickable {

                                    levelSetOpener.gameLevels[activeLevel].resetLevel()
                                }
                        ) {
                            Text(text = "Restart",color = Color.White, fontSize = 24.sp)
                        }
                    }
                }

            }


        }

    }








}