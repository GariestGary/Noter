package com.volumebox.noter.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditNoteScreenView(
    state: NoteState,
    allTags: List<TagState>,
    saveNote: (NoteState) -> Unit,
    onNavigate: (NoteState) -> Unit,
    editTagScreen: String,
    nav: NavController
) {
    var note by remember { mutableStateOf(state) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    fun removeTag(tagId: String) {
        note.tags = note.tags.filterNot { it.uid == tagId }
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        contentWindowInsets = WindowInsets(0.dp),
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title Section
                    Text(
                        text = "New Note",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Note Name Field
                    OutlinedTextField(
                        value = note.name,
                        onValueChange = { note.name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Title") },
                        placeholder = { Text("Enter note title") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )

                    // Note Content Field
                    OutlinedTextField(
                        value = note.text,
                        onValueChange = { note.text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        label = { Text("Content") },
                        placeholder = { Text("Write your note here...") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        )
                    )

                    // Tags Section
                    Text(
                        text = "Tags",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    // Existing Tags FlowRow (wrappable)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 10 // This is flexible and will adjust based on screen width
                    ) {
                        note.tags.forEach { tag ->
                            NoteTagView(state = tag, showRemoveButton = true, onRemoveClick = {removeTag(it.uid)})
                        }
                    }

                    // Simple Tag Dropdown with Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add a tag:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Box {
                            OutlinedButton(
                                onClick = { isDropdownExpanded = true },
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text("Select tag")
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown Arrow",
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                            
                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false },
                                modifier = Modifier.width(200.dp)
                            ) {
                                allTags.forEach { tag ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape),
                                                    color = tag.color
                                                ) {}
                                                Text(tag.name)
                                            }
                                        },
                                        onClick = {
                                            if (!note.tags.any{x -> x.uid == tag.uid}) {
                                                note.tags += tag
                                            }
                                            isDropdownExpanded = false
                                        }
                                    )
                                }
                                
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add Tag",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text("Create new tag...")
                                        }
                                    },
                                    onClick = {
                                        isDropdownExpanded = false
                                        onNavigate(note)
                                        nav.navigate(editTagScreen + "new")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    saveNote.invoke(NoteState(uid = state.uid, name = note.name, text = note.text, tags = note.tags))
                    nav.popBackStack()
                },
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Save Note",
                    modifier = Modifier.scale(1.5f),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Noter",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    )
}

@Preview(name = "Add Note Screen", showBackground = true)
@Composable
fun AddNoteScreenPreview() {
    MaterialTheme {
        EditNoteScreenView(
            state = NoteState(
                name = "",
                text = "",
                tags = listOf(
                    TagState(name = "Work", color = Color(0xFF4CAF50)),
                    TagState(name = "Personal", color = Color(0xFF2196F3))
                )
            ),
            allTags = listOf(
                TagState(name = "Work", color = Color(0xFF4CAF50)),
                TagState(name = "Personal", color = Color(0xFF2196F3)),
                TagState(name = "Ideas", color = Color(0xFF9C27B0)),
                TagState(name = "Important", color = Color(0xFFF44336))
            ),
            saveNote = {},
            editTagScreen = "",
            nav = NavController(LocalContext.current),
            onNavigate = {}
        )
    }
}