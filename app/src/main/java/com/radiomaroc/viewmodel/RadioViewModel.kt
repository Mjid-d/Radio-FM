package com.radiomaroc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.radiomaroc.data.RadioRepository
import com.radiomaroc.data.RadioStation
import com.radiomaroc.player.RadioPlayerManager

class RadioViewModel(app: Application) : AndroidViewModel(app) {

    val playerManager = RadioPlayerManager(app)
    val stations: List<RadioStation> = RadioRepository.moroccanStations

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
