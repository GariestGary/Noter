package com.volumebox.noter.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreenView(
    notes: List<NoteState>,
    tags: List<TagState>,
    editNoteScreen: String,
    noteScreen: String,
    onNavigate: () -> Unit = {},
    nav: NavController
) {
    val listState = rememberLazyListState()
    var expanded by remember { mutableStateOf(false) }
    var selectedTags by remember { mutableStateOf(emptySet<TagState>()) }

    val filteredNotes = if (selectedTags.isEmpty()) {
        notes
    } else {
        notes.filter { note ->
            // Only show notes that contain ALL selected tags
            selectedTags.all { selectedTag -> selectedTag.uid in note.tags.map { it.uid } }
        }
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header Section
                    Text(
                        text = "Your Notes",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Tags Filter Section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (selectedTags.isEmpty()) "Filter by tags" else "${selectedTags.size} tags selected",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    AnimatedContent(
                                        targetState = expanded,
                                        transitionSpec = {
                                            fadeIn(animationSpec = tween(150)) togetherWith
                                            fadeOut(animationSpec = tween(150))
                                        },
                                        label = "Arrow animation"
                                    ) { isExpanded ->
                                        Icon(
                                            imageVector = if (isExpanded) 
                                                Icons.Default.KeyboardArrowUp 
                                            else 
                                                Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                tags.forEach { tag ->
                                    DropdownMenuItem(
                                        text = { 
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Surface(
                                                    color = tag.color,
                                                    shape = CircleShape,
                                                    modifier = Modifier.size(16.dp)
                                                ) {}
                                                Text(tag.name)
                                            }
                                        },
                                        onClick = {
                                            selectedTags = if (selectedTags.contains(tag)) {
                                                selectedTags - tag
                                            } else {
                                                selectedTags + tag
                                            }
                                        },
                                        leadingIcon = {
                                            if(selectedTags.contains(tag)){
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Selected",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { selectedTags = emptySet() },
                            enabled = selectedTags.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear filters",
                                tint = if (selectedTags.isNotEmpty()) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notes List
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (filteredNotes.isEmpty()) {
                            item {
                                Text(
                                    text = if (selectedTags.isEmpty()) 
                                        "No notes yet. Tap + to create one!" 
                                    else 
                                        "No notes match selected tags",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp, horizontal = 12.dp)
                                )
                            }
                        } else {
                            items(filteredNotes) { note ->
                                NoteView(
                                    state = note,
                                    onClick = {
                                        onNavigate()
                                        nav.navigate(noteScreen + note.uid)
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
                onClick = { nav.navigate(editNoteScreen + "new") },
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
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
                }
            )
        }
    )
} 