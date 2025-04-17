package com.volumebox.noter

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAll()

    suspend fun addNote(note: Note) {
        noteDao.insertAll(note)
    }
}