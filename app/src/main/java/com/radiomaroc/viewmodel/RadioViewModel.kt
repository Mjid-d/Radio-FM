package com.radiomaroc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.radiomaroc.data.RadioRepository
import com.radiomaroc.data.RadioStation
import com.radiomaroc.player.RadioPlayerManager

class RadioViewModel(app: Application) : AndroidViewModel(app) {

    val playerManager = RadioPlayerManager(app)
    val stations: List<RadioStation> = RadioRepository.moroccanStations

    private var currentIndex = 0

    fun getCurrentIndex(): Int = currentIndex

    fun playIndex(index: Int) {
        if (index in stations.indices) {
            currentIndex = index
            playerManager.playStation(stations[index])
        }
    }

    fun nextStation() {
        currentIndex = (currentIndex + 1) % stations.size
        playerManager.playStation(stations[currentIndex])
    }

    fun previousStation() {
        currentIndex = if (currentIndex - 1 < 0) stations.size - 1 else currentIndex - 1
        playerManager.playStation(stations[currentIndex])
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
