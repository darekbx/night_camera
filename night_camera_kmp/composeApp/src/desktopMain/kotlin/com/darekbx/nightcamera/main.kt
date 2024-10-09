package com.darekbx.nightcamera

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NightCamera",
    ) {
        App()
    }
}