package com.volumebox.noter.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volumebox.noter.database.NoteRepository
import com.volumebox.noter.database.NoteTag
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    // Note state
    private val _noteState = mutableStateOf(NoteState())
    val noteState: NoteState get() = _noteState.value
    
    // Tag state
    private val _tagState = MutableStateFlow(TagState())
    val tagState: StateFlow<TagState> = _tagState.asStateFlow()
    
    // Expose database data as Flows
    val notes = repository.getAllNotes()
    val notesWithTags = repository.getNotesWithTags()
    val tags = repository.getAllNoteTags()
    
    // Methods to update note state
    fun updateNoteName(name: String) {
        _noteState.value.name = name
    }
    
    fun updateNoteText(text: String) {
        _noteState.value.text = text
    }
    
    // Methods to update tag state
    fun updateTagName(name: String) {
        _tagState.value.name = name
    }
    
    fun updateTagColor(color: Color) {
        _tagState.value.color = color
    }

    fun upsertNote(note: NoteState) {
        viewModelScope.launch {
            repository.upsert(note)
        }
    }

    fun upsertTag(state: TagState) {
        viewModelScope.launch {
            repository.addTag(NoteTag(name = state.name, color = state.color.toArgb()))
        }
    }
    
    fun addTagToNote(noteId: String, tagId: String) {
        viewModelScope.launch {
            repository.addTagToNote(noteId, tagId)
        }
    }
    
    fun createAndAddTagToNote(noteId: String, tagName: String, tagColor: Int = 0xFFFFFFF) {
        viewModelScope.launch {
            repository.createAndAddTagToNote(noteId, tagName, tagColor)
        }
    }
}