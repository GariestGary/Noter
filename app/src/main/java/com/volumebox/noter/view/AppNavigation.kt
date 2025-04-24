package com.volumebox.noter.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import com.volumebox.noter.viewmodel.NoteViewModel
import kotlinx.coroutines.flow.asFlow

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
                addNoteClick = {
                    navController.navigate("add")
                }
            )
        }
        composable("add") {
            val viewModel = hiltViewModel<NoteViewModel>()
            AddNoteScreenView(
                state = viewModel.state,
                addNote = {
                    navController.navigate("notes")
                    viewModel.addNote(it)
                },
                allTags = tags.map { TagState(it.uid, it.name, Color(it.color)) }
            )
        }
        composable("note_view/{noteUid}"){ backStackEntry ->
            val uid = backStackEntry.arguments?.getString("noteUid")
            val note = notesWithTags.find { note -> note.note.uid == uid }

            if(note != null) {
                NoteScreenView(NoteState(note.note.uid, note.note.name, note.note.text, note.tags.map { TagState(it.uid, it.name, Color(it.color)) }))
            }
        }
        composable("tag_edit/{tagUid}"){ backStackEntry ->
            val uid = backStackEntry.arguments?.getString("tagUid")
            val tag = tags.find { tag -> tag.uid == uid }

            if(tag != null) {
                TagEditScreenView(TagState(tag.uid, tag.name, Color(tag.color)), onSave = {})
            }
        }
        composable("tag_edit/new"){ backStackEntry ->
            TagEditScreenView(TagState(), onSave = {
                viewModel.addTag(it)
            })
        }
    }
}