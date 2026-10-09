package com.radiomaroc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.radiomaroc.data.RadioStation
import com.radiomaroc.ui.theme.*

@Composable
fun BottomPlayer(
    station: RadioStation?,
    isPlaying: Boolean,
    isBuffering: Boolean,
    onToggle: () -> Unit,
    onStop: () -> Unit
) {
    if (station == null) return

    Surface(
        color = DarkBackground, // ✅ تم تغيير اللون ليكون نفس لون الخلفية
        shadowElevation = 0.dp, // ✅ تم إزالة الظل ليكون اللون موحداً تماماً
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = when {
                        isBuffering -> "جاري التحميل..."
                        isPlaying -> "يُبَث الآن • ${station.city}"
                        else -> "متوقف • ${station.city}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isPlaying) SuccessGreen else TextSecondary
                )
            }

            Spacer(Modifier.width(8.dp))

            PlayerButton(
                icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                onClick = onToggle
            )
            Spacer(Modifier.width(8.dp))
            PlayerButton(
                icon = Icons.Default.Stop,
                onClick = onStop
            )
        }
    }
}

@Composable
private fun PlayerButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (focused) GoldPrimary else SurfaceLight)
            .border(
                BorderStroke(1.dp, if (focused) GoldLight else SurfaceLight),
                CircleShape
            )
            .onFocusChanged { focused = it.isFocused }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (focused) DarkBackground else TextPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}
