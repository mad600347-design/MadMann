package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import com.example.data.local.UserProgressEntity
import com.example.ui.theme.BrightSaffron
import com.example.ui.theme.DeepIndigoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenAmber

@Composable
fun GoogleSignInDialog(
    userProgress: UserProgressEntity?,
    onDismiss: () -> Unit,
    onSignIn: (username: String, email: String, name: String, grade: String) -> Unit,
    onSignOut: () -> Unit,
    onCheckGift: (usernameOrEmail: String) -> Unit
) {
    val isSignedIn = userProgress?.isGoogleSignedIn == true
    var isEditing by remember { mutableStateOf(!isSignedIn) }

    var username by remember { mutableStateOf(userProgress?.googleUsername?.removePrefix("@") ?: "student_bharat") }
    var email by remember { mutableStateOf(userProgress?.googleEmail ?: "student.bharat@gmail.com") }
    var displayName by remember { mutableStateOf(userProgress?.userName ?: "Suresh Kumar") }
    var grade by remember { mutableStateOf(userProgress?.userGrade ?: "Class 10 (Board Mission)") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("google_signin_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4285F4).copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "G",
                                    color = Color(0xFF4285F4),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Google Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "VidyaSetu Student Identity",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isSignedIn && !isEditing) {
                    // Current Signed-In Profile View
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(DeepIndigoPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (userProgress?.userName?.take(1) ?: "S").uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = userProgress?.userName ?: "Suresh Kumar",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified Google Account",
                                        tint = Color(0xFF4285F4),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = userProgress?.googleUsername ?: "@student",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DeepIndigoPrimary
                                )
                                Text(
                                    text = userProgress?.googleEmail ?: "student@gmail.com",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pro & Giveaway Status Card
                    if (userProgress?.isPro == true) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (userProgress.isGiftedByOwner) GoldenAmber.copy(alpha = 0.15f) else EmeraldSuccess.copy(alpha = 0.12f)
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (userProgress.isGiftedByOwner) GoldenAmber else EmeraldSuccess
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (userProgress.isGiftedByOwner) Icons.Default.CardGiftcard else Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = if (userProgress.isGiftedByOwner) Color(0xFFB78103) else EmeraldSuccess,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (userProgress.isGiftedByOwner) "👑 VIDYASETU PRO - 100% FREE GIFT" else "⭐ PRO MEMBER ACTIVE",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = if (userProgress.isGiftedByOwner) Color(0xFFB78103) else EmeraldSuccess
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (userProgress.isGiftedByOwner) {
                                        "Granted by Owner: ${userProgress.giftNote.ifBlank { "Educational Giveaway Initiative" }} • Lifetime 100% Free Access!"
                                    } else {
                                        "Plan: ${userProgress.proPlan} • Valid until: ${userProgress.proExpiryDate}"
                                    },
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    } else {
                        // Free Tier with Owner Giveaway Check Button
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.School, contentDescription = null, tint = DeepIndigoPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Free Student Tier", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Standard access to all lectures and study notes. Did the founder/owner grant you a free Pro sponsorship?",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        onCheckGift(userProgress?.googleUsername ?: "")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrightSaffron),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("claim_owner_gift_btn")
                                ) {
                                    Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Check Owner Pro Giveaway Status", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isEditing = true },
                            modifier = Modifier.weight(1f).testTag("switch_google_account_btn"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Switch Account", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                onSignOut()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.85f)),
                            modifier = Modifier.weight(1f).testTag("signout_google_btn"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sign Out", fontSize = 12.sp, color = Color.White)
                        }
                    }
                } else {
                    // Sign In / Edit Form
                    Text(
                        text = "Enter your Google credentials to sync progress, receive owner Pro giveaways, and study uninterrupted.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Google Username (e.g. @rahul_maths)") },
                        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = DeepIndigoPrimary) },
                        modifier = Modifier.fillMaxWidth().testTag("google_username_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Google Email (e.g. rahul@gmail.com)") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = DeepIndigoPrimary) },
                        modifier = Modifier.fillMaxWidth().testTag("google_email_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth().testTag("google_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = grade,
                        onValueChange = { grade = it },
                        label = { Text("Class / Academic Target") },
                        modifier = Modifier.fillMaxWidth().testTag("google_grade_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val cleanUser = if (username.startsWith("@")) username.trim() else "@${username.trim()}"
                            onSignIn(cleanUser, email.trim(), displayName.trim(), grade.trim())
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("submit_google_signin_btn")
                    ) {
                        Text(
                            text = "Sign in with Google Username",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    if (isSignedIn) {
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = { isEditing = false },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancel", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
