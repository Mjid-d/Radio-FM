package com.radiomaroc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiomaroc.ui.components.BigVisualizer
import com.radiomaroc.ui.theme.*
import com.radiomaroc.viewmodel.RadioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: RadioViewModel = viewModel()) {

    val currentStation by vm.playerManager.currentStation.collectAsState()
    val isPlaying by vm.playerManager.isPlaying.collectAsState()

    // عند فتح التطبيق، شغّل أول إذاعة تلقائياً إن لم تكن هناك إذاعة
    LaunchedEffect(Unit) {
        if (currentStation == null && vm.stations.isNotEmpty()) {
            vm.playIndex(0)
        }
    }

    val station = currentStation ?: vm.stations.firstOrNull()
    val index = vm.stations.indexOf(station).coerceAtLeast(0)

    Scaffold(
        containerColor = DarkBackground,
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
                    IconButton(onClick = { /* قائمة مستقبلاً */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "القائمة",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* بحث */ }) {
                        Icon(Icons.Default.Search, null, tint = Color.White)
                    }
                    IconButton(onClick = { /* إعدادات */ }) {
                        Icon(Icons.Default.Settings, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            // ===== الجزء العلوي: الغلاف + المؤثر الصوتي =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // الغلاف (مربع أبيض مع أيقونة الراديو + الرقم + اسم الإذاعة)
                Box(
                    modifier = Modifier
                        .weight(0.38f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = null,
                            tint = DarkBackground,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = String.format("%02d", index + 1),
                            color = DarkBackground,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Track ${String.format("%02d", index + 1)}",
                            color = DarkBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.width(20.dp))

                // المؤثر الصوتي الكبير
                Box(
                    modifier = Modifier
                        .weight(0.62f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    BigVisualizer(
                        isPlaying = isPlaying,
                        modifier = Modifier.height(180.dp),
                        barCount = 22,
                        barWidth = 8.dp,
                        barSpacing = 3.dp,
                        maxHeight = 180.dp,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ===== أزرار التحكم السفلية =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // زر المفضلة
                ControlIcon(Icons.Default.FavoriteBorder) { /* مفضلة مستقبلاً */ }

                // زر المشغل/المعادل
                ControlIcon(Icons.Default.Equalizer) { /* معادل مستقبلاً */ }

                // زر التشغيل/الإيقاف الرئيسي (كبير)
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

                // التالي
                ControlIcon(Icons.Default.SkipNext) { vm.nextStation() }

                // إيقاف
                ControlIcon(Icons.Default.Stop) { vm.playerManager.stop() }
            }
        }
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
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
