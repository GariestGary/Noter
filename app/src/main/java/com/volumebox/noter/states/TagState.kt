package com.volumebox.noter.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.volumebox.noter.database.NoteTag

/**
 * State holder for tag creation/editing in the UI
 */
class TagState(uid: String = "", name: String = "", color: Color = Color.White) {
    // Use mutableStateOf to make these properties observable by Compose
    var uid by mutableStateOf(uid)
    var name by mutableStateOf(name)
    var color by mutableStateOf(color)  // Default to white with alpha
    
    // Helper method to reset the state
    fun reset() {
        uid = ""
        name = ""
        color = Color.White
    }

    fun copyFrom(state: TagState) {
        uid = state.uid
        name = state.name
        color = state.color
    }
} 