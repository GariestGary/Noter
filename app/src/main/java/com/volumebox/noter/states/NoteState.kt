package com.volumebox.noter.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.volumebox.noter.database.Note
import com.volumebox.noter.database.NoteTag

class NoteState(uid: String = "", name: String = "", text: String = "", tags: List<TagState> = emptyList()) {
    // Use mutableStateOf to make these properties observable by Compose
    var uid by mutableStateOf(uid)
    var name by mutableStateOf(name)
    var text by mutableStateOf(text)
    var tags by mutableStateOf(tags)
    
    // Helper method to reset the state
    fun reset() {
        uid = ""
        name = ""
        text = ""
        tags = emptyList()
    }

    fun copyFrom(state: NoteState) {
        uid = state.uid
        name = state.name
        text = state.text
        tags = state.tags
    }
}