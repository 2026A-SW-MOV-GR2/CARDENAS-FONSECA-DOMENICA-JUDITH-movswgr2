package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun getAppString(): String

@Composable
expect fun getAppTextColor(): Color

@Composable
expect fun getAppBackgroundColor(): Color
