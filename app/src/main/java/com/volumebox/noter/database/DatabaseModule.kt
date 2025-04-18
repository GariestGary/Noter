package com.volumebox.noter.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "notes.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()
    @Provides
    fun provideNoteTagDao(database: AppDatabase): NoteTagDao = database.noteTagDao()
    @Provides
    fun provideNoteWithTagsDao(database: AppDatabase): NoteWithTagsDao = database.noteWithTagsDao()
}