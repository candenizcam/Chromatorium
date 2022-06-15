package com.pungo.chromatorium.fifthTest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pungo.chromatorium.R
import com.pungo.chromatorium.fifthTest.levelOpener.LevelSetOpener
import com.pungo.chromatorium.fifthTest.levelOpener.NodeType
import com.pungo.chromatorium.tools.*
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
                .fillMaxWidth()
                .height(hudBottom.dp)
                .align(Alignment.BottomCenter)) {
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {

                    // move counter
                    Row(modifier = Modifier
                        .fillMaxHeight()
                        .width(hudBottom.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                        Text(text ="${levelSetOpener.gameLevels[activeLevel].moveCounter.value}",
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
                        .background(Color.Green)
                        .clickable {
                            // TODO: HINT
                        }
                    ) {

                    }

                }
            }
            
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
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween){

                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .width((hudTop).dp)
                        .clickable {
                            // back button

                        }
                    ){
                        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {

                            drawCircle(Color.White, 3*36f/1080f*this.size.width ,Point(this.size.width*0.8,this.size.height/2.0).offset)

                            drawCircle(Color.White, 1.5f*36f/1080f*this.size.width ,Point(this.size.width*0.55,this.size.height/2.0).offset)

                            drawCircle(Color.White, 1.5f*36f/1080f*this.size.width ,Point(this.size.width*0.4,this.size.height/2.0).offset)

                            drawCircle(Color.White, 1.5f*36f/1080f*this.size.width ,Point(this.size.width*0.25,this.size.height/2.0).offset)

                            drawCircle(Color.White, 1.5f*36f/1080f*this.size.width ,Point(this.size.width*0.1,this.size.height/2.0).offset)


                        })



                    }


                    val levelChromini = levelSetOpener.gameLevels[activeLevel].levelData.levelEllipses.firstOrNull { it.nodeType==NodeType.SINK }?.fillColour.let {
                        if (it==null){

                            Chromini.white
                        }else{
                            Chromini.fromHex(it)
                        }

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