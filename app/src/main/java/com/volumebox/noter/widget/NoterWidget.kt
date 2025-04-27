package com.volumebox.noter.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.hilt.navigation.compose.hiltViewModel
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState
import com.volumebox.noter.viewmodel.NoteViewModel

class NoterWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            MyContent()
        }
    }

    @Composable
    private fun MyContent() {
        val viewModel: NoteViewModel = hiltViewModel()
        val notesWithTags by viewModel.notesWithTags.collectAsState(initial = emptyList())
        val tags by viewModel.tags.collectAsState(initial = emptyList())

        val notes = remember(notesWithTags) {
            notesWithTags.map { (note, tags) ->
                NoteState(
                    uid = note.uid,
                    name = note.name,
                    text = note.text,
                    tags = tags.map {
                        TagState(
                            it.uid,
                            it.name,
                            Color(it.color)
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { items(notes) { note ->
                Text(note.name)
            }
        }
    }
}
