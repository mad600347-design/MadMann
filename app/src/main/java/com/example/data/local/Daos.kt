package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureDao {
    @Query("SELECT * FROM downloaded_lectures ORDER BY isLiveNow DESC, id ASC")
    fun getAllLectures(): Flow<List<DownloadedLectureEntity>>

    @Query("SELECT * FROM downloaded_lectures WHERE isDownloaded = 1 ORDER BY downloadTimestamp DESC")
    fun getDownloadedLectures(): Flow<List<DownloadedLectureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lecture: DownloadedLectureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectures(lectures: List<DownloadedLectureEntity>)

    @Query("UPDATE downloaded_lectures SET isDownloaded = :isDownloaded, downloadTimestamp = :timestamp WHERE id = :id")
    suspend fun setDownloaded(id: String, isDownloaded: Boolean, timestamp: Long)

    @Query("DELETE FROM downloaded_lectures WHERE id = :id")
    suspend fun deleteLecture(id: String)
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM study_notes ORDER BY grade ASC, chapter ASC")
    fun getAllNotes(): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE isDownloadedOffline = 1 ORDER BY downloadTimestamp DESC")
    fun getOfflineNotes(): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE id = :id LIMIT 1")
    fun getNoteById(id: String): Flow<StudyNoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<StudyNoteEntity>)

    @Query("UPDATE study_notes SET isDownloadedOffline = :isDownloaded, downloadTimestamp = :timestamp WHERE id = :id")
    suspend fun setDownloaded(id: String, isDownloaded: Boolean, timestamp: Long)

    @Query("DELETE FROM study_notes WHERE id = :id")
    suspend fun deleteNote(id: String)
}

@Dao
interface DoubtDao {
    @Query("SELECT * FROM doubts ORDER BY createdAt DESC")
    fun getAllDoubts(): Flow<List<DoubtEntity>>

    @Query("SELECT * FROM doubts WHERE id = :id LIMIT 1")
    fun getDoubtById(id: Long): Flow<DoubtEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubt(doubt: DoubtEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubts(doubts: List<DoubtEntity>)

    @Update
    suspend fun updateDoubt(doubt: DoubtEntity)

    @Query("UPDATE doubts SET teacherAnswer = :teacherAnswer, teacherName = :teacherName, status = 'RESOLVED' WHERE id = :id")
    suspend fun resolveDoubtByTeacher(id: Long, teacherAnswer: String, teacherName: String)
}

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: UserProgressEntity)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM live_chat_messages WHERE lectureId = :lectureId ORDER BY timestamp ASC")
    fun getMessagesForLecture(lectureId: String): Flow<List<LiveChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: LiveChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<LiveChatMessageEntity>)
}

@Dao
interface GiftedProUserDao {
    @Query("SELECT * FROM gifted_pro_users ORDER BY giftedDate DESC")
    fun getAllGiftedUsers(): Flow<List<GiftedProUserEntity>>

    @Query("SELECT * FROM gifted_pro_users WHERE LOWER(googleUsername) = LOWER(:username) OR LOWER(googleUsername) = LOWER(:email) LIMIT 1")
    fun findGiftedUser(username: String, email: String): Flow<GiftedProUserEntity?>

    @Query("SELECT * FROM gifted_pro_users WHERE LOWER(googleUsername) = LOWER(:username) OR LOWER(googleUsername) = LOWER(:email) LIMIT 1")
    suspend fun getGiftedUserDirect(username: String, email: String): GiftedProUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGiftedUser(user: GiftedProUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGiftedUsers(users: List<GiftedProUserEntity>)

    @Query("DELETE FROM gifted_pro_users WHERE LOWER(googleUsername) = LOWER(:username)")
    suspend fun deleteGiftedUser(username: String)
}

