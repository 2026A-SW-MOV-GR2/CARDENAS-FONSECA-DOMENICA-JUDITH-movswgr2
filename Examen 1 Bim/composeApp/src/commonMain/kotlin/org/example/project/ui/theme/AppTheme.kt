package org.example.project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Primary = Color(0xFF1B4965)
private val Secondary = Color(0xFFF25F5C)
private val Surface = Color(0xFFFFFBF5)
private val Background = Color(0xFFF3EEE6)
private val OnPrimary = Color(0xFFFFFFFF)
private val OnSecondary = Color(0xFFFFFFFF)
private val OnSurface = Color(0xFF1A1A1A)
private val OnBackground = Color(0xFF1A1A1A)

private val AppColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    surface = Surface,
    background = Background,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onSurface = OnSurface,
    onBackground = OnBackground
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp
    )
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}

@Composable
fun appBackgroundBrush(): Brush {
    return Brush.verticalGradient(
        listOf(
            Color(0xFFF4EFE7),
            Color(0xFFE7F1F8)
        )
    )
}
