package com.volumebox.noter.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.volumebox.noter.database.Note
import com.volumebox.noter.database.NoteTag

class NoteState {
    // Use mutableStateOf to make these properties observable by Compose
    var name by mutableStateOf("")
    var text by mutableStateOf("")
    var tags by mutableStateOf<List<NoteTag>>(emptyList())
    
    // Helper method to reset the state
    fun reset() {
        name = ""
        text = ""
        tags = emptyList()
    }
    
    // Convert state to Note entity
    fun toNote(): Note {
        return Note(name = name, text = text)
    }
}