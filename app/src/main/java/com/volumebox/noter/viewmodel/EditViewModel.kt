package com.volumebox.noter.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.volumebox.noter.database.NoteRepository
import com.volumebox.noter.states.NoteState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditViewModel() : ViewModel() {
    private val _sharedNote = MutableStateFlow(NoteState())
    val sharedNote = _sharedNote.asStateFlow()
    private lateinit var repository: NoteRepository

    fun updateNote(state: NoteState){
        _sharedNote.value = state
    }
}
