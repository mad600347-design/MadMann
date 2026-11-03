package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        DownloadedLectureEntity::class,
        StudyNoteEntity::class,
        DoubtEntity::class,
        UserProgressEntity::class,
        LiveChatMessageEntity::class,
        GiftedProUserEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lectureDao(): LectureDao
    abstract fun noteDao(): NoteDao
    abstract fun doubtDao(): DoubtDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun chatDao(): ChatDao
    abstract fun giftedProUserDao(): GiftedProUserDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vidyasetu_db"
                )
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
