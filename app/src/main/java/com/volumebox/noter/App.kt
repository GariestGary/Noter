package com.volumebox.noter

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun App() {
    // Your app content goes here
    Theme {
        Surface {
            Box(contentAlignment = Alignment.Center)
            {
                Text(
                    text = "Hello",
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                )
            }
            // Your app content

        }
    }
}