package com.radiomaroc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        color = DarkBackground,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // معلومات المحطة
            Column(Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary, // أبيض
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            // التأثير الصوتي
            SoundWaveVisualizer(
                isPlaying = isPlaying,
                modifier = Modifier.height(14.dp),
                barCount = 3,
                barWidth = 2.dp,
                barSpacing = 2.dp,
                maxHeight = 14.dp,
                color = Color.White
            )

            Spacer(Modifier.width(12.dp))

            // ✅ زر التشغيل/الإيقاف الموحد بدون خلفية دائرية
            PlayerButton(
                icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                onClick = onToggle
            )
        }
    }
}

@Composable
private fun PlayerButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(44.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White, // ✅ أبيض مباشرة
            modifier = Modifier.size(28.dp)
        )
    }
}
