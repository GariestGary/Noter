package com.volumebox.noter.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import com.volumebox.noter.viewmodel.EditViewModel
import com.volumebox.noter.viewmodel.NoteViewModel

@Composable
fun AppNavigation(viewModel: NoteViewModel = hiltViewModel(), navController: NavHostController) {
    val notesWithTags by viewModel.notesWithTags.collectAsState(initial = emptyList())
    val tags by viewModel.tags.collectAsState(initial = emptyList())

    val notes = remember(notesWithTags) {
        notesWithTags.map { (note, tagIds) ->
            NoteState(
                uid = note.uid,
                name = note.name,
                text = note.text,
                tags = tagIds.map { TagState(it.uid, it.name, Color(it.color)) }
            )
        }
    }

    NavHost(navController, startDestination = "notes") {
        composable("notes") {
            NotesListScreenView(
                notes = notes,
                editNoteScreen = "noteEdit/",
                noteScreen = "noteView/",
                nav = LocalNavController.current
            )
        }
        navigation(startDestination = "noteEdit/new", route = "editing") {
            composable("noteEdit/{noteUid}") { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("noteUid")
                val note = notesWithTags.find { note -> note.note.uid == uid }
                
                if(note != null) {
                    val initialNoteState = NoteState(
                        note.note.uid,
                        note.note.name,
                        note.note.text,
                        note.tags.map {
                            TagState(it.uid, it.name, Color(it.color))
                        }
                    )
                    
                    val editViewModel = backStackEntry.sharedViewModel(navController, initialNoteState, TagState())
                    val state by editViewModel.sharedNote.collectAsStateWithLifecycle()

                    editViewModel.updateNote(initialNoteState)

                    EditNoteScreenView(
                        state = state,
                        saveNote = {
                            viewModel.upsertNote(it)
                        },
                        allTags = tags.map { TagState(it.uid, it.name, Color(it.color)) },
                        editTagScreen = "tagEdit/",
                        nav = LocalNavController.current
                    )
                }
            }
            composable("noteEdit/new") { backStackEntry ->
                val editViewModel = backStackEntry.sharedViewModel(navController, NoteState(), TagState())
                val state by editViewModel.sharedNote.collectAsStateWithLifecycle()

                EditNoteScreenView(
                    state = state,
                    saveNote = {
                        viewModel.upsertNote(it)
                    },
                    allTags = tags.map { TagState(it.uid, it.name, Color(it.color)) },
                    editTagScreen = "tagEdit/",
                    nav = LocalNavController.current
                )
            }
            composable("tagEdit/{tagUid}"){ backStackEntry ->
                val uid = backStackEntry.arguments?.getString("tagUid")
                val tag = tags.find { tag -> tag.uid == uid }

                if(tag != null) {
                    val initialTag = TagState(tag.uid, tag.name, Color(tag.color))
                    val editViewModel = backStackEntry.sharedViewModel(navController, NoteState(), initialTag)
                    val state by editViewModel.sharedTag.collectAsStateWithLifecycle()

                    TagEditScreenView(
                        tag = state,
                        onSave = {viewModel.upsertTag(it)},
                        nav = LocalNavController.current
                    )
                }
            }
            composable("tagEdit/new"){ backStackEntry ->
                val editViewModel = backStackEntry.sharedViewModel(navController, NoteState(), TagState())
                val state by editViewModel.sharedTag.collectAsStateWithLifecycle()
                TagEditScreenView(
                    tag = state,
                    onSave = {
                        viewModel.upsertTag(it)
                    },
                    nav = LocalNavController.current
                )
            }
        }
        composable("noteView/{noteUid}"){ backStackEntry ->
            val uid = backStackEntry.arguments?.getString("noteUid")
            val note = notesWithTags.find { note -> note.note.uid == uid }

            if(note != null) {
                NoteScreenView(
                    NoteState(
                        note.note.uid,
                        note.note.name,
                        note.note.text,
                        note.tags.map {
                            TagState(
                                it.uid,
                                it.name,
                                Color(it.color)
                            )
                        }
                    ),
                    editNoteScreen = "noteEdit/",
                    nav = LocalNavController.current
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController) : T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

@Composable
inline fun NavBackStackEntry.sharedViewModel(navController: NavHostController, noteState: NoteState, tagState: TagState): EditViewModel {
    val navGraphRoute = destination.parent?.route ?: return viewModel { EditViewModel.Factory(noteState, tagState).create(EditViewModel::class.java) }
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(
        viewModelStoreOwner = parentEntry,
        factory = EditViewModel.Factory(noteState, tagState)
    )
}