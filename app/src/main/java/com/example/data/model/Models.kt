package com.example.data.model

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false,
    val requiredPoints: Int = 0
)

data class LeaderboardStudent(
    val rank: Int,
    val name: String,
    val location: String,
    val points: Int,
    val streak: Int,
    val isCurrentUser: Boolean = false,
    val badgeTitle: String
)

enum class VideoQuality(val label: String, val badge: String) {
    DATA_SAVER_144P("144p - Ultra Saver", "15 MB/hr"),
    STANDARD_360P("360p - Balanced", "45 MB/hr"),
    HIGH_720P("720p - HD Clarity", "120 MB/hr")
}

enum class ProPlan(
    val id: String,
    val title: String,
    val durationText: String,
    val priceInr: Int,
    val savingsText: String,
    val isPopular: Boolean = false
) {
    ONE_MONTH("1_month", "1 Month Access", "30 Days", 9, "Only ₹0.30/day", false),
    SIX_MONTHS("6_months", "6 Months Access", "180 Days", 29, "Best Value • ₹4.8/mo", true),
    SCHOLARSHIP("scholarship", "Shiksha Samman Free Scholarship", "180 Days", 0, "100% Free for needy students", false),
    OWNER_GIFTED("owner_gifted", "Lifetime Pro (Gifted by Owner)", "Lifetime Access", 0, "100% Free Owner Sponsorship", true)
}

data class EducationalProject(
    val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val durationText: String,
    val practicalCategory: String, // "Physics Lab", "Chemistry Practical", "Math Model", "Biology Study", "STEM Coding"
    val objective: String,
    val materialsRequired: List<String>,
    val theoryAndPrinciple: String,
    val stepByStepProcedure: List<String>,
    val observationTable: List<Pair<String, String>>, // Label/Metric to Value/Template
    val calculations: String,
    val vivaQuestions: List<Pair<String, String>>, // Question to Expected Answer
    val boardSubmissionFormat: String,
    val isCompleted: Boolean = false
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)

data class PracticeQuiz(
    val id: String,
    val title: String,
    val subject: String,
    val grade: String,
    val durationMinutes: Int,
    val questions: List<QuizQuestion>,
    val totalAttempts: Int = 1420
)

data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val author: String = "Alok Sir (Founder)",
    val timestamp: String = "Today",
    val isImportant: Boolean = true
)

