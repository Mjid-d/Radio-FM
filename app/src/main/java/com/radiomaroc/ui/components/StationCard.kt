package com.radiomaroc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.radiomaroc.data.RadioStation
import com.radiomaroc.ui.theme.*

@Composable
fun StationCard(
    station: RadioStation,
    isActive: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    val borderColor = when {
        focused -> GoldLight
        isActive -> GoldPrimary
        else -> SurfaceLight
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isActive) Brush.linearGradient(listOf(SurfaceLight, SurfaceDark))
                else Brush.linearGradient(listOf(SurfaceDark, SurfaceDark))
            )
            .border(
                BorderStroke(if (focused || isActive) 2.dp else 1.dp, borderColor),
                RoundedCornerShape(12.dp)
            )
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // الجانب الأيسر: الأيقونة + التأثير الصوتي
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(44.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isActive) GoldPrimary else SurfaceLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (isActive) DarkBackground else GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.height(2.dp))
                SoundWaveVisualizer(
                    isPlaying = isPlaying,
                    modifier = Modifier.height(10.dp),
                    barCount = 3,
                    barWidth = 3.dp,
                    barSpacing = 2.dp,
                    maxHeight = 10.dp,
                    color = GoldPrimary
                )
            }

            Spacer(Modifier.width(12.dp))

            // الوسط: اسم المحطة والوصف
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${station.city} • ${station.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GoldLight
                )
            }

            // الجانب الأيمن: أيقونة إضافية
            Icon(
                imageVector = Icons.Default.SignalCellularAlt,
                contentDescription = null,
                tint = GoldPrimary.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
