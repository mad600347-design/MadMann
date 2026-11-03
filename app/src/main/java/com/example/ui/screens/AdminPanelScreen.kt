package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.viewmodel.VidyaSetuViewModel

/**
 * AdminPanelScreen redirects to the enhanced AdminDashboardScreen
 * which provides full educational resource uploading (lectures, notes/DPPs,
 * quizzes, doubt resolution, and announcements) while keeping authentication
 * secure via AdminLoginDialog without leaking passwords.
 */
@Composable
fun AdminPanelScreen(
    viewModel: VidyaSetuViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AdminDashboardScreen(
        viewModel = viewModel,
        onBack = onBack,
        modifier = modifier
    )
}
