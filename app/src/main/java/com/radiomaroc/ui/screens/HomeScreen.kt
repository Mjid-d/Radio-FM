package com.radiomaroc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radiomaroc.ui.components.BottomPlayer
import com.radiomaroc.ui.components.StationCard
import com.radiomaroc.ui.theme.*
import com.radiomaroc.viewmodel.RadioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: RadioViewModel = viewModel()) {
    val currentStation by vm.playerManager.currentStation.collectAsState()
    val isPlaying by vm.playerManager.isPlaying.collectAsState()
    val isBuffering by vm.playerManager.isBuffering.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "radio FM",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                // تم نقل الأيقونات إلى الأسفل
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = GoldPrimary
                )
            )
        },
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
            LazyColumn(
                contentPadding = PaddingValues(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(vm.stations, key = { it.id }) { station ->
                    StationCard(
                        station = station,
                        isActive = currentStation?.id == station.id,
                        isPlaying = isPlaying && currentStation?.id == station.id,
                        onClick = { vm.playerManager.playStation(station) }
                    )
                }
            }
        }
    }
}
