package com.pungo.chromatorium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.pungo.chromatorium.R
import com.pungo.chromatorium.game.GameApp
import com.pungo.chromatorium.tools.drawBackground

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {

            //DragTest()
            //SecondTest()
            //ThirdTest()
            //FourthTest()
            GameApp()
            //IzelinThingi()

        }
    }
}


@Composable
fun IzelinThingi(){
    Box(modifier = Modifier.fillMaxSize()){
        drawBackground(.9f, .15f)
        Image(painter =  painterResource(id = R.drawable.ic_arkaplan___14),"aaa", contentScale = ContentScale.Fit)
    }

}


