package com.volumebox.noter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.volumebox.noter.states.NoteState
import com.volumebox.noter.states.TagState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NoteView(
    state: NoteState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 10
                ) {
                    state.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = tag.color,
                            modifier = Modifier.height(6.dp).width(32.dp)
                        ){

                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Note with Tags", showBackground = false)
@Composable
fun NoteViewWithTagsPreview() {
    MaterialTheme {
        NoteView(
            state = NoteState(
                name = "Shopping List",
                text = "Milk, Eggs, Bread",
                tags = listOf(
                    TagState(name = "Shopping", color = Color(0xFF4CAF50)),
                    TagState(name = "Urgent", color = Color(0xFFF44336))
                )
            ),
            onClick = {}
        )
    }
}

@Preview(name = "Note without Tags", showBackground = false)
@Composable
fun NoteViewWithoutTagsPreview() {
    MaterialTheme {
        NoteView(
            state = NoteState(
                name = "Meeting Notes",
                text = "Discuss project timeline and deliverables"
            ),
            onClick = {}
        )
    }
}

@Preview(name = "Long Note Title", showBackground = false)
@Composable
fun NoteViewLongTitlePreview() {
    MaterialTheme {
        NoteView(
            state = NoteState(
                name = "This is a very long note title that should wrap to multiple lines if needed",
                text = "Short content",
                tags = listOf(
                    TagState(name = "Important", color = Color(0xFF2196F3)),
                    TagState(name = "Work", color = Color(0xFF9C27B0)),
                )
            ),
            onClick = {}
        )
    }
}