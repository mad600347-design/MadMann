package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloaded_lectures")
data class DownloadedLectureEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val teacher: String,
    val durationMinutes: Int,
    val aiSummary: String,
    val keyFormulas: String,
    val fileSizeMb: Double,
    val isDownloaded: Boolean = false,
    val downloadTimestamp: Long = 0L,
    val isLiveNow: Boolean = false
)

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val chapter: String,
    val pageCount: Int,
    val fileSizeKb: Int,
    val contentSummary: String,
    val detailedNotesMarkdown: String,
    val isDownloadedOffline: Boolean = false,
    val downloadTimestamp: Long = 0L
)

@Entity(tableName = "doubts")
data class DoubtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentName: String,
    val question: String,
    val subject: String,
    val grade: String,
    val status: String, // "AI_ANSWERED", "TEACHER_REVIEW", "RESOLVED"
    val aiInstantAnswer: String,
    val teacherAnswer: String,
    val teacherName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val guaranteedResolveBy: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
    val userUpvotes: Int = 0
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Suresh Kumar",
    val googleUsername: String = "@suresh_student",
    val googleEmail: String = "suresh.kumar@gmail.com",
    val isGoogleSignedIn: Boolean = true,
    val userGrade: String = "Class 10 (Board Mission)",
    val points: Int = 180,
    val streakDays: Int = 4,
    val lastActiveDate: String = "2026-09-27",
    val isPro: Boolean = false,
    val proPlan: String = "FREE",
    val proExpiryDate: String = "",
    val isGiftedByOwner: Boolean = false,
    val giftNote: String = "",
    val lessonsWatchedCount: Int = 6,
    val notesReadCount: Int = 5,
    val doubtsAskedCount: Int = 2,
    val completedProjectsCount: Int = 1,
    val unlockedBadgeIdsCsv: String = "first_step,streak_3"
)

@Entity(tableName = "gifted_pro_users")
data class GiftedProUserEntity(
    @PrimaryKey val googleUsername: String, // Normalized lowercase e.g. "@rahul_kumar" or "rahul@gmail.com"
    val studentName: String = "",
    val giftedDate: String,
    val grantedBy: String = "VidyaSetu Owner",
    val scholarshipReason: String = "Free Education Initiative",
    val isPermanent: Boolean = true
)

@Entity(tableName = "live_chat_messages")
data class LiveChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lectureId: String,
    val senderName: String,
    val isTeacher: Boolean = false,
    val isDoubt: Boolean = false,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

