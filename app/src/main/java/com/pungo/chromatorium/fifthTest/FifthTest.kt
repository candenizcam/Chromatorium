package com.pungo.chromatorium.fifthTest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.fifthTest.levelOpener.LevelSetOpener
import com.pungo.chromatorium.tools.ReadDataFileWithCallback
import com.pungo.chromatorium.tools.Size

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
    var activeLevel by remember {
        mutableStateOf(-1)
    }
    val screenSize = Size(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp-120 )


    ReadDataFileWithCallback(path = "batches/inp_Chroma_6.txt"){
        levelSetOpener.deploy(it)
        levelSetOpener.generateGameLevels(screenSize)
        loadedString = true
        activeLevel = 0
    }


    if(loadedString){




    }

    if(activeLevel==-1){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "loading")
        }


    }else{
        Box(modifier = Modifier
            .fillMaxSize()

            .clickable {
                activeLevel += 1
                if (activeLevel >= levelSetOpener.gameLevels.size) {
                    activeLevel = 0
                }
            }

                ,
            contentAlignment = Alignment.TopCenter
        ) {



            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black), contentAlignment = Alignment.BottomCenter) {
                //Text(text = "loaded")
                levelSetOpener.gameLevels[activeLevel].draw()
            }

            Box(modifier = Modifier.height(120.dp).fillMaxWidth().background(Color.Black), contentAlignment = Alignment.Center){
                Text(text = "level ${activeLevel+1}",color = Color.White, fontSize = 24.sp)
            }


        }

    }






}