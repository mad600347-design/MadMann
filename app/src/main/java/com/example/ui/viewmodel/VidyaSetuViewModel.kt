package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DownloadedLectureEntity
import com.example.data.local.DoubtEntity
import com.example.data.local.GiftedProUserEntity
import com.example.data.local.LiveChatMessageEntity
import com.example.data.local.StudyNoteEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.Announcement
import com.example.data.model.Badge
import com.example.data.model.EducationalProject
import com.example.data.model.EducationalProjectsData
import com.example.data.model.LeaderboardStudent
import com.example.data.model.PracticeQuiz
import com.example.data.model.ProPlan
import com.example.data.model.QuizQuestion
import com.example.data.repository.VidyaSetuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    data object BatchesHome : Screen()
    data class LecturePlayer(val lectureId: String) : Screen()
    data object DoubtsAndAiGuru : Screen()
    data object OfflineDownloads : Screen()
    data object LeaderboardAndBadges : Screen()
    data object ProSubscription : Screen()
    data class NoteDetail(val noteId: String) : Screen()
    data object AdminPanel : Screen()
    data object AdminDashboard : Screen()
    data class PracticeQuizMode(val quizId: String) : Screen()
    data object EducationalProjects : Screen()
    data class ProjectDetail(val projectId: String) : Screen()
}


data class AiChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val sender: String, // "user", "ai_guru"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val quickTopic: String? = null
)

class VidyaSetuViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = VidyaSetuRepository(db)

    val lectures: StateFlow<List<DownloadedLectureEntity>> = repository.allLectures
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val offlineLectures: StateFlow<List<DownloadedLectureEntity>> = repository.downloadedLectures
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<StudyNoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val offlineNotes: StateFlow<List<StudyNoteEntity>> = repository.offlineNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doubts: StateFlow<List<DoubtEntity>> = repository.allDoubts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProgress: StateFlow<UserProgressEntity?> = repository.userProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val giftedUsers: StateFlow<List<GiftedProUserEntity>> = repository.allGiftedUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _educationalProjects = MutableStateFlow<List<EducationalProject>>(EducationalProjectsData.projects)
    val educationalProjects: StateFlow<List<EducationalProject>> = _educationalProjects.asStateFlow()

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.BatchesHome)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Screen navigation stack
    private val screenStack = mutableListOf<Screen>(Screen.BatchesHome)

    // AI Guru Chat in Doubts Section
    private val _aiChatMessages = MutableStateFlow<List<AiChatMessage>>(listOf(
        AiChatMessage(
            sender = "ai_guru",
            text = "Namaste! Main hoon aapka 'AI Guru' (विद्यामित्र) — VidyaSetu ka free 24x7 study companion.\n\nAap mujhse physics formulas, maths theorems, chemistry reactions, ya board exam revision ke baare me kuch bhi pooch sakte hain. Main saral bhasha me samjhaunga!"
        )
    ))
    val aiChatMessages: StateFlow<List<AiChatMessage>> = _aiChatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Admin Auth State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    // Selected Video Quality
    private val _selectedQuality = MutableStateFlow("144p - Data Saver (15MB/hr)")
    val selectedQuality: StateFlow<String> = _selectedQuality.asStateFlow()

    // Toast / Feedback message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun signInWithGoogle(username: String, email: String, displayName: String, grade: String) {
        viewModelScope.launch {
            repository.updateGoogleAccount(username, email, displayName, grade)
            showSnackbar("Signed in as $username with Google Account!")
        }
    }

    fun signOutGoogle() {
        viewModelScope.launch {
            repository.signOutGoogle()
            showSnackbar("Signed out from Google Account")
        }
    }

    fun adminGiftProToUser(googleUsername: String, studentName: String, reason: String) {
        if (googleUsername.isBlank()) {
            showSnackbar("Please enter a student's Google username or email")
            return
        }
        viewModelScope.launch {
            repository.giftProToUser(googleUsername, studentName, reason)
            showSnackbar("🎁 Free Lifetime Pro gifted to $googleUsername!")
        }
    }

    fun adminRevokeGiftedPro(googleUsername: String) {
        viewModelScope.launch {
            repository.revokeGiftedUser(googleUsername)
            showSnackbar("Pro access revoked for $googleUsername")
        }
    }

    fun checkAndClaimOwnerGift(usernameOrEmail: String) {
        if (usernameOrEmail.isBlank()) {
            showSnackbar("Please enter your Google username or email")
            return
        }
        viewModelScope.launch {
            val gifted = repository.checkAndRedeemOwnerGift(usernameOrEmail)
            if (gifted != null) {
                showSnackbar("🎉 Free Pro Activated! Gifted by Owner: ${gifted.scholarshipReason}")
            } else {
                showSnackbar("No owner giveaway found for $usernameOrEmail yet. Contact Owner Desk!")
            }
        }
    }

    fun toggleProjectCompleted(projectId: String) {
        val currentList = _educationalProjects.value
        _educationalProjects.value = currentList.map {
            if (it.id == projectId) {
                val newStatus = !it.isCompleted
                if (newStatus) {
                    viewModelScope.launch {
                        repository.awardPoints(50, "Completed Practical Science Project: ${it.title}")
                    }
                    showSnackbar("🎉 Practical Work Completed! +50 Gyan Points")
                }
                it.copy(isCompleted = newStatus)
            } else it
        }
    }

    // Configurable UPI Gateway Settings (Safe for personal, parent, or business UPI)
    private val _configuredUpiId = MutableStateFlow("vidyasetu.bharat@upi")
    val configuredUpiId: StateFlow<String> = _configuredUpiId.asStateFlow()

    private val _configuredMerchantName = MutableStateFlow("VidyaSetu Education")
    val configuredMerchantName: StateFlow<String> = _configuredMerchantName.asStateFlow()

    fun updateUpiSettings(upiId: String, merchantName: String) {
        val trimmedId = upiId.trim()
        val trimmedName = if (merchantName.isBlank()) "VidyaSetu Education" else merchantName.trim()
        if (trimmedId.isNotBlank()) {
            _configuredUpiId.value = trimmedId
            _configuredMerchantName.value = trimmedName
            showSnackbar("UPI Settings saved! Active ID: $trimmedId")
        }
    }


    // Practice Quizzes
    private val _quizzes = MutableStateFlow<List<PracticeQuiz>>(
        listOf(
            PracticeQuiz(
                id = "quiz_1",
                title = "Light: Reflection & Spherical Mirrors Speed Test",
                subject = "Physics",
                grade = "Class 10 (Mission Board)",
                durationMinutes = 8,
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "The focal length of a spherical mirror of radius of curvature 30 cm is:",
                        options = listOf("15 cm", "30 cm", "60 cm", "10 cm"),
                        correctOptionIndex = 0,
                        explanation = "Focal length f = R / 2 = 30 / 2 = 15 cm."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "An image formed by a concave mirror is observed to be virtual, erect and larger than the object. The position of the object should be:",
                        options = listOf("Between focus and center of curvature", "At center of curvature", "Beyond center of curvature", "Between pole and principal focus"),
                        correctOptionIndex = 3,
                        explanation = "Only when an object is placed between the pole and focus of a concave mirror does it form a virtual, erect, and magnified image."
                    ),
                    QuizQuestion(
                        id = 3,
                        question = "The magnification produced by a rear-view mirror fitted in vehicles is:",
                        options = listOf("Always less than 1", "Always greater than 1", "Equal to 1", "Can be more or less than 1"),
                        correctOptionIndex = 0,
                        explanation = "Convex mirrors always form diminished, virtual, and erect images, so magnification m < 1 always."
                    )
                ),
                totalAttempts = 2340
            ),
            PracticeQuiz(
                id = "quiz_2",
                title = "Quadratic Equations Board Master Quiz",
                subject = "Mathematics",
                grade = "Class 10 (Lakshya Batch)",
                durationMinutes = 10,
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "If the discriminant D = b² - 4ac > 0, the quadratic equation has:",
                        options = listOf("Two distinct real roots", "Two equal real roots", "No real roots", "Complex roots only"),
                        correctOptionIndex = 0,
                        explanation = "When D > 0, there are two distinct real roots given by (-b ± √D)/(2a)."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "The sum of the roots of the quadratic equation 2x² - 8x + 6 = 0 is:",
                        options = listOf("4", "-4", "3", "-3"),
                        correctOptionIndex = 0,
                        explanation = "Sum of roots = -b/a = -(-8)/2 = 4."
                    ),
                    QuizQuestion(
                        id = 3,
                        question = "For what value of k does x² - 4x + k = 0 have equal real roots?",
                        options = listOf("4", "2", "16", "8"),
                        correctOptionIndex = 0,
                        explanation = "For equal roots, D = 0 => (-4)² - 4(1)(k) = 0 => 16 = 4k => k = 4."
                    )
                ),
                totalAttempts = 1890
            ),
            PracticeQuiz(
                id = "quiz_3",
                title = "Chemical Reactions & Equations Rapid Fire",
                subject = "Chemistry",
                grade = "Class 10 (Yakeen Foundation)",
                durationMinutes = 7,
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "When magnesium ribbon is burnt in air, the ash formed is:",
                        options = listOf("White", "Black", "Yellow", "Grey"),
                        correctOptionIndex = 0,
                        explanation = "Magnesium burns with a dazzling white flame to form white magnesium oxide (MgO) powder."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "The reaction between iron and copper sulphate solution is an example of:",
                        options = listOf("Displacement reaction", "Combination reaction", "Decomposition reaction", "Double displacement"),
                        correctOptionIndex = 0,
                        explanation = "Fe + CuSO₄ → FeSO₄ + Cu. Iron displaces copper because iron is more reactive."
                    )
                ),
                totalAttempts = 1520
            )
        )
    )
    val quizzes: StateFlow<List<PracticeQuiz>> = _quizzes.asStateFlow()

    // Announcements Notice Board
    private val _announcements = MutableStateFlow<List<Announcement>>(
        listOf(
            Announcement(
                id = "ann_1",
                title = "🔴 Today 6:00 PM: Live Doubt Resolution & Strategy Session",
                message = "Er. Alok Sir will solve live student doubts on quadratic equations and board exam time management. Join live in the Batches tab!",
                author = "Alok Sir (Founder)",
                timestamp = "Just Now",
                isImportant = true
            ),
            Announcement(
                id = "ann_2",
                title = "📚 Free DPP #4 Uploaded for Light Chapter",
                message = "Daily Practice Problem sheet with 15 numerical problems and step-by-step solutions is now available in the Offline Notes section.",
                author = "Pooja Verma (Physics Dept)",
                timestamp = "3 hrs ago",
                isImportant = false
            )
        )
    )
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    fun navigateTo(screen: Screen) {
        screenStack.add(screen)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = screenStack.last()
            return true
        }
        return false
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun setVideoQuality(quality: String) {
        _selectedQuality.value = quality
        _snackbarMessage.value = "Streaming switched to $quality (Optimized for low network)"
    }

    fun toggleLectureDownload(lecture: DownloadedLectureEntity) {
        viewModelScope.launch {
            val newState = !lecture.isDownloaded
            repository.setLectureDownloaded(lecture.id, newState)
            _snackbarMessage.value = if (newState) {
                "📥 Lecture downloaded for Offline Mode! (+40 Gyan Points)"
            } else {
                "Lecture removed from offline storage"
            }
        }
    }

    fun toggleNoteDownload(note: StudyNoteEntity) {
        viewModelScope.launch {
            val newState = !note.isDownloadedOffline
            repository.setNoteDownloaded(note.id, newState)
            _snackbarMessage.value = if (newState) {
                "📄 Notes saved offline for 0 data usage! (+25 Points)"
            } else {
                "Notes removed from offline cache"
            }
        }
    }

    fun sendLiveChatMessage(lectureId: String, senderName: String, message: String, isDoubt: Boolean) {
        if (message.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(lectureId, senderName, message, isDoubt)
        }
    }

    fun getChatMessagesForLecture(lectureId: String) = repository.getChatMessages(lectureId)

    fun askAiGuruInChat(question: String, subject: String = "General Science & Math", grade: String = "Class 10") {
        if (question.isBlank()) return
        val userMsg = AiChatMessage(sender = "user", text = question)
        _aiChatMessages.value = _aiChatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            try {
                val answer = repository.askAiGuruDirect(question)
                val aiMsg = AiChatMessage(sender = "ai_guru", text = answer)
                _aiChatMessages.value = _aiChatMessages.value + aiMsg
                // Also award points for asking doubt
                repository.awardPoints(15, "Consulted AI Guru for Concept Clarity")
            } catch (e: Exception) {
                val fallbackMsg = AiChatMessage(
                    sender = "ai_guru",
                    text = "Aapka doubt receive ho gaya hai! Vidyasetu study repository ke mutabik: Yeh formula/concept core NCERT syllabus me shamil hai. Step-by-step revision tips offline notes tab me bhi uplabdh hain."
                )
                _aiChatMessages.value = _aiChatMessages.value + fallbackMsg
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun submitOfficialDoubt(question: String, subject: String, grade: String) {
        if (question.isBlank()) return
        viewModelScope.launch {
            repository.askDoubt(
                question = question,
                subject = subject,
                grade = grade,
                studentName = userProgress.value?.userName ?: "Suresh Kumar"
            )
            _snackbarMessage.value = "✅ Doubt posted! AI instant resolution attached & Human Teacher assigned (<24hr guarantee)"
        }
    }

    fun activatePro(plan: ProPlan, transactionRef: String) {
        viewModelScope.launch {
            repository.activateProPlan(plan, transactionRef)
            _snackbarMessage.value = "🎉 Pro Membership Activated! Welcome to VidyaSetu Elite."
        }
    }

    fun verifyAdminPassword(password: String): Boolean {
        if (password == "VidyaSetu123") {
            _isAdminAuthenticated.value = true
            _snackbarMessage.value = "🔓 Admin Access Granted! Welcome Founder/Teacher."
            return true
        } else {
            _snackbarMessage.value = "❌ Incorrect Admin Password! Try again."
            return false
        }
    }

    fun adminLogout() {
        _isAdminAuthenticated.value = false
        _snackbarMessage.value = "Logged out from Admin Console"
    }

    fun adminUploadLecture(
        title: String,
        subject: String,
        grade: String,
        teacher: String,
        durationMinutes: Int,
        aiSummary: String,
        keyFormulas: String,
        isLiveNow: Boolean
    ) {
        viewModelScope.launch {
            repository.adminUploadLecture(
                title, subject, grade, teacher, durationMinutes, aiSummary, keyFormulas, isLiveNow
            )
            _snackbarMessage.value = "🚀 New Lecture Uploaded Successfully!"
        }
    }

    fun adminUploadNote(
        title: String,
        subject: String,
        grade: String,
        chapter: String,
        pageCount: Int,
        contentSummary: String,
        detailedNotesMarkdown: String
    ) {
        viewModelScope.launch {
            repository.adminUploadNote(
                title, subject, grade, chapter, pageCount, contentSummary, detailedNotesMarkdown
            )
            _snackbarMessage.value = "📚 Study Notes Added to Repository!"
        }
    }

    fun adminResolveDoubt(doubtId: Long, teacherAnswer: String, teacherName: String) {
        viewModelScope.launch {
            repository.adminResolveDoubt(doubtId, teacherAnswer, teacherName)
            _snackbarMessage.value = "✅ Doubt resolved & notified to student!"
        }
    }

    fun markLessonCompleted(lectureId: String) {
        viewModelScope.launch {
            repository.markLessonCompleted(lectureId)
            _snackbarMessage.value = "🌟 Lesson Completed! +50 Gyan Points Added!"
        }
    }

    fun submitQuizScore(quizId: String, score: Int, total: Int) {
        viewModelScope.launch {
            val points = score * 20
            repository.awardPoints(points, "Completed Quiz $score/$total")
            _snackbarMessage.value = "🎯 Quiz Completed! Score: $score/$total (+$points Points)!"
        }
    }

    fun claimDailyStreak() {
        viewModelScope.launch {
            repository.awardPoints(25, "Daily Study Check-in Bonus")
            _snackbarMessage.value = "🔥 Daily Study Streak Maintained! +25 Gyan Points Awarded!"
        }
    }

    fun adminPublishAnnouncement(title: String, message: String, isImportant: Boolean) {
        if (title.isBlank() || message.isBlank()) return
        val newAnn = Announcement(
            id = "ann_${System.currentTimeMillis()}",
            title = title,
            message = message,
            author = "VidyaSetu Founder Desk",
            timestamp = "Just now",
            isImportant = isImportant
        )
        _announcements.value = listOf(newAnn) + _announcements.value
        _snackbarMessage.value = "📢 Announcement Broadcasted to All Students!"
    }

    fun adminUploadQuiz(
        title: String,
        subject: String,
        grade: String,
        durationMinutes: Int,
        questions: List<QuizQuestion>
    ) {
        val newQuiz = PracticeQuiz(
            id = "quiz_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            grade = grade,
            durationMinutes = durationMinutes,
            questions = questions
        )
        _quizzes.value = _quizzes.value + newQuiz
        _snackbarMessage.value = "📝 New Practice Quiz Uploaded for Students!"
    }

    fun getBadges(): List<Badge> {
        val unlockedCsv = userProgress.value?.unlockedBadgeIdsCsv ?: ""
        return repository.getAllBadges(unlockedCsv)
    }

    fun getLeaderboard(): List<LeaderboardStudent> {
        val currentPoints = userProgress.value?.points ?: 180
        val baseList = repository.getLeaderboard()
        return baseList.map {
            if (it.isCurrentUser) it.copy(points = currentPoints) else it
        }.sortedByDescending { it.points }.mapIndexed { index, student ->
            student.copy(rank = index + 1)
        }
    }
}
