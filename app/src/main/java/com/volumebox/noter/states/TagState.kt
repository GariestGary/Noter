package com.volumebox.noter.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.volumebox.noter.database.NoteTag

/**
 * State holder for tag creation/editing in the UI
 */
class TagState {
    // Use mutableStateOf to make these properties observable by Compose
    var name by mutableStateOf("")
    var color by mutableStateOf(0xFFFFFFFF.toInt())  // Default to white with alpha
    
    // Helper method to reset the state
    fun reset() {
        name = ""
        color = 0xFFFFFFFF.toInt()
    }
    
    // Convert state to NoteTag entity
    fun toNoteTag(): NoteTag {
        return NoteTag(name = name, color = color)
    }
    
    // Helper to update from an existing tag
    fun updateFromTag(tag: NoteTag) {
        name = tag.name
        color = tag.color
    }
} 