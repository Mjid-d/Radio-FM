package com.radiomaroc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
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
import androidx.compose.ui.graphics.Color
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)) // حواف شبه مستقيمة
            .background(if (focused) SurfaceDark else Color.Transparent)
            .onFocusChanged { focused = it.isFocused }
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            // ✅ أيقونة التشغيل في مكان الوقت (على اليسار) - باللون الأبيض
            Icon(
                imageVector = if (isActive && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = GoldPrimary, // أبيض
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.width(16.dp))

            // الوسط: اسم المحطة والوصف
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary, // أبيض
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${station.city} • ${station.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary, // رمادي مزرق
                    maxLines = 1
                )
            }

            // الجانب الأيمن: موجة صوتية أو أيقونة إضافية
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isActive && isPlaying) {
                    SoundWaveVisualizer(
                        isPlaying = isPlaying,
                        modifier = Modifier.height(12.dp),
                        barCount = 3,
                        barWidth = 2.dp,
                        barSpacing = 2.dp,
                        maxHeight = 12.dp,
                        color = GoldPrimary // أبيض
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.SignalCellularAlt,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        // ✅ الخط الفاصل الأبيض الرقيق
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.15f)) // خط أبيض شفاف قليلاً ليظهر بشكل كلاسيكي
        )
    }
}
