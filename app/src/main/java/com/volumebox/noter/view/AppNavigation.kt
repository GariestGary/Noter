package com.volumebox.noter.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.volumebox.noter.database.Note
import com.volumebox.noter.database.NoteWithTags
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.viewmodel.NoteViewModel
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "notes") {
        composable("notes") {
            NotesScreen(
                addNoteClick = {
                    navController.navigate("add")
                }
            )
        }
        composable("add") {
            val viewModel = hiltViewModel<NoteViewModel>()
//            AddNoteScreen(
//                state = viewModel.noteState,
//                addClick = {
//                    navController.navigate("notes")
//                }
//            )
        }
    }
}