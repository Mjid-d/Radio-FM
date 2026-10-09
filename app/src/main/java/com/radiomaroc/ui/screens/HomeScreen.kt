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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // الغلاف
                Box(
                    modifier = Modifier
                        .weight(0.4f)
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

                Spacer(Modifier.width(24.dp))

                // معلومات الإذاعة (بدلاً من التأثير الصوتي)
                Column(
                    modifier = Modifier.weight(0.6f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Radio,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = station?.name ?: "",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = station?.category ?: "",
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // أزرار التحكم
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlIcon(Icons.Default.FavoriteBorder) { }
                ControlIcon(Icons.Default.Equalizer) { }

                // زر التشغيل/الإيقاف
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

                ControlIcon(Icons.Default.SkipNext) { vm.nextStation() }
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
