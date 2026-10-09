package com.radiomaroc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiomaroc.ui.components.BottomPlayer
import com.radiomaroc.ui.components.StationCard
import com.radiomaroc.ui.theme.*
import com.radiomaroc.viewmodel.RadioViewModel

@Composable
fun HomeScreen(vm: RadioViewModel = viewModel()) {
    val currentStation by vm.playerManager.currentStation.collectAsState()
    val isPlaying by vm.playerManager.isPlaying.collectAsState()
    val isBuffering by vm.playerManager.isBuffering.collectAsState()

    Scaffold(
        containerColor = DarkBackground, // ✅ لون الخلفية الموحد
        bottomBar = {
            BottomPlayer(
                station = currentStation,
                isPlaying = isPlaying,
                isBuffering = isBuffering,
                onToggle = { vm.playerManager.togglePlayPause() },
                onStop = { vm.playerManager.stop() }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "radio FM",
                    color = GoldPrimary, // لون النص فقط (أزرق)
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(20.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vm.stations, key = { it.id }) { station ->
                        StationCard(
                            station = station,
                            isActive = currentStation?.id == station.id,
                            onClick = { vm.playerManager.playStation(station) }
                        )
                    }
                }
            }
        }
    }
}
