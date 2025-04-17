package com.volumebox.noter

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

@Entity
data class Note(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "text") val text: String,
)

@Dao
interface NoteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun updateNotes(vararg notes: Note)

    @Query("SELECT * FROM note")
    fun getAll(): Flow<List<Note>>
}