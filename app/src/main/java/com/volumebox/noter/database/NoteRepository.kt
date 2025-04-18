package com.volumebox.noter.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteTagDao: NoteTagDao,
    private val noteWithTagsDao: NoteWithTagsDao
) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAll()

    suspend fun addNote(note: Note) {
        noteDao.insertAll(note)
    }

    fun getAllNoteTags(): Flow<List<NoteTag>> = noteTagDao.getAll()

    suspend fun addTag(tag: NoteTag) {
        noteTagDao.insertAll(tag)
    }

    fun getNotesWithTags(): Flow<List<NoteWithTags>> = noteWithTagsDao.getNotesWithTags()

    fun getNoteWithTags(noteId: String): Flow<NoteWithTags> = noteWithTagsDao.getNoteWithTags(noteId)

    suspend fun addTagToNote(noteId: String, tagId: String) {
        val crossRef = NoteTagCrossRef(noteId = noteId, tagId = tagId)
        noteWithTagsDao.insertNoteTagCrossRef(crossRef)
    }

    suspend fun createAndAddTagToNote(noteId: String, tagName: String, tagColor: Int = 0xFFFFFFF) {
        val tag = NoteTag(name = tagName, color = tagColor)
        noteTagDao.insertAll(tag)
        addTagToNote(noteId, tag.uid)
    }
}