package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource

class AndroidAppResources : AppResources {
    @Composable
    override fun getLabel(): String = stringResource(R.string.saludo)

    @Composable
    override fun getTextColor(): Color = colorResource(R.color.color_text)

    @Composable
    override fun getBackgroundColor(): Color = colorResource(R.color.color_fondo)
}

@Composable
actual fun rememberAppResources(): AppResources {
    return remember { AndroidAppResources() }
}
