package com.radiomaroc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    
    val listState = rememberLazyListState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF2C3E50), Color(0xFF0F0E17))
    )

    Scaffold(
        containerColor = Color.Transparent,
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
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = GoldPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
                .background(gradientBrush)
                .padding(padding)
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
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
