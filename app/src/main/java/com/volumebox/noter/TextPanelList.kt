package com.volumebox.noter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotesScreen(viewModel: NoteViewModel = hiltViewModel()) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    LazyColumn {
        items(notes) { note ->
            Text(note.text)
        }
    }
}

@Composable
fun AddNoteScreen(viewModel: NoteViewModel = hiltViewModel()) {
    var text by remember { mutableStateOf("") }

    Column {
        TextField(value = text, onValueChange = { text = it })
        Button(onClick = { viewModel.addNote(text) }) {
            Text("Add Note")
        }
    }
}