package org.julienjnnqin.luxmateapp

import androidx.compose.ui.window.ComposeUIViewController
import org.julienjnnqin.luxmateapp.di.initializeKoinIos

fun MainViewController() = ComposeUIViewController(
    configure = {
        // On initialise Koin avant que le premier écran ne s'affiche
        initializeKoinIos()
    }
) {
    App()
}