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
fun SoundWaveVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 4,
    barWidth: Dp = 3.dp,
    barSpacing: Dp = 2.dp,
    maxHeight: Dp = 20.dp,
    color: Color = Color.White
) {
    val transition = rememberInfiniteTransition(label = "wave")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(barSpacing),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(barCount) { index ->
            val height by transition.animateFloat(
                initialValue = 0.2f,
                targetValue = if (isPlaying) 1f else 0.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 300 + index * 100,
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
                    .clip(RoundedCornerShape(1.dp))
                    .background(color)
            )
        }
    }
}
