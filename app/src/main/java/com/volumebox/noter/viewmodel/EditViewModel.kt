package com.volumebox.noter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditViewModel : ViewModel() {
    private val _sharedNote = MutableStateFlow(NoteState())
    val sharedNote = _sharedNote.asStateFlow()

    fun updateNote(state: NoteState){
        _sharedNote.value = state
    }
}
