package com.github.rcd27.ytinsight

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "youtube-insight",
    ) {
        App()
    }
}