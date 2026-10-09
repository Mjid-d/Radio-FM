package com.radiomaroc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.radiomaroc.ui.screens.HomeScreen
import com.radiomaroc.ui.screens.SplashScreen
import com.radiomaroc.ui.theme.DarkBackground
import com.radiomaroc.ui.theme.RadioMarocTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadioMarocTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    var showSplash by remember { mutableStateOf(true) }
                    LaunchedEffect(Unit) {
                        delay(5000) // ✅ تم تغيير المدة من 1500 إلى 5000 (5 ثوانٍ)
                        showSplash = false
                    }
                    if (showSplash) {
                        SplashScreen()
                    } else {
                        HomeScreen()
                    }
                }
            }
        }
    }
}
