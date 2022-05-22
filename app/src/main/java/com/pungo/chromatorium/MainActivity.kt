package com.pungo.chromatorium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.pungo.chromatorium.fourthTest.FourthTest
import com.pungo.chromatorium.ui.theme.ChromatoriumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {

            //DragTest()
            //SecondTest()
            //ThirdTest()
            FourthTest()

        }
    }
}




@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChromatoriumTheme {
        Greeting("Android")
    }
}