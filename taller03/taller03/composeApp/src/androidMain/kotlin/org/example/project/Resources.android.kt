package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource

@Composable
actual fun getAppString(): String {
    return stringResource(R.string.saludo)
}

@Composable
actual fun getAppTextColor(): Color {
    return colorResource(R.color.color_text)
}

@Composable
actual fun getAppBackgroundColor(): Color {
    return colorResource(R.color.color_fondo)
}
