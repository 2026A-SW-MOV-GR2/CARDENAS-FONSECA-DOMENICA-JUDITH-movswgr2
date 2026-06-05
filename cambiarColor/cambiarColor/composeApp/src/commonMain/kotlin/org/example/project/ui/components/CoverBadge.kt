package org.example.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.model.CoverTone

@Composable
fun CoverBadge(
    letter: String,
    tone: CoverTone,
    modifier: Modifier = Modifier,
) {
    val bg = coverToneColor(tone)
    val fg = coverToneOnColor(tone)

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = letter,
            color = fg,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun coverToneColor(tone: CoverTone): Color {
    val scheme = MaterialTheme.colorScheme
    return when (tone) {
        CoverTone.Primary -> scheme.primary
        CoverTone.Secondary -> scheme.secondary
        CoverTone.Tertiary -> scheme.tertiary
        CoverTone.Error -> scheme.error
        CoverTone.SurfaceVariant -> scheme.surfaceVariant
    }
}

@Composable
fun coverToneOnColor(tone: CoverTone): Color {
    val scheme = MaterialTheme.colorScheme
    return when (tone) {
        CoverTone.Primary -> scheme.onPrimary
        CoverTone.Secondary -> scheme.onSecondary
        CoverTone.Tertiary -> scheme.onTertiary
        CoverTone.Error -> scheme.onError
        CoverTone.SurfaceVariant -> scheme.onSurfaceVariant
    }
}
