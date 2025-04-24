package com.volumebox.noter.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Database(entities = [Note::class, NoteTag::class, NoteTagCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun noteTagDao(): NoteTagDao
    abstract fun noteWithTagsDao(): NoteWithTagsDao
}

@Entity
data class Note(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "text") val text: String = ""
)

@Entity
data class NoteTag(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: Int = 0xFFFFFFF
)

// Create a cross-reference entity for the many-to-many relationship
@Entity(primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef(
    val noteId: String,
    val tagId: String
)

// Define relationship object
data class NoteWithTags(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid",
        associateBy = Junction(NoteTagCrossRef::class, 
                              parentColumn = "noteId", 
                              entityColumn = "tagId")
    )
    val tags: List<NoteTag>
)

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg notes: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(vararg notes: Note)

    @Query("SELECT * FROM note")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT COUNT(*) FROM note WHERE uid = :id")
    suspend fun exists(id: String): Boolean
}

@Dao
interface NoteTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tags: NoteTag)

    @Delete
    suspend fun delete(tag: NoteTag)

    @Update
    suspend fun updateTags(vararg tags: NoteTag)

    @Query("SELECT * FROM notetag")
    fun getAll(): Flow<List<NoteTag>>

    @Query("SELECT EXISTS(SELECT 1 FROM NoteTag WHERE uid = :tagId LIMIT 1)")
    suspend fun tagExists(tagId: String): Boolean
}

@Dao
interface NoteWithTagsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteTagCrossRef(crossRef: NoteTagCrossRef)
    
    @Transaction
    @Query("SELECT * FROM Note")
    fun getNotesWithTags(): Flow<List<NoteWithTags>>

    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId")
    suspend fun clearAllTagsFromNote(noteId: String)

    @Transaction
    @Query("SELECT * FROM Note WHERE uid = :noteId")
    fun getNoteWithTags(noteId: String): Flow<NoteWithTags>
}