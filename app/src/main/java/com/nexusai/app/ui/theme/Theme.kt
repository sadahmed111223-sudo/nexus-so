package com.nexusai.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NexusColors = lightColorScheme(
    primary = Color(0xFF087A50),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4F5E5),
    onPrimaryContainer = Color(0xFF003824),
    secondary = Color(0xFF416A58),
    background = Color(0xFFF7F9F7),
    surface = Color.White
)

@Composable
fun NexusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NexusColors,
        content = content
    )
}