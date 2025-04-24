package com.volumebox.noter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditViewModel(noteState: NoteState, tagState: TagState): ViewModel() {
    private val _sharedNote = MutableStateFlow(noteState)
    val sharedNote = _sharedNote.asStateFlow()
    private val _sharedTag = MutableStateFlow(tagState)
    val sharedTag = _sharedTag.asStateFlow()

    fun updateNote(state: NoteState){
        _sharedNote.value.copyFrom(state)
    }

    fun updateTag(state: TagState){
        _sharedTag.value.copyFrom(state)
    }
    
    // Factory to properly create EditViewModel with parameters
    class Factory(private val noteState: NoteState, private val tagState: TagState) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
                return EditViewModel(noteState, tagState) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
