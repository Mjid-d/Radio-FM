package com.radiomaroc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiomaroc.ui.theme.*
import com.radiomaroc.viewmodel.RadioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: RadioViewModel = viewModel()) {

    val currentStation by vm.playerManager.currentStation.collectAsState()
    val isPlaying by vm.playerManager.isPlaying.collectAsState()

    LaunchedEffect(Unit) {
        if (currentStation == null && vm.stations.isNotEmpty()) {
            vm.playIndex(0)
        }
    }

    val station = currentStation ?: vm.stations.firstOrNull()
    val index = vm.stations.indexOf(station).coerceAtLeast(0)

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A2A28),
            Color(0xFF051A18),
            Color(0xFF030706)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = station?.name ?: "radio FM",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, "القائمة", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, "بحث", tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Settings, "إعدادات", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PulseRings(isPlaying = isPlaying)

                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color.White, Color(0xFFE8F0EE))
                                )
                            )
                            .border(3.dp, GoldPrimary.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = null,
                            tint = DarkBackground,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Text(
                    text = "TRACK ${String.format("%02d", index + 1)}",
                    color = GoldPrimary.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = station?.name ?: "",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = station?.category ?: "",
                    color = TextSecondary,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(20.dp))

                // ===== أزرار التحكم (بعد التعديل) =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. المفضلة
                    ControlIcon(Icons.Default.FavoriteBorder) { }

                    // 2. المعادل
                    ControlIcon(Icons.Default.Equalizer) { }

                    // 3. تشغيل/إيقاف (كبير)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { vm.playerManager.togglePlayPause() },
                            modifier = Modifier.size(72.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = DarkBackground,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    // 4. السابق (Prev) - ✅ جديد
                    ControlIcon(Icons.Default.SkipPrevious) { vm.previousStation() }

                    // 5. التالي (Next) - ✅ موجود
                    ControlIcon(Icons.Default.SkipNext) { vm.nextStation() }
                }
            }
        }
    }
}

@Composable
private fun PulseRings(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "pulse")

    repeat(3) { i ->
        val scale by transition.animateFloat(
            initialValue = 1f,
            targetValue = if (isPlaying) 2.2f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000 + i * 600,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "scale$i"
        )
        val alpha by transition.animateFloat(
            initialValue = 0.6f,
            targetValue = if (isPlaying) 0f else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000 + i * 600,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "alpha$i"
        )

        Box(
            modifier = Modifier
                .size(180.dp)
                .scale(scale)
                .alpha(alpha)
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.4f))
        )
    }
}

@Composable
private fun ControlIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
