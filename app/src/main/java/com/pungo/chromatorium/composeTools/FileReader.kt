package com.pungo.chromatorium.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

@Composable
fun ReadDataFileWithCallback(path: String, launchKey: Boolean=true, callback: (String)->Unit={}){

    val context = LocalContext.current
    LaunchedEffect(launchKey) {
        kotlin.runCatching {
            val inputStream: InputStream = context.assets.open(path)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            String(buffer)
        }.onSuccess {
            callback(it)
        }.onFailure {
            callback(it.message.toString())
        }

    }
}