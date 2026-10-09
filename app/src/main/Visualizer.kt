package com.radiomaroc.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BigVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 24,
    barWidth: Dp = 8.dp,
    barSpacing: Dp = 3.dp,
    maxHeight: Dp = 200.dp,
    color: Color = Color.White
) {
    val transition = rememberInfiniteTransition(label = "big_wave")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(barSpacing),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(barCount) { index ->
            val baseMultiplier = if (index in (barCount / 3)..(barCount * 2 / 3)) 1f else 0.7f
            val height by transition.animateFloat(
                initialValue = 0.15f,
                targetValue = if (isPlaying) baseMultiplier else 0.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 250 + (index % 5) * 80,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar$index"
            )
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(maxHeight * height)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
        }
    }
}
