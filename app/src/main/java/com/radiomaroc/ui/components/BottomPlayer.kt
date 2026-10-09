package com.radiomaroc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
        color = DarkBackground,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // معلومات المحطة (اسم فقط)
            Column(Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary, // أبيض
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            // ✅ التأثير الصوتي مع زر التشغيل/الإيقاف الموحد
            Row(verticalAlignment = Alignment.CenterVertically) {
                SoundWaveVisualizer(
                    isPlaying = isPlaying,
                    modifier = Modifier.height(14.dp),
                    barCount = 3,
                    barWidth = 2.dp,
                    barSpacing = 2.dp,
                    maxHeight = 14.dp,
                    color = GoldPrimary // أبيض
                )
                Spacer(Modifier.width(8.dp))
                // ✅ زر واحد فقط للتشغيل والإيقاف
                PlayerButton(
                    icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    onClick = onToggle
                )
            }

            Spacer(Modifier.width(8.dp))

            // ✅ أيقونة البحث
            PlayerButton(
                icon = Icons.Default.Search,
                onClick = { /* بحث مستقبلاً */ }
            )
            Spacer(Modifier.width(6.dp))

            // ✅ أيقونة الإعدادات
            PlayerButton(
                icon = Icons.Default.Settings,
                onClick = { /* إعدادات مستقبلاً */ }
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
            .size(42.dp)
            .clip(CircleShape)
            .background(if (focused) GoldPrimary else SurfaceDark)
            .border(
                BorderStroke(1.dp, if (focused) GoldPrimary else SurfaceLight),
                CircleShape
            )
            .onFocusChanged { focused = it.isFocused }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (focused) DarkBackground else GoldPrimary, // أبيض
            modifier = Modifier.size(22.dp)
        )
    }
}
