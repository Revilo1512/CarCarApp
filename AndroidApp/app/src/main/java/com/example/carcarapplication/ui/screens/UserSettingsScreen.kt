package com.example.carcarapplication.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun UserSettingsScreen() {
    val alpha = remember { Animatable(1f) }

    // Infinite repeatable animation for the alpha value
    LaunchedEffect(Unit) {
        while (true) {
            alpha.animateTo(
                targetValue = 0f, // Fade out
                animationSpec = tween(durationMillis = 500)
            )
            alpha.animateTo(
                targetValue = 1f, // Fade in
                animationSpec = tween(durationMillis = 500)
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "HELP ME, I'M BEING HELD AGAINST MY WILL AT 49.077791285109775, 9.964634952272702",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Red,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alpha.value)
        )
    }
}