package com.radiomaroc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPlaying) {
                        PulseRings()
                    }

                    // ✅ الدائرة الذهبية الفاخرة (بدلاً من البيضاء)
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFF4D078), // ذهبي فاتح في المنتصف
                                        Color(0xFFD4AF37), // ذهبي كلاسيكي
                                        Color(0xFF8B6914)  // ذهبي داكن عند الحواف
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        ClassicRadioIcon(
                            isPlaying = isPlaying
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FocusableControlIcon(Icons.Default.FavoriteBorder) { }
                    FocusableControlIcon(Icons.Default.Equalizer) { }

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

// ✅ أيقونة راديو فاخرة (أسود + ذهبي + أحمر)
@Composable
private fun ClassicRadioIcon(
    isPlaying: Boolean
) {
    val transition = rememberInfiniteTransition(label = "radio_wave")

    // 5 أشرطة بأطوال مختلفة (شكل الموجات)
    val heights = listOf(0.4f, 0.75f, 1.0f, 0.75f, 0.4f)

    val animatedHeights = heights.mapIndexed { index, baseHeight ->
        val height by transition.animateFloat(
            initialValue = baseHeight * 0.2f,
            targetValue = if (isPlaying) baseHeight else baseHeight * 0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 380 + index * 80,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar$index"
        )
        height
    }

    // الألوان
    val radioBlack = Color(0xFF0A0A0A)
    val goldLight = Color(0xFFF4D078)
    val goldDark = Color(0xFF8B6914)
    val redBright = Color(0xFFFF3B30)
    val orangeBright = Color(0xFFFF9500)

    Canvas(modifier = Modifier.size(180.dp)) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 4.dp.toPx())

        // ===== 1. الهوائي =====
        val antennaStart = Offset(w * 0.62f, h * 0.30f)
        val antennaEnd = Offset(w * 0.90f, h * 0.12f)
        drawLine(
            color = radioBlack,
            start = antennaStart,
            end = antennaEnd,
            strokeWidth = 4.dp.toPx(),
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        // كرة صغيرة في نهاية الهوائي
        drawCircle(
            color = goldLight,
            radius = w * 0.025f,
            center = antennaEnd
        )

        // ===== 2. جسم الراديو (مستطيل أسود بإطار ذهبي) =====
        val bodyLeft = w * 0.15f
        val bodyTop = h * 0.32f
        val bodyRight = w * 0.85f
        val bodyBottom = h * 0.82f

        // خلفية الراديو (أسود)
        drawRoundRect(
            color = radioBlack,
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyRight - bodyLeft, bodyBottom - bodyTop),
            cornerRadius = CornerRadius(12.dp.toPx())
        )
        // الإطار الذهبي
        drawRoundRect(
            color = goldDark,
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyRight - bodyLeft, bodyBottom - bodyTop),
            cornerRadius = CornerRadius(12.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )

        // ===== 3. شاشة الترددات (يمين أعلى) =====
        val screenLeft = w * 0.50f
        val screenTop = h * 0.40f
        val screenRight = w * 0.78f
        val screenBottom = h * 0.52f

        drawRoundRect(
            color = Color(0xFF1A1A1A),
            topLeft = Offset(screenLeft, screenTop),
            size = Size(screenRight - screenLeft, screenBottom - screenTop),
            cornerRadius = CornerRadius(3.dp.toPx())
        )
        drawRoundRect(
            color = goldDark,
            topLeft = Offset(screenLeft, screenTop),
            size = Size(screenRight - screenLeft, screenBottom - screenTop),
            cornerRadius = CornerRadius(3.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // خطوط التردد الرمادية
        for (i in 1..4) {
            val x = screenLeft + (screenRight - screenLeft) * i / 5f
            drawLine(
                color = Color.Gray,
                start = Offset(x, screenTop + h * 0.02f),
                end = Offset(x, screenBottom - h * 0.02f),
                strokeWidth = 1.dp.toPx()
            )
        }

        // المؤشر الأحمر (مؤشر المحطة)
        val needleX = screenLeft + (screenRight - screenLeft) * 0.6f
        drawLine(
            color = redBright,
            start = Offset(needleX, screenTop + h * 0.01f),
            end = Offset(needleX, screenBottom - h * 0.01f),
            strokeWidth = 2.5.dp.toPx(),
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )

        // ===== 4. الذبذبات (أشرطة حمراء-برتقالية) - على يسار الراديو =====
        val barWidth = w * 0.040f
        val barSpacing = w * 0.022f
        val startX = w * 0.20f
        val baseY = h * 0.72f
        val maxBarHeight = h * 0.22f

        animatedHeights.forEachIndexed { index, heightRatio ->
            val barHeight = maxBarHeight * heightRatio
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(orangeBright, redBright)
                ),
                topLeft = Offset(
                    startX + index * (barWidth + barSpacing),
                    baseY - barHeight
                ),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }

        // ===== 5. قرص التوليف (دائرة ذهبية يمين أسفل) =====
        val knobCenter = Offset(w * 0.70f, h * 0.66f)
        val knobRadius = w * 0.075f

        // خلفية القرص (أسود)
        drawCircle(
            color = Color(0xFF1A1A1A),
            radius = knobRadius,
            center = knobCenter
        )
        // حلقة ذهبية
        drawCircle(
            color = goldDark,
            radius = knobRadius,
            center = knobCenter,
            style = Stroke(width = 2.dp.toPx())
        )
        // قرص ذهبي لامع
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(goldLight, goldDark),
                center = Offset(knobCenter.x - knobRadius * 0.3f, knobCenter.y - knobRadius * 0.3f),
                radius = knobRadius
            ),
            radius = knobRadius * 0.65f,
            center = knobCenter
        )
    }
}

// ✅ ذبذبات ناعمة
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
            .size(220.dp)
            .scale(scale1)
            .alpha(alpha1)
            .clip(CircleShape)
            .background(Color(0xFFF4D078))
    )
    Box(
        modifier = Modifier
            .size(220.dp)
            .scale(scale2)
            .alpha(alpha2)
            .clip(CircleShape)
            .background(Color(0xFFF4D078))
    )
}

// ✅ زر مع تأثير التركيز
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
                if (focused) Color.White.copy(alpha = 0.3f)
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
