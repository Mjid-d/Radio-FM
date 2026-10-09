package com.radiomaroc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.radiomaroc.ui.theme.DarkBackground
import com.radiomaroc.ui.theme.RadioMarocTheme
import com.radiomaroc.ui.theme.TextPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadioMarocTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    Text(text = "التطبيق يعمل الآن!", color = TextPrimary)
                }
            }
        }
    }
}
