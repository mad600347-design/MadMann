package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DownloadedLectureEntity
import com.example.data.local.DoubtEntity
import com.example.data.local.GiftedProUserEntity
import com.example.data.local.LiveChatMessageEntity
import com.example.data.local.StudyNoteEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.Badge
import com.example.data.model.LeaderboardStudent
import com.example.data.model.ProPlan
import com.example.data.network.GeminiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VidyaSetuRepository(
    private val db: AppDatabase,
    private val geminiService: GeminiService = GeminiService()
) {
    private val lectureDao = db.lectureDao()
    private val noteDao = db.noteDao()
    private val doubtDao = db.doubtDao()
    private val userProgressDao = db.userProgressDao()
    private val chatDao = db.chatDao()
    private val giftedProUserDao = db.giftedProUserDao()

    val allLectures: Flow<List<DownloadedLectureEntity>> = lectureDao.getAllLectures()
    val downloadedLectures: Flow<List<DownloadedLectureEntity>> = lectureDao.getDownloadedLectures()
    val allNotes: Flow<List<StudyNoteEntity>> = noteDao.getAllNotes()
    val offlineNotes: Flow<List<StudyNoteEntity>> = noteDao.getOfflineNotes()
    val allDoubts: Flow<List<DoubtEntity>> = doubtDao.getAllDoubts()
    val userProgress: Flow<UserProgressEntity?> = userProgressDao.getUserProgress()
    val allGiftedUsers: Flow<List<GiftedProUserEntity>> = giftedProUserDao.getAllGiftedUsers()

    fun getChatMessages(lectureId: String): Flow<List<LiveChatMessageEntity>> =
        chatDao.getMessagesForLecture(lectureId)


    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfEmpty()
        }
    }

    private suspend fun seedInitialDataIfEmpty() {
        val currentLectures = lectureDao.getAllLectures().firstOrNull()
        if (currentLectures.isNullOrEmpty()) {
            val seedLectures = listOf(
                DownloadedLectureEntity(
                    id = "lec_live_1",
                    title = "Quadratic Equations & Parabola Modeling: Complete Masterclass",
                    subject = "Mathematics",
                    grade = "Class 10 (Lakshya Batch)",
                    teacher = "Er. Alok Sharma (VidyaSetu Founder)",
                    durationMinutes = 60,
                    aiSummary = "Roots of quadratic equation, Nature of roots (Discriminant D > 0, D = 0, D < 0), Graphical interpretation, and shortcut tricks for Board Exam speed.",
                    keyFormulas = "ax² + bx + c = 0 | x = (-b ± √(b² - 4ac)) / 2a | D = b² - 4ac",
                    fileSizeMb = 48.5,
                    isDownloaded = false,
                    isLiveNow = false
                ),
                DownloadedLectureEntity(
                    id = "lec_2",
                    title = "Light: Reflection, Spherical Mirrors & Ray Diagrams (PW Style)",
                    subject = "Physics / Science",
                    grade = "Class 10 (Mission Board)",
                    teacher = "Pooja Verma (Gold Medalist)",
                    durationMinutes = 45,
                    aiSummary = "Concave & convex mirrors, sign convention cartesian rules, mirror formula, magnification, and 6 essential ray diagrams.",
                    keyFormulas = "1/f = 1/v + 1/u | m = -v/u = h'/h | Power P = 1/f(m)",
                    fileSizeMb = 36.2,
                    isDownloaded = true,
                    downloadTimestamp = System.currentTimeMillis() - 86400000L,
                    isLiveNow = false
                ),
                DownloadedLectureEntity(
                    id = "lec_3",
                    title = "Chemical Reactions & Balancing Equations in 30 Seconds",
                    subject = "Chemistry / Science",
                    grade = "Class 10 (Yakeen Foundation)",
                    teacher = "Dr. Vikas Tripathi",
                    durationMinutes = 40,
                    aiSummary = "Types of chemical reactions: Combination, Decomposition, Displacement, Double Displacement, Redox reactions, and Corrosion prevention.",
                    keyFormulas = "Reactants → Products | Law of Conservation of Mass | Zn + H₂SO₄ → ZnSO₄ + H₂↑",
                    fileSizeMb = 32.0,
                    isDownloaded = false,
                    isLiveNow = false
                ),
                DownloadedLectureEntity(
                    id = "lec_4",
                    title = "Trigonometry Heights and Distances Shortcuts (No Angle Fear!)",
                    subject = "Mathematics",
                    grade = "Class 10 & 11",
                    teacher = "Er. Alok Sharma",
                    durationMinutes = 52,
                    aiSummary = "Trigonometric ratios sin, cos, tan, cot, sec, cosec with mnemonic tricks, standard angles (0°, 30°, 45°, 60°, 90°), and elevation/depression angle problems.",
                    keyFormulas = "sin²θ + cos²θ = 1 | 1 + tan²θ = sec²θ | 1 + cot²θ = cosec²θ",
                    fileSizeMb = 42.4,
                    isDownloaded = true,
                    downloadTimestamp = System.currentTimeMillis() - 43200000L,
                    isLiveNow = false
                ),
                DownloadedLectureEntity(
                    id = "lec_5",
                    title = "Spoken English & Interview Confidence for Hindi Medium Students",
                    subject = "English & Life Skills",
                    grade = "All Students",
                    teacher = "Simran Kaur",
                    durationMinutes = 35,
                    aiSummary = "Overcoming hesitation, common grammatical errors, daily conversation phrases in village/city settings, and introduction speech structuring.",
                    keyFormulas = "Subject + Auxiliary Verb + Action + Context | Active vs Passive Voice",
                    fileSizeMb = 28.1,
                    isDownloaded = false,
                    isLiveNow = false
                )
            )
            lectureDao.insertLectures(seedLectures)
        }

        val currentNotes = noteDao.getAllNotes().firstOrNull()
        if (currentNotes.isNullOrEmpty()) {
            val seedNotes = listOf(
                StudyNoteEntity(
                    id = "note_1",
                    title = "Complete Class 10 Math Formula Cheat-Sheet (NCERT)",
                    subject = "Mathematics",
                    grade = "Class 10",
                    chapter = "All Chapters 1-14",
                    pageCount = 12,
                    fileSizeKb = 850,
                    contentSummary = "Compact handwritten formula book covering Real Numbers, Polynomials, Linear Equations, Quadratics, AP, Triangles, Coordinate Geometry, Trigonometry, Circles, Surface Area & Volume, Statistics, and Probability.",
                    detailedNotesMarkdown = """
                        # VidyaSetu Complete Math Formula Sheet
                        ## 1. Real Numbers
                        - Fundamental Theorem of Arithmetic: Every composite number can be uniquely expressed as product of primes.
                        - HCF(a, b) × LCM(a, b) = a × b
                        
                        ## 2. Polynomials
                        - For quadratic polynomial ax² + bx + c:
                          - Sum of zeroes (α + β) = -b/a
                          - Product of zeroes (αβ) = c/a
                        
                        ## 3. Arithmetic Progression (AP)
                        - nth term: aₙ = a + (n - 1)d
                        - Sum of n terms: Sₙ = n/2 [2a + (n - 1)d] = n/2 [a + l]
                        
                        ## 4. Trigonometric Identities
                        - sin²θ + cos²θ = 1
                        - sec²θ - tan²θ = 1
                        - cosec²θ - cot²θ = 1
                    """.trimIndent(),
                    isDownloadedOffline = true,
                    downloadTimestamp = System.currentTimeMillis()
                ),
                StudyNoteEntity(
                    id = "note_2",
                    title = "Science Life Processes & Reproduction Quick Revision Notes",
                    subject = "Biology / Science",
                    grade = "Class 10",
                    chapter = "Chapter 6 & 8",
                    pageCount = 8,
                    fileSizeKb = 620,
                    contentSummary = "Diagram-rich summary of Nutrition (Autotrophic & Heterotrophic), Respiration (Aerobic vs Anaerobic), Transportation in human heart & plants (Xylem/Phloem), and Excretion (Nephron structure).",
                    detailedNotesMarkdown = """
                        # Life Processes - High Yield Revision
                        ## Nutrition
                        - Autotrophic: Plants use CO₂, H₂O, sunlight & chlorophyll.
                        - Heterotrophic: Holozoic (Amoeba, Humans), Saprophytic (Fungi), Parasitic (Cuscuta).
                        
                        ## Respiration
                        - Glycolysis in cytoplasm: Glucose (6C) → Pyruvate (3C).
                        - Absence of O₂ (Yeast): Ethanol + CO₂ + Energy (2 ATP).
                        - Lack of O₂ (Muscles): Lactic acid + Energy (causes cramps!).
                        - Presence of O₂ (Mitochondria): CO₂ + H₂O + Energy (38 ATP).
                    """.trimIndent(),
                    isDownloadedOffline = false
                ),
                StudyNoteEntity(
                    id = "note_3",
                    title = "Newton's Laws & Gravitation Concept Booklet (DPP Attached)",
                    subject = "Physics",
                    grade = "Class 9 & 11",
                    chapter = "Laws of Motion",
                    pageCount = 6,
                    fileSizeKb = 490,
                    contentSummary = "Equations of motion under gravity, Free fall, Universal Law of Gravitation, Difference between Mass & Weight, and Archimedes Principle.",
                    detailedNotesMarkdown = """
                        # Physics: Motion & Gravitation
                        ## Equations of Uniform Motion:
                        1. v = u + at
                        2. s = ut + ½at²
                        3. v² = u² + 2as
                        
                        ## Gravitation:
                        - Universal Force: F = G (m₁m₂) / r²
                        - G = 6.67 × 10⁻¹¹ N m²/kg²
                        - Acceleration due to gravity: g = GM / R² ≈ 9.8 m/s²
                        - Weight = m × g (changes on Moon to 1/6th).
                    """.trimIndent(),
                    isDownloadedOffline = true,
                    downloadTimestamp = System.currentTimeMillis()
                )
            )
            noteDao.insertNotes(seedNotes)
        }

        val currentDoubts = doubtDao.getAllDoubts().firstOrNull()
        if (currentDoubts.isNullOrEmpty()) {
            val seedDoubts = listOf(
                DoubtEntity(
                    studentName = "Rakesh Mondal (Bihar)",
                    question = "How to find roots of equation 2x² - 7x + 3 = 0 using factorisation?",
                    subject = "Mathematics",
                    grade = "Class 10",
                    status = "RESOLVED",
                    aiInstantAnswer = "To factorise 2x² - 7x + 3 = 0, multiply first and last coefficient: 2 × 3 = 6. Split middle term -7 into -6 and -1. 2x² - 6x - x + 3 = 0 → 2x(x - 3) - 1(x - 3) = 0 → (2x - 1)(x - 3) = 0. Therefore, roots are x = 1/2 and x = 3.",
                    teacherAnswer = "Bahut badiya question! Alok Sir verified: Always check signs when grouping terms. (2x - 1) gives x = 1/2. Keep practicing such board questions!",
                    teacherName = "Er. Alok Sharma (VidyaSetu Founder)",
                    createdAt = System.currentTimeMillis() - 12 * 3600000L,
                    guaranteedResolveBy = System.currentTimeMillis() + 12 * 3600000L,
                    userUpvotes = 24
                ),
                DoubtEntity(
                    studentName = "Priyanka Patel (MP)",
                    question = "Why does sky appear blue during daytime and red during sunset?",
                    subject = "Physics / Science",
                    grade = "Class 10",
                    status = "RESOLVED",
                    aiInstantAnswer = "Sky appears blue due to Rayleigh Scattering. Blue light has shorter wavelength, so it is scattered most by atmospheric gas particles. During sunset, sunlight travels longer path through atmosphere, blue light is scattered away and longer red wavelength reaches our eyes.",
                    teacherAnswer = "Pooja Ma'am added: Formula is Scattering Intensity ∝ 1 / λ⁴. Since Red wavelength is ~1.8 times blue, blue scatters ~10 times more!",
                    teacherName = "Pooja Verma (Physics Mentor)",
                    createdAt = System.currentTimeMillis() - 20 * 3600000L,
                    guaranteedResolveBy = System.currentTimeMillis() + 4 * 3600000L,
                    userUpvotes = 38
                ),
                DoubtEntity(
                    studentName = "Suresh Kumar (You)",
                    question = "What is the difference between respiration and breathing?",
                    subject = "Biology",
                    grade = "Class 10",
                    status = "AI_ANSWERED",
                    aiInstantAnswer = "Breathing is physical mechanical process of inhaling O₂ and exhaling CO₂ (no energy produced). Respiration is biochemical cellular process inside cells where glucose is broken down with O₂ to produce ATP energy.",
                    teacherAnswer = "Teacher is reviewing your question (Guaranteed response in 18 hrs)",
                    teacherName = "Assigned to Dr. Vikas",
                    createdAt = System.currentTimeMillis() - 2 * 3600000L,
                    guaranteedResolveBy = System.currentTimeMillis() + 22 * 3600000L,
                    userUpvotes = 5
                )
            )
            doubtDao.insertDoubts(seedDoubts)
        }

        val currentProgress = userProgressDao.getUserProgress().firstOrNull()
        if (currentProgress == null) {
            userProgressDao.insertOrUpdate(UserProgressEntity())
        }

        val currentGifted = giftedProUserDao.getAllGiftedUsers().firstOrNull()
        if (currentGifted.isNullOrEmpty()) {
            val seedGifted = listOf(
                GiftedProUserEntity(
                    googleUsername = "@aman_kumar",
                    studentName = "Aman Kumar",
                    giftedDate = "25 Sep 2026",
                    grantedBy = "VidyaSetu Owner Desk",
                    scholarshipReason = "Rural Student Merit & Need Sponsorship",
                    isPermanent = true
                ),
                GiftedProUserEntity(
                    googleUsername = "sneha.patel@gmail.com",
                    studentName = "Sneha Patel",
                    giftedDate = "20 Sep 2026",
                    grantedBy = "VidyaSetu Owner Desk",
                    scholarshipReason = "Top Performer in Science Practical Project",
                    isPermanent = true
                )
            )
            giftedProUserDao.insertGiftedUsers(seedGifted)
        }

        val currentChat = chatDao.getMessagesForLecture("lec_live_1").firstOrNull()
        if (currentChat.isNullOrEmpty()) {
            val seedChat = listOf(
                LiveChatMessageEntity(
                    lectureId = "lec_live_1",
                    senderName = "Er. Alok Sharma (Faculty)",
                    isTeacher = true,
                    isDoubt = false,
                    message = "Welcome to this concept lesson on Quadratic Equations. Notice how the quadratic parabola models actual physical trajectories like projectile motion."
                ),
                LiveChatMessageEntity(
                    lectureId = "lec_live_1",
                    senderName = "Sneha (Student)",
                    isTeacher = false,
                    isDoubt = true,
                    message = "Why do roots become non-real when the discriminant D is less than zero?"
                ),
                LiveChatMessageEntity(
                    lectureId = "lec_live_1",
                    senderName = "Er. Alok Sharma (Faculty)",
                    isTeacher = true,
                    isDoubt = false,
                    message = "Because the square root of a negative quantity is not a real number. Geometrically, the parabola never crosses the x-axis!"
                )
            )
            chatDao.insertMessages(seedChat)
        }
    }

    suspend fun setLectureDownloaded(id: String, isDownloaded: Boolean) {
        lectureDao.setDownloaded(id, isDownloaded, if (isDownloaded) System.currentTimeMillis() else 0L)
        if (isDownloaded) {
            awardPoints(40, "Downloaded Lecture for Offline Study")
        }
    }

    suspend fun setNoteDownloaded(id: String, isDownloaded: Boolean) {
        noteDao.setDownloaded(id, isDownloaded, if (isDownloaded) System.currentTimeMillis() else 0L)
        if (isDownloaded) {
            awardPoints(25, "Saved Study Notes Offline")
        }
    }

    suspend fun askDoubt(question: String, subject: String, grade: String, studentName: String = "Suresh Kumar"): Long {
        // AI Guru generates the instant pedagogical response
        val aiResponse = geminiService.askDoubt(question, subject, grade)

        val newDoubt = DoubtEntity(
            studentName = studentName,
            question = question,
            subject = subject,
            grade = grade,
            status = "AI_ANSWERED",
            aiInstantAnswer = aiResponse,
            teacherAnswer = "Assigned to VidyaSetu Educator Desk. Guaranteed response within 24 hours.",
            teacherName = "Master Educator Queue",
            createdAt = System.currentTimeMillis(),
            guaranteedResolveBy = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
        )
        val id = doubtDao.insertDoubt(newDoubt)
        awardPoints(30, "Asked a Doubt on VidyaDesk")
        updateDoubtCount()
        return id
    }

    suspend fun askAiGuruDirect(prompt: String): String {
        return geminiService.askDoubt(prompt, "General Study", "All Classes")
    }

    suspend fun sendChatMessage(lectureId: String, senderName: String, message: String, isDoubt: Boolean) {
        val chat = LiveChatMessageEntity(
            lectureId = lectureId,
            senderName = senderName,
            isTeacher = false,
            isDoubt = isDoubt,
            message = message,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(chat)
        awardPoints(5, "Participated in Live Classroom Chat")
    }

    suspend fun activateProPlan(plan: ProPlan, transactionId: String) {
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val expiryTime = System.currentTimeMillis() + when (plan) {
            ProPlan.ONE_MONTH -> 30L * 24 * 3600 * 1000L
            ProPlan.SIX_MONTHS, ProPlan.SCHOLARSHIP -> 180L * 24 * 3600 * 1000L
            ProPlan.OWNER_GIFTED -> 3650L * 24 * 3600 * 1000L
        }
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val expiryString = sdf.format(Date(expiryTime))

        val updated = current.copy(
            isPro = true,
            proPlan = plan.title,
            proExpiryDate = expiryString,
            points = current.points + 100
        )
        userProgressDao.insertOrUpdate(updated)
    }

    suspend fun giftProToUser(googleUsername: String, studentName: String, reason: String): Boolean {
        val cleanUsername = googleUsername.trim().lowercase()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDateStr = sdf.format(Date())

        val giftedEntity = GiftedProUserEntity(
            googleUsername = cleanUsername,
            studentName = if (studentName.isBlank()) "Scholar ($cleanUsername)" else studentName.trim(),
            giftedDate = currentDateStr,
            grantedBy = "VidyaSetu Owner Desk",
            scholarshipReason = if (reason.isBlank()) "Free Education Sponsorship (Gifted by Owner)" else reason.trim(),
            isPermanent = true
        )
        giftedProUserDao.insertGiftedUser(giftedEntity)

        // If the current student's google username or email matches, elevate them immediately!
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val curUserClean = current.googleUsername.trim().lowercase().removePrefix("@")
        val inputClean = cleanUsername.removePrefix("@")
        if (curUserClean == inputClean || current.googleEmail.trim().lowercase() == cleanUsername) {
            val updated = current.copy(
                isPro = true,
                proPlan = "LIFETIME_GIFTED",
                proExpiryDate = "Lifetime Access (Gifted by Owner)",
                isGiftedByOwner = true,
                giftNote = giftedEntity.scholarshipReason,
                points = current.points + 200
            )
            userProgressDao.insertOrUpdate(updated)
        }
        return true
    }

    suspend fun revokeGiftedUser(googleUsername: String) {
        val clean = googleUsername.trim().lowercase()
        giftedProUserDao.deleteGiftedUser(clean)
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val curUserClean = current.googleUsername.trim().lowercase().removePrefix("@")
        val targetClean = clean.removePrefix("@")
        if (curUserClean == targetClean || current.googleEmail.trim().lowercase() == clean) {
            if (current.isGiftedByOwner) {
                val updated = current.copy(
                    isPro = false,
                    proPlan = "FREE",
                    proExpiryDate = "",
                    isGiftedByOwner = false,
                    giftNote = ""
                )
                userProgressDao.insertOrUpdate(updated)
            }
        }
    }

    suspend fun checkAndRedeemOwnerGift(usernameOrEmail: String): GiftedProUserEntity? {
        val clean = usernameOrEmail.trim().lowercase()
        val cleanNoAt = clean.removePrefix("@")
        val gifted = giftedProUserDao.getGiftedUserDirect(clean, clean) 
            ?: giftedProUserDao.getGiftedUserDirect("@$cleanNoAt", cleanNoAt)
        if (gifted != null) {
            val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
            val updated = current.copy(
                isPro = true,
                proPlan = "LIFETIME_GIFTED",
                proExpiryDate = "Lifetime Access (Gifted by Owner)",
                isGiftedByOwner = true,
                giftNote = gifted.scholarshipReason,
                points = current.points + 200
            )
            userProgressDao.insertOrUpdate(updated)
        }
        return gifted
    }

    suspend fun updateGoogleAccount(googleUsername: String, googleEmail: String, displayName: String, grade: String) {
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val rawUser = googleUsername.trim()
        val formattedUsername = if (rawUser.startsWith("@")) rawUser else "@$rawUser"
        val cleanEmail = googleEmail.trim().lowercase()

        // Check if this student's Google account was gifted Pro by the owner
        val gifted = giftedProUserDao.getGiftedUserDirect(formattedUsername.lowercase(), cleanEmail)
            ?: giftedProUserDao.getGiftedUserDirect(formattedUsername.removePrefix("@").lowercase(), cleanEmail)

        val isGifted = gifted != null
        val updated = current.copy(
            userName = if (displayName.isNotBlank()) displayName.trim() else current.userName,
            googleUsername = formattedUsername,
            googleEmail = cleanEmail,
            isGoogleSignedIn = true,
            userGrade = if (grade.isNotBlank()) grade.trim() else current.userGrade,
            isPro = if (isGifted) true else current.isPro,
            proPlan = if (isGifted) "LIFETIME_GIFTED" else current.proPlan,
            proExpiryDate = if (isGifted) "Lifetime Access (Gifted by Owner)" else current.proExpiryDate,
            isGiftedByOwner = if (isGifted) true else current.isGiftedByOwner,
            giftNote = if (isGifted) (gifted?.scholarshipReason ?: "Free Pro Owner Sponsorship") else current.giftNote
        )
        userProgressDao.insertOrUpdate(updated)
    }

    suspend fun signOutGoogle() {
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val updated = current.copy(
            isGoogleSignedIn = false
        )
        userProgressDao.insertOrUpdate(updated)
    }


    suspend fun awardPoints(pointsToAdd: Int, reason: String) {
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val newPoints = current.points + pointsToAdd
        
        val currentBadges = current.unlockedBadgeIdsCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
        if (newPoints >= 100) currentBadges.add("points_100")
        if (newPoints >= 300) currentBadges.add("points_300")
        if (newPoints >= 500) currentBadges.add("points_500")

        val updated = current.copy(
            points = newPoints,
            unlockedBadgeIdsCsv = currentBadges.joinToString(",")
        )
        userProgressDao.insertOrUpdate(updated)
    }

    suspend fun markLessonCompleted(lectureId: String) {
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val currentBadges = current.unlockedBadgeIdsCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
        val newCount = current.lessonsWatchedCount + 1
        if (newCount >= 5) currentBadges.add("lecture_5")
        
        val updated = current.copy(
            lessonsWatchedCount = newCount,
            points = current.points + 50,
            unlockedBadgeIdsCsv = currentBadges.joinToString(",")
        )
        userProgressDao.insertOrUpdate(updated)
    }

    private suspend fun updateDoubtCount() {
        val current = userProgressDao.getUserProgress().firstOrNull() ?: UserProgressEntity()
        val currentBadges = current.unlockedBadgeIdsCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
        currentBadges.add("doubt_asker")
        val updated = current.copy(
            doubtsAskedCount = current.doubtsAskedCount + 1,
            unlockedBadgeIdsCsv = currentBadges.joinToString(",")
        )
        userProgressDao.insertOrUpdate(updated)
    }

    // Admin Operations
    suspend fun adminUploadLecture(
        title: String,
        subject: String,
        grade: String,
        teacher: String,
        durationMinutes: Int,
        aiSummary: String,
        keyFormulas: String,
        isLiveNow: Boolean
    ) {
        val id = "lec_" + System.currentTimeMillis()
        val lecture = DownloadedLectureEntity(
            id = id,
            title = title,
            subject = subject,
            grade = grade,
            teacher = teacher.ifBlank { "Teacher (VidyaSetu)" },
            durationMinutes = durationMinutes,
            aiSummary = aiSummary,
            keyFormulas = keyFormulas,
            fileSizeMb = (durationMinutes * 0.8),
            isDownloaded = false,
            isLiveNow = isLiveNow
        )
        lectureDao.insertLecture(lecture)
    }

    suspend fun adminUploadNote(
        title: String,
        subject: String,
        grade: String,
        chapter: String,
        pageCount: Int,
        contentSummary: String,
        detailedNotesMarkdown: String
    ) {
        val id = "note_" + System.currentTimeMillis()
        val note = StudyNoteEntity(
            id = id,
            title = title,
            subject = subject,
            grade = grade,
            chapter = chapter,
            pageCount = pageCount,
            fileSizeKb = pageCount * 70,
            contentSummary = contentSummary,
            detailedNotesMarkdown = detailedNotesMarkdown,
            isDownloadedOffline = false
        )
        noteDao.insertNote(note)
    }

    suspend fun adminResolveDoubt(doubtId: Long, teacherAnswer: String, teacherName: String) {
        doubtDao.resolveDoubtByTeacher(doubtId, teacherAnswer, teacherName.ifBlank { "Teacher (VidyaSetu Admin)" })
    }

    fun getAllBadges(unlockedIdsCsv: String): List<Badge> {
        val unlockedSet = unlockedIdsCsv.split(",").toSet()
        return listOf(
            Badge(
                id = "first_step",
                title = "विद्या आरम्भ (First Step)",
                description = "Started learning journey with VidyaSetu",
                iconEmoji = "🌱",
                isUnlocked = true,
                requiredPoints = 0
            ),
            Badge(
                id = "streak_3",
                title = "अग्नि संकल्प (3-Day Streak)",
                description = "Maintained a 3-day active study streak",
                iconEmoji = "🔥",
                isUnlocked = unlockedSet.contains("streak_3") || true,
                requiredPoints = 50
            ),
            Badge(
                id = "doubt_asker",
                title = "जिज्ञासु छात्र (Doubt Seeker)",
                description = "Asked a doubt to AI Guru & Teacher Desk",
                iconEmoji = "💡",
                isUnlocked = unlockedSet.contains("doubt_asker"),
                requiredPoints = 100
            ),
            Badge(
                id = "lecture_5",
                title = "पाठक शिरोमणि (5 Lectures)",
                description = "Completed 5 full video lectures",
                iconEmoji = "🎯",
                isUnlocked = unlockedSet.contains("lecture_5"),
                requiredPoints = 200
            ),
            Badge(
                id = "points_300",
                title = "मेधावी वीर (300 Gyan Points)",
                description = "Crossed 300 Vidya Points on the platform",
                iconEmoji = "🎖️",
                isUnlocked = unlockedSet.contains("points_300"),
                requiredPoints = 300
            ),
            Badge(
                id = "points_500",
                title = "शिक्षा सम्राट (Vidya Samrat)",
                description = "Crossed 500 Vidya Points & inspired peers",
                iconEmoji = "👑",
                isUnlocked = unlockedSet.contains("points_500"),
                requiredPoints = 500
            )
        )
    }

    fun getLeaderboard(): List<LeaderboardStudent> {
        return listOf(
            LeaderboardStudent(1, "Aarav Sharma", "Patna, Bihar", 840, 19, false, "👑 National Top Ranker"),
            LeaderboardStudent(2, "Meera Kumari", "Ranchi, Jharkhand", 790, 16, false, "🌟 Math Wizard"),
            LeaderboardStudent(3, "Deepak Yadav", "Varanasi, UP", 720, 14, false, "⚡ Science Prodigy"),
            LeaderboardStudent(4, "Suresh Kumar", "Jaipur, Rajasthan", 180, 4, true, "🔥 Rising Star (You)"),
            LeaderboardStudent(5, "Anjali Devi", "Bhopal, MP", 650, 12, false, "📚 Board Aspirant"),
            LeaderboardStudent(6, "Rohit Verma", "Alwar, Rajasthan", 580, 10, false, "💡 Active Doubter"),
            LeaderboardStudent(7, "Pooja Mondal", "Kolkata, WB", 510, 9, false, "🎯 Concept Champion")
        )
    }
}
