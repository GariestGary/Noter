package com.volumebox.noter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.volumebox.noter.ui.NoterTheme
import com.volumebox.noter.view.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoterTheme {  // Make sure this matches your theme name
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation() // Your root composable
                }
            }
        }
    }
}