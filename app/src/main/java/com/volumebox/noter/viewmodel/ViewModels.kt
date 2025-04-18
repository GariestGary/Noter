package com.volumebox.noter.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volumebox.noter.database.Note
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
    private val _noteState = MutableStateFlow(NoteState())
    val noteState: StateFlow<NoteState> = _noteState.asStateFlow()
    
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
    
    fun addTagToState(tag: NoteTag) {
        val currentTags = _noteState.value.tags.toMutableList()
        currentTags.add(tag)
        _noteState.value.tags = currentTags
    }
    
    fun removeTagFromState(tagId: String) {
        val currentTags = _noteState.value.tags.toMutableList()
        currentTags.removeIf { it.uid == tagId }
        _noteState.value.tags = currentTags
    }
    
    // Methods to update tag state
    fun updateTagName(name: String) {
        _tagState.value.name = name
    }
    
    fun updateTagColor(color: Int) {
        _tagState.value.color = color
    }
    
    // Save the current note state to the database
    fun saveNote() {
        viewModelScope.launch {
            val state = _noteState.value
            val note = state.toNote()
            
            // Add the note to the database
            repository.addNote(note)
            
            // Add all tags to the note
            state.tags.forEach { tag ->
                repository.addTagToNote(note.uid, tag.uid)
            }
            
            // Reset the state after saving
            _noteState.value.reset()
        }
    }
    
    // Save the current tag state to the database
    fun saveTag() {
        viewModelScope.launch {
            val tag = _tagState.value.toNoteTag()
            repository.addTag(tag)
            
            // Reset the tag state after saving
            _tagState.value.reset()
        }
    }
    
    // Save tag and add it to the current note
    fun saveTagAndAddToCurrentNote() {
        viewModelScope.launch {
            val tag = _tagState.value.toNoteTag()
            repository.addTag(tag)
            
            // Add the tag to the current note state
            addTagToState(tag)
            
            // Reset the tag state
            _tagState.value.reset()
        }
    }
    
    // Original methods preserved
    fun addNote(name: String, text: String) {
        viewModelScope.launch {
            repository.addNote(Note(name = name, text = text))
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }
    
    fun addTag(name: String, color: Int = 0xFFFFFFF) {
        viewModelScope.launch {
            repository.addTag(NoteTag(name = name, color = color))
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