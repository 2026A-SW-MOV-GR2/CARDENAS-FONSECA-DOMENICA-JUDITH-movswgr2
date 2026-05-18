package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

interface AppResources {
    @Composable
    fun getLabel(): String

    @Composable
    fun getTextColor(): Color

    @Composable
    fun getBackgroundColor(): Color
}

@Composable
expect fun rememberAppResources(): AppResources
