package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import org.example.project.R // IMPORTANTE: Importa la clase R generada de tu proyecto

@Composable
actual fun getPlatformDynamicResources(): DynamicResources {
    // Android resolverá automáticamente la carpeta correcta (values-en, values-land, etc.)
    return DynamicResources(
        text = stringResource(id = R.string.texto_saludo),
        textColor = colorResource(id = R.color.color_texto),
        backgroundColor = colorResource(id = R.color.color_fondo)
    )
}