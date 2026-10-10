package com.radiomaroc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
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

    // نفس التدرج للأيقونة
    val iconGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A2A28),
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

                // ===== الجزء المركزي: الأيقونة مع الحلقات النابضة =====
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPlaying) {
                        PulseRings()
                    }

                    // الدائرة البيضاء الرئيسية
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
                        // ✅ أيقونة الراديو مع التدرج اللوني (الطريقة الصحيحة)
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(110.dp)
                                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                                .drawWithContent {
                                    drawContent()
                                    drawRect(brush = iconGradient, blendMode = BlendMode.SrcIn)
                                }
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

                // ===== أزرار التحكم =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ControlIcon(Icons.Default.FavoriteBorder) { }
                    ControlIcon(Icons.Default.Equalizer) { }

                    var playFocused by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(if (playFocused) GoldPrimary else Color.White)
                            .onFocusChanged { playFocused = it.isFocused }
                            .focusable(),
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

                    ControlIcon(Icons.Default.SkipPrevious) { vm.previousStation() }
                    ControlIcon(Icons.Default.SkipNext) { vm.nextStation() }
                }
            }
        }
    }
}

// ✅ ذبذبات ناعمة ومتواصلة
@Composable
private fun PulseRings() {
    val transition = rememberInfiniteTransition(label = "pulse")

    val scale1 by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale1"
    )
    val alpha1 by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )

    val scale2 by transition.animateFloat(
        initialValue = 1.15f,
        targetValue = 1.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale2"
    )
    val alpha2 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )

    Box(
        modifier = Modifier
            .size(180.dp)
            .scale(scale1)
            .alpha(alpha1)
            .clip(CircleShape)
            .background(GoldPrimary)
    )
    Box(
        modifier = Modifier
            .size(180.dp)
            .scale(scale2)
            .alpha(alpha2)
            .clip(CircleShape)
            .background(GoldPrimary)
    )
}

@Composable
private fun ControlIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(
                if (focused) GoldPrimary.copy(alpha = 0.3f)
                else Color.White.copy(alpha = 0.1f)
            )
            .border(
                1.dp,
                if (focused) GoldPrimary else Color.White.copy(alpha = 0.2f),
                CircleShape
            )
            .onFocusChanged { focused = it.isFocused }
            .focusable()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (focused) GoldPrimary else Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
