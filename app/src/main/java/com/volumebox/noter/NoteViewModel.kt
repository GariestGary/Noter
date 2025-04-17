package com.volumebox.noter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {
    val notes = repository.getAllNotes()

    fun addNote(text: String) {
        viewModelScope.launch {
            repository.addNote(Note(uid = UUID.randomUUID().toString(), text = text))
        }
    }
}