package com.volumebox.noter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.volumebox.noter.database.NoteTag
import com.volumebox.noter.states.NoteState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    state: NoteState,
    addClick: () -> Unit,
    addNote: (NoteState) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var tags : List<NoteTag> by remember { mutableStateOf(listOf()) }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        contentWindowInsets = WindowInsets(0.dp),
        content = {
                paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth())
                TextField(
                    value = text,
                    minLines = 15,
                    onValueChange = { text = it },
                    modifier = Modifier.wrapContentSize())
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addClick.invoke()
                addNote.invoke()
            }, shape = CircleShape, modifier = Modifier.size(64.dp))
            {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Save",
                    modifier = Modifier.scale(2f)
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)){
                        Text("Noter")
                    }
                }
            )
        }
    )
} 