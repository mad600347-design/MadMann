package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdminLoginDialog
import com.example.ui.components.GoogleSignInDialog
import com.example.ui.components.TopEducationalBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.BatchesHomeScreen
import com.example.ui.screens.DoubtsAndAiGuruScreen
import com.example.ui.screens.EducationalProjectDetailScreen
import com.example.ui.screens.EducationalProjectsScreen
import com.example.ui.screens.LeaderboardAndBadgesScreen
import com.example.ui.screens.LecturePlayerScreen
import com.example.ui.screens.NoteDetailScreen
import com.example.ui.screens.OfflineDownloadsScreen
import com.example.ui.screens.PracticeQuizScreen
import com.example.ui.screens.ProSubscriptionScreen
import com.example.ui.theme.BrightSaffron
import com.example.ui.theme.DeepIndigoPrimary
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaSetuViewModel


class MainActivity : ComponentActivity() {
    private val viewModel: VidyaSetuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                VidyaSetuMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun VidyaSetuMainApp(viewModel: VidyaSetuViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var showGoogleSignInDialog by remember { mutableStateOf(false) }

    if (showAdminLoginDialog) {
        AdminLoginDialog(
            onDismiss = { showAdminLoginDialog = false },
            onSuccess = {
                viewModel.verifyAdminPassword("VidyaSetu123")
                showAdminLoginDialog = false
                viewModel.navigateTo(Screen.AdminDashboard)
            }
        )
    }

    if (showGoogleSignInDialog) {
        GoogleSignInDialog(
            userProgress = userProgress,
            onDismiss = { showGoogleSignInDialog = false },
            onSignIn = { username, email, name, grade ->
                viewModel.signInWithGoogle(username, email, name, grade)
            },
            onSignOut = {
                viewModel.signOutGoogle()
            },
            onCheckGift = { usernameOrEmail ->
                viewModel.checkAndClaimOwnerGift(usernameOrEmail)
            }
        )
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button for sub-screens
    BackHandler(enabled = currentScreen !is Screen.BatchesHome) {
        if (!viewModel.navigateBack()) {
            viewModel.navigateTo(Screen.BatchesHome)
        }
    }

    val isTopLevelScreen = currentScreen is Screen.BatchesHome ||
            currentScreen is Screen.DoubtsAndAiGuru ||
            currentScreen is Screen.OfflineDownloads ||
            currentScreen is Screen.LeaderboardAndBadges ||
            currentScreen is Screen.ProSubscription

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (isTopLevelScreen) {
                TopEducationalBar(
                    userProgress = userProgress,
                    onPointsClick = { viewModel.navigateTo(Screen.LeaderboardAndBadges) },
                    onProClick = { viewModel.navigateTo(Screen.ProSubscription) },
                    onProfileClick = { showGoogleSignInDialog = true },
                    onAdminClick = {
                        if (isAdminAuthenticated) {
                            viewModel.navigateTo(Screen.AdminDashboard)
                        } else {
                            showAdminLoginDialog = true
                        }
                    }
                )
            }
        },

        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen is Screen.BatchesHome,
                        onClick = { viewModel.navigateTo(Screen.BatchesHome) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Batches") },
                        label = { Text("Batches", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepIndigoPrimary,
                            selectedTextColor = DeepIndigoPrimary,
                            indicatorColor = DeepIndigoPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_batches")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.DoubtsAndAiGuru,
                        onClick = { viewModel.navigateTo(Screen.DoubtsAndAiGuru) },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Guru") },
                        label = { Text("AI Guru", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrightSaffron,
                            selectedTextColor = BrightSaffron,
                            indicatorColor = BrightSaffron.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_ai_guru")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.OfflineDownloads,
                        onClick = { viewModel.navigateTo(Screen.OfflineDownloads) },
                        icon = { Icon(Icons.Default.CloudDownload, contentDescription = "Offline Vault") },
                        label = { Text("Offline", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepIndigoPrimary,
                            selectedTextColor = DeepIndigoPrimary,
                            indicatorColor = DeepIndigoPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_offline")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.LeaderboardAndBadges,
                        onClick = { viewModel.navigateTo(Screen.LeaderboardAndBadges) },
                        icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard") },
                        label = { Text("Rankings", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldenAmber,
                            selectedTextColor = GoldenAmber,
                            indicatorColor = GoldenAmber.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_rankings")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.ProSubscription,
                        onClick = { viewModel.navigateTo(Screen.ProSubscription) },
                        icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = "Pro Pass") },
                        label = { Text("Pro ₹9", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrightSaffron,
                            selectedTextColor = BrightSaffron,
                            indicatorColor = BrightSaffron.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_pro")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val screen = currentScreen) {
                is Screen.BatchesHome -> BatchesHomeScreen(viewModel = viewModel)
                is Screen.LecturePlayer -> LecturePlayerScreen(
                    lectureId = screen.lectureId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.DoubtsAndAiGuru -> DoubtsAndAiGuruScreen(viewModel = viewModel)
                is Screen.OfflineDownloads -> OfflineDownloadsScreen(viewModel = viewModel)
                is Screen.LeaderboardAndBadges -> LeaderboardAndBadgesScreen(viewModel = viewModel)
                is Screen.ProSubscription -> ProSubscriptionScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.NoteDetail -> NoteDetailScreen(
                    noteId = screen.noteId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.AdminPanel -> AdminDashboardScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.AdminDashboard -> AdminDashboardScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.PracticeQuizMode -> PracticeQuizScreen(
                    quizId = screen.quizId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.EducationalProjects -> EducationalProjectsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.ProjectDetail -> EducationalProjectDetailScreen(
                    projectId = screen.projectId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
            }
        }
    }
}
