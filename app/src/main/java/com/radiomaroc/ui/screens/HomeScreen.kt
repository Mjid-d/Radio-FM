package com.radiomaroc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
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

    val backgroundColor = Color.Black

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = station?.name ?: "radio FM",
                        color = Color.White,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(16.dp))

                // ===== الجزء المركزي: الأيقونة الكلاسيكية مع الذبذبات =====
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
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        ClassicRadioIcon(
                            isPlaying = isPlaying,
                            tint = backgroundColor
                        )
                    }
                }

                Text(
                    text = "TRACK ${String.format("%02d", index + 1)}",
                    color = Color.White.copy(alpha = 0.6f),
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
                    FocusableControlIcon(Icons.Default.FavoriteBorder) { }
                    FocusableControlIcon(Icons.Default.Equalizer) { }

                    // زر التشغيل/الإيقاف (كبير)
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
                                tint = backgroundColor,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    FocusableControlIcon(Icons.Default.SkipPrevious) { vm.previousStation() }
                    FocusableControlIcon(Icons.Default.SkipNext) { vm.nextStation() }
                }
            }
        }
    }
}

// ✅ أيقونة راديو كلاسيكية مع ذبذبات متحركة
@Composable
private fun ClassicRadioIcon(
    isPlaying: Boolean,
    tint: Color
) {
    val transition = rememberInfiniteTransition(label = "radio_wave")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // جسم الراديو
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val stroke = Stroke(width = 4.dp.toPx())

                // الهوائي
                val antennaPath = Path().apply {
                    moveTo(w * 0.60f, h * 0.30f)
                    lineTo(w * 0.88f, h * 0.10f)
                }
                drawPath(antennaPath, tint, style = stroke)

                // جسم الراديو (مستطيل مستدير)
                val bodyLeft = w * 0.18f
                val bodyTop = h * 0.32f
                val bodyRight = w * 0.82f
                val bodyBottom = h * 0.78f
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(bodyLeft, bodyTop),
                    size = Size(bodyRight - bodyLeft, bodyBottom - bodyTop),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()),
                    style = stroke
                )

                // الشاشة الداخلية (مستطيل صغير أعلى)
                drawRect(
                    color = tint,
                    topLeft = Offset(w * 0.25f, h * 0.40f),
                    size = Size(w * 0.40f, h * 0.12f)
                )

                // قرص التوليف (دائرة سفلية يسار)
                drawCircle(
                    color = tint,
                    radius = w * 0.08f,
                    center = Offset(w * 0.36f, h * 0.65f)
                )

                // خطوط السماعات (يمين) - تتحرك عند التشغيل
                val barHeights = listOf(0.05f, 0.08f, 0.06f)
                barHeights.forEachIndexed { i, _ ->
                    val barHeight by transition.animateFloat(
                        initialValue = 0.04f,
                        targetValue = if (isPlaying) (0.10f + i * 0.03f) else 0.04f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 400 + i * 150,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "bar$i"
                    )
                    val barX = w * 0.55f + i * (w * 0.10f)
                    val barY = h * 0.68f - h * barHeight
                    drawRect(
                        color = tint,
                        topLeft = Offset(barX, barY),
                        size = Size(w * 0.06f, h * barHeight * 2)
                    )
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
            .size(200.dp)
            .scale(scale1)
            .alpha(alpha1)
            .clip(CircleShape)
            .background(Color.White)
    )
    Box(
        modifier = Modifier
            .size(200.dp)
            .scale(scale2)
            .alpha(alpha2)
            .clip(CircleShape)
            .background(Color.White)
    )
}

// ✅ زر مع تأثير التركيز الأبيض الداخلي
@Composable
private fun FocusableControlIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(
                if (focused) Color.White.copy(alpha = 0.3f) // ✅ خلفية بيضاء شفافة عند التركيز
                else Color.Transparent
            )
            .onFocusChanged { focused = it.isFocused }
            .focusable(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (focused) Color.White else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
