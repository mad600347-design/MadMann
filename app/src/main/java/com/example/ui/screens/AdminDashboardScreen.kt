package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DoubtEntity
import com.example.data.local.GiftedProUserEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.QuizQuestion
import com.example.ui.components.AdminLoginDialog
import com.example.ui.theme.BrightSaffron
import com.example.ui.theme.DeepIndigoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenAmber
import com.example.ui.viewmodel.VidyaSetuViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: VidyaSetuViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val doubts by viewModel.doubts.collectAsState()
    val lectures by viewModel.lectures.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val quizzes by viewModel.quizzes.collectAsState()
    val giftedUsers by viewModel.giftedUsers.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()


    var showLoginDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    if (showLoginDialog) {
        AdminLoginDialog(
            onDismiss = { showLoginDialog = false },
            onSuccess = {
                viewModel.verifyAdminPassword("VidyaSetu123")
                showLoginDialog = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Surface(
            color = DeepIndigoPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_dashboard_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldenAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Admin Dashboard", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "Founder & Educator Resource Management Desk", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                    }
                }

                if (isAuthenticated) {
                    Button(
                        onClick = { viewModel.adminLogout() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("admin_logout_btn")
                    ) {
                        Text("Lock / Logout", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (!isAuthenticated) {
            // LOCKED VIEW - SECURE AUTHENTICATION REQUIRED
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(DeepIndigoPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = DeepIndigoPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Admin Authentication Required",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "This area is restricted to VidyaSetu founders, faculty, and authorized content managers.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                        )

                        Button(
                            onClick = { showLoginDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_open_login_dialog_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Unlock with Master Password", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // AUTHENTICATED ADMIN DASHBOARD
            // Quick Analytics Strip
            Surface(
                color = DeepIndigoPrimary.copy(alpha = 0.06f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatPill("Lectures", "${lectures.size}", DeepIndigoPrimary)
                    StatPill("Notes/DPPs", "${notes.size}", BrightSaffron)
                    StatPill("Quizzes", "${quizzes.size}", EmeraldSuccess)
                    StatPill("Doubts", "${doubts.size}", GoldenAmber)
                    StatPill("Gifted Pro", "${giftedUsers.size}", GoldenAmber)
                }
            }

            // NAVIGATION TABS
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = DeepIndigoPrimary,
                edgePadding = 12.dp
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Upload Lecture", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Upload Notes", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Upload Quiz", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Doubts (${doubts.count { it.status != "RESOLVED" }})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    text = { Text("Announcements", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 5,
                    onClick = { selectedTab = 5 },
                    text = { Text("🎁 Pro Giveaway (${giftedUsers.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp), tint = GoldenAmber) }
                )
            }

            // TAB CONTENTS
            when (selectedTab) {
                0 -> UploadLectureSection(viewModel = viewModel)
                1 -> UploadNoteSection(viewModel = viewModel)
                2 -> UploadQuizSection(viewModel = viewModel)
                3 -> ResolveDoubtsSection(doubts = doubts, viewModel = viewModel)
                4 -> PostAnnouncementSection(viewModel = viewModel)
                5 -> OwnerProGiveawaySection(viewModel = viewModel, giftedUsers = giftedUsers, userProgress = userProgress)
            }
        }
    }
}


@Composable
private fun StatPill(label: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
fun UploadLectureSection(viewModel: VidyaSetuViewModel) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physics") }
    var grade by remember { mutableStateOf("Class 10 (Mission Board)") }
    var teacher by remember { mutableStateOf("Er. Alok Sharma (Founder)") }
    var durationText by remember { mutableStateOf("45") }
    var keyFormulas by remember { mutableStateOf("") }
    var aiSummary by remember { mutableStateOf("") }
    var isLiveNow by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Publish New Educational Lecture", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepIndigoPrimary)
            Text("Upload a pre-recorded lesson or start a live interactive classroom stream.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Lecture Title") },
                placeholder = { Text("e.g. Ray Optics: Mirror Formula & Ray Diagrams") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_lecture_title_input"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subject Selector Pills
            Text("Subject Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Physics", "Mathematics", "Chemistry", "English").forEach { sub ->
                    FilterChip(
                        selected = subject == sub,
                        onClick = { subject = sub },
                        label = { Text(sub, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepIndigoPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = grade,
                onValueChange = { grade = it },
                label = { Text("Target Batch / Class") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Faculty Name") },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it },
                    label = { Text("Mins") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = keyFormulas,
                onValueChange = { keyFormulas = it },
                label = { Text("Blackboard Key Formulas") },
                placeholder = { Text("1/f = 1/v + 1/u | m = -v/u") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = aiSummary,
                onValueChange = { aiSummary = it },
                label = { Text("AI Concept Digest / Overview") },
                placeholder = { Text("Key takeaways and revision notes for students...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Educational Format Notice
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Standard Educational Curriculum Lecture",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Optimized for ultra-low 144p data streaming, offline study downloads, and board syllabus notes.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.adminUploadLecture(
                            title = title.trim(),
                            subject = subject,
                            grade = grade,
                            teacher = teacher,
                            durationMinutes = durationText.toIntOrNull() ?: 45,
                            aiSummary = aiSummary.ifBlank { "Important board exam lecture covered by $teacher." },
                            keyFormulas = keyFormulas.ifBlank { "Standard formulas covered in lecture." },
                            isLiveNow = false
                        )
                        title = ""
                        keyFormulas = ""
                        aiSummary = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_publish_lecture_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publish Lecture to VidyaSetu App", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun UploadNoteSection(viewModel: VidyaSetuViewModel) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physics") }
    var grade by remember { mutableStateOf("Class 10") }
    var chapter by remember { mutableStateOf("Chapter 10: Light") }
    var pageCountText by remember { mutableStateOf("8") }
    var contentSummary by remember { mutableStateOf("") }
    var detailedMarkdown by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Publish Study Notes, DPP & Formula Sheets", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepIndigoPrimary)
            Text("Students can read online or download with 0 data usage.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Note / DPP Title") },
                placeholder = { Text("e.g. Light Reflection & Refraction Handwritten DPP") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_note_title_input"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = grade,
                    onValueChange = { grade = it },
                    label = { Text("Grade") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = chapter,
                    onValueChange = { chapter = it },
                    label = { Text("Chapter") },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = pageCountText,
                    onValueChange = { pageCountText = it },
                    label = { Text("Pages") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = contentSummary,
                onValueChange = { contentSummary = it },
                label = { Text("Brief Overview") },
                placeholder = { Text("Short summary shown on the card...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = detailedMarkdown,
                onValueChange = { detailedMarkdown = it },
                label = { Text("Detailed Notes Content (Markdown / Text)") },
                placeholder = { Text("# Chapter Notes\n- Concept 1\n- Formula: E = mc²\n- Practice problems...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                minLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.adminUploadNote(
                            title = title,
                            subject = subject,
                            grade = grade,
                            chapter = chapter,
                            pageCount = pageCountText.toIntOrNull() ?: 6,
                            contentSummary = contentSummary.ifBlank { "Handwritten revision notes for $chapter." },
                            detailedNotesMarkdown = detailedMarkdown.ifBlank {
                                "# $title\n\nChapter: $chapter\nSubject: $subject\n\n- Key definitions and core principles.\n- Comprehensive numerical practice questions with step-by-step solutions."
                            }
                        )
                        title = ""
                        contentSummary = ""
                        detailedMarkdown = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_publish_note_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = BrightSaffron),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Book, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Notes to Student Library", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun UploadQuizSection(viewModel: VidyaSetuViewModel) {
    var quizTitle by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physics") }
    var grade by remember { mutableStateOf("Class 10") }
    var durationMinutesText by remember { mutableStateOf("10") }

    var questionText by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctIndex by remember { mutableIntStateOf(0) }
    var explanation by remember { mutableStateOf("") }

    val questionList = remember { mutableListOf<QuizQuestion>() }
    var questionsCount by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Create Chapter Practice Quiz / MCQ Test", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepIndigoPrimary)
            Text("Create quizzes with timer, multiple choices, and instant answer explanations.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = quizTitle,
                onValueChange = { quizTitle = it },
                label = { Text("Quiz Title") },
                placeholder = { Text("e.g. Periodic Classification Speed Test") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_quiz_title_input"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = durationMinutesText,
                    onValueChange = { durationMinutesText = it },
                    label = { Text("Duration (Mins)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Questions Sub-Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, DeepIndigoPrimary.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Add Question (${questionsCount} added)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepIndigoPrimary)
                        if (questionsCount > 0) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = EmeraldSuccess.copy(alpha = 0.15f)
                            ) {
                                Text("$questionsCount in queue", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = questionText,
                        onValueChange = { questionText = it },
                        label = { Text("Question Statement") },
                        placeholder = { Text("Type question here...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = optionA,
                        onValueChange = { optionA = it },
                        label = { Text("Option A") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = optionB,
                        onValueChange = { optionB = it },
                        label = { Text("Option B") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = optionC,
                        onValueChange = { optionC = it },
                        label = { Text("Option C") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = optionD,
                        onValueChange = { optionD = it },
                        label = { Text("Option D") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Correct Option:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("A", "B", "C", "D").forEachIndexed { idx, optLabel ->
                            FilterChip(
                                selected = correctIndex == idx,
                                onClick = { correctIndex = idx },
                                label = { Text("Option $optLabel", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldSuccess,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = explanation,
                        onValueChange = { explanation = it },
                        label = { Text("Solution / Explanation") },
                        placeholder = { Text("Why is this answer correct?") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (questionText.isNotBlank() && optionA.isNotBlank() && optionB.isNotBlank()) {
                                val q = QuizQuestion(
                                    id = questionsCount + 1,
                                    question = questionText,
                                    options = listOf(optionA, optionB, optionC.ifBlank { "None of these" }, optionD.ifBlank { "All of the above" }),
                                    correctOptionIndex = correctIndex,
                                    explanation = explanation.ifBlank { "Correct formula and concept applied." }
                                )
                                questionList.add(q)
                                questionsCount = questionList.size
                                questionText = ""
                                optionA = ""
                                optionB = ""
                                optionC = ""
                                optionD = ""
                                explanation = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Add Question to Quiz")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (quizTitle.isNotBlank()) {
                        val finalQuestions = if (questionList.isNotEmpty()) {
                            questionList.toList()
                        } else {
                            listOf(
                                QuizQuestion(
                                    id = 1,
                                    question = "Sample Question for $quizTitle",
                                    options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
                                    correctOptionIndex = 0,
                                    explanation = "Standard theoretical deduction from NCERT textbook."
                                )
                            )
                        }

                        viewModel.adminUploadQuiz(
                            title = quizTitle,
                            subject = subject,
                            grade = grade,
                            durationMinutes = durationMinutesText.toIntOrNull() ?: 10,
                            questions = finalQuestions
                        )
                        quizTitle = ""
                        questionList.clear()
                        questionsCount = 0
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_publish_quiz_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publish Quiz for Students", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ResolveDoubtsSection(doubts: List<DoubtEntity>, viewModel: VidyaSetuViewModel) {
    var teacherResponseText by remember { mutableStateOf("") }
    var selectedDoubtId by remember { mutableStateOf<Long?>(null) }
    var teacherName by remember { mutableStateOf("Alok Sir (Founder)") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Student Doubts Queue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepIndigoPrimary)
            Text("Guaranteed 24-hr resolution desk for all VidyaSetu students.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (doubts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No doubts pending! Great job!", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }

        items(doubts) { doubt ->
            val isSelected = selectedDoubtId == doubt.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(
                    1.dp,
                    if (doubt.status == "RESOLVED") EmeraldSuccess.copy(alpha = 0.4f) else GoldenAmber.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${doubt.studentName} (${doubt.subject})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DeepIndigoPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (doubt.status == "RESOLVED") EmeraldSuccess else GoldenAmber
                        ) {
                            Text(
                                text = doubt.status,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "❓ ${doubt.question}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (doubt.aiInstantAnswer.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = "🤖 AI Preliminary: ${doubt.aiInstantAnswer}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                maxLines = 2,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }

                    if (doubt.status == "RESOLVED") {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "👨‍🏫 Teacher Answer (${doubt.teacherName}):\n${doubt.teacherAnswer}",
                            fontSize = 12.sp,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Spacer(modifier = Modifier.height(10.dp))
                        if (isSelected) {
                            OutlinedTextField(
                                value = teacherName,
                                onValueChange = { teacherName = it },
                                label = { Text("Teacher Name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(6.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = teacherResponseText,
                                onValueChange = { teacherResponseText = it },
                                label = { Text("Write Teacher Explanation / Solution") },
                                placeholder = { Text("Explain step-by-step formula or concept...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_teacher_response_input"),
                                shape = RoundedCornerShape(8.dp),
                                minLines = 3
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { selectedDoubtId = null },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (teacherResponseText.isNotBlank()) {
                                            viewModel.adminResolveDoubt(doubt.id, teacherResponseText, teacherName)
                                            teacherResponseText = ""
                                            selectedDoubtId = null
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.testTag("admin_submit_resolution_btn")
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Send Teacher Solution", fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Button(
                                onClick = {
                                    selectedDoubtId = doubt.id
                                    teacherResponseText = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.testTag("admin_open_resolve_doubt_${doubt.id}")
                            ) {
                                Text("Resolve Doubt as Educator", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostAnnouncementSection(viewModel: VidyaSetuViewModel) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Broadcast Notice Board Announcement", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepIndigoPrimary)
            Text("Broadcast live updates, batch timetables, and board notices to all student devices.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Announcement Headline") },
                placeholder = { Text("e.g. 🔴 Special Live Physics Marathon Tomorrow at 10 AM") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_announcement_title_input"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Notice Details / Instructions") },
                placeholder = { Text("Enter detailed instruction, syllabus coverage, or links...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_announcement_msg_input"),
                shape = RoundedCornerShape(8.dp),
                minLines = 4
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Important / High Priority Notice", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Highlighted with an alert banner on the Batches feed.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    }
                    Switch(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = BrightSaffron)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && message.isNotBlank()) {
                        viewModel.adminPublishAnnouncement(title, message, isUrgent)
                        title = ""
                        message = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_broadcast_announcement_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = BrightSaffron),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Broadcast to All Students", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun OwnerProGiveawaySection(
    viewModel: VidyaSetuViewModel,
    giftedUsers: List<GiftedProUserEntity>,
    userProgress: UserProgressEntity?
) {
    var googleUsername by remember { mutableStateOf("") }
    var studentName by remember { mutableStateOf("") }
    var scholarshipReason by remember { mutableStateOf("Rural / Underprivileged Student 100% Free Scholarship") }
    val reasons = listOf(
        "Rural / Underprivileged Student 100% Free Scholarship",
        "Financial Hardship / Free Education Support",
        "Merit Scholarship - High Quiz & Project Performer",
        "Community Educational Giveaway Winner"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GoldenAmber.copy(alpha = 0.15f)),
                border = BorderStroke(1.5.dp, GoldenAmber)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(GoldenAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Owner Pro Giveaway Desk", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DeepIndigoPrimary)
                        Text(
                            "As owner/founder, give away lifetime VidyaSetu Pro passes for 100% free to poor or deserving students using their Google username or email.",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Quick 1-click gift for currently logged-in student
        if (userProgress != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Current Active Student Account:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text("${userProgress.userName} (${userProgress.googleUsername})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                if (userProgress.isGiftedByOwner) "Status: Already Gifted by Owner 👑" else if (userProgress.isPro) "Status: Pro Active ⭐" else "Status: Free Tier (Eligible for Giveaway)",
                                fontSize = 11.sp,
                                color = if (userProgress.isGiftedByOwner) GoldenAmber else if (userProgress.isPro) EmeraldSuccess else Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (!userProgress.isGiftedByOwner) {
                            Button(
                                onClick = {
                                    viewModel.adminGiftProToUser(
                                        userProgress.googleUsername,
                                        userProgress.userName,
                                        "Direct Owner Giveaway to Current Student"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenAmber),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("quick_gift_current_user_btn")
                            ) {
                                Text("Gift Pro Now", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Grant Giveaway Form Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Grant Free Pro to Student (Google Username)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepIndigoPrimary)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = googleUsername,
                        onValueChange = { googleUsername = it },
                        label = { Text("Google Username or Email (e.g. @rahul_maths or rahul@gmail.com)") },
                        modifier = Modifier.fillMaxWidth().testTag("giveaway_username_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name (Optional, e.g. Rahul Verma)") },
                        modifier = Modifier.fillMaxWidth().testTag("giveaway_student_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Scholarship / Giveaway Reason:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    reasons.forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { scholarshipReason = r }
                                .padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (scholarshipReason == r) GoldenAmber else Color.LightGray.copy(alpha = 0.5f))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(r, fontSize = 12.sp, color = if (scholarshipReason == r) DeepIndigoPrimary else MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (googleUsername.isNotBlank()) {
                                viewModel.adminGiftProToUser(
                                    googleUsername = googleUsername.trim(),
                                    studentName = studentName.trim(),
                                    reason = scholarshipReason
                                )
                                googleUsername = ""
                                studentName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("grant_giveaway_submit_btn")
                    ) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = GoldenAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Grant 100% Free Lifetime Pro Access", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Ledger of Gifted Users
        item {
            Text("🎁 Sponsored & Gifted Students Ledger (${giftedUsers.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
        }

        if (giftedUsers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No gifted students yet. Use form above to giveaway Pro passes!", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        } else {
            items(giftedUsers) { student ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("gifted_user_card_${student.googleUsername}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, GoldenAmber.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(student.studentName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = GoldenAmber.copy(alpha = 0.15f)) {
                                    Text("LIFETIME PRO", color = GoldenAmber, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Text("Google User: ${student.googleUsername}", fontSize = 12.sp, color = DeepIndigoPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Reason: ${student.scholarshipReason}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text("Granted: ${student.giftedDate} by ${student.grantedBy}", fontSize = 10.sp, color = Color.Gray)
                        }

                        IconButton(
                            onClick = { viewModel.adminRevokeGiftedPro(student.googleUsername) },
                            modifier = Modifier.testTag("revoke_pro_${student.googleUsername}")
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Revoke Pro", tint = Color.Red.copy(alpha = 0.7f))
                        }
                    }
                }
            }
        }
    }
}

