package com.pungo.chromatorium.fourthTest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.pungo.chromatorium.game.Level
import com.pungo.chromatorium.tools.ReadDataFileWithCallback
import kotlinx.coroutines.launch

@Composable
fun FourthTest() {
    //val level by remember{ mutableStateOf(CardLevel.testLevel()) }
    val scope = rememberCoroutineScope()

    var level = remember {
        mutableStateOf(CardLevel.testLevel())
    }
    var loaded by remember {
        mutableStateOf(false)
    }
    var batchString = remember {
        mutableStateOf("")
    }

    var levels = listOf< BatchReader.LevelData>()

    BatchReader.dataReader(path = "batches/inp_Chroma_5.txt"){
        batchString.value = it
        loaded = true
    }

    if(batchString.value!=""){
        levels = BatchReader.stringToClasses(batchString.value)

        scope.launch {

            level.value = CardLevel.fromBatchReader(
                levels[0].height.toInt(),
                levels[0].width.toInt(),
                levels[0].ellipses,
                levels[0].lines
            )
            loaded = true
        }




    }




    /*
    BatchReader.batchReader(path = "batches/inp_Chroma_4.txt" ){
        scope.launch {
            level.value = it
            loaded = true
        }

    }

 */
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if (loaded){
            level.value.draw(frameWidth = LocalConfiguration.current.screenWidthDp.toFloat(), frameHeight = LocalConfiguration.current.screenHeightDp.toFloat())
        }

        //level.draw( LocalConfiguration.current.screenWidthDp.toFloat(), LocalConfiguration.current.screenHeightDp.toFloat())
    }





}