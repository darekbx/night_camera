package com.darekbx.nightcamera.commands

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.statement.bodyAsText
import org.jetbrains.skia.Image
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TakePhoto : Caller() {

    /**
     * @param exposureTime - exposure time in seconds
     */
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(exposureTime: Int): ImageBitmap? {
        val response = executeCommand("photo?exposure_time=$exposureTime")
        val body = response?.bodyAsText()
        val prefix = "--start--"
        val sufix = "--end--"
        val image64 = body?.substringAfter(prefix)?.substringBefore(sufix)
        val imageBytes = image64?.let { Base64.decode(it) }

        return Image
            .makeFromEncoded(imageBytes!!)
            .toComposeImageBitmap()
    }
}
