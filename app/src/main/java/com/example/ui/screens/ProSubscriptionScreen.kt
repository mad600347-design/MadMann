package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolunteerActivism
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProPlan
import com.example.ui.components.BharatUpiQrCard
import com.example.ui.theme.BrightSaffron
import com.example.ui.theme.DeepIndigoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenAmber
import com.example.ui.viewmodel.VidyaSetuViewModel

@Composable
fun ProSubscriptionScreen(
    viewModel: VidyaSetuViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProgress by viewModel.userProgress.collectAsState()
    val configuredUpiId by viewModel.configuredUpiId.collectAsState()
    val configuredMerchantName by viewModel.configuredMerchantName.collectAsState()
    var selectedPlan by remember { mutableStateOf(ProPlan.ONE_MONTH) }
    var transactionRef by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var claimUsernameInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP HEADER
        item {
            Surface(
                color = DeepIndigoPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("pro_screen_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "VidyaSetu Pro & QR Support",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Sabhi Bachho Ka Haq — High Quality Education at Affordable ₹9",
                        color = GoldenAmber,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "While big coachings charge ₹30,000 to ₹80,000, VidyaSetu provides everything free and offers 24-hr personal teacher doubt resolution at just ₹9 so no child from a low-income family is left behind.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Active Status
        if (userProgress?.isPro == true) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("pro_active_status_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (userProgress?.isGiftedByOwner == true) GoldenAmber.copy(alpha = 0.15f) else EmeraldSuccess.copy(alpha = 0.12f)
                    ),
                    border = BorderStroke(1.5.dp, if (userProgress?.isGiftedByOwner == true) GoldenAmber else EmeraldSuccess)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (userProgress?.isGiftedByOwner == true) Icons.Default.CardGiftcard else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (userProgress?.isGiftedByOwner == true) Color(0xFFB78103) else EmeraldSuccess,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (userProgress?.isGiftedByOwner == true) "👑 VIDYASETU PRO - 100% FREE GIFT" else "Pro Membership Active! (${userProgress?.proPlan})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (userProgress?.isGiftedByOwner == true) Color(0xFFB78103) else EmeraldSuccess
                            )
                            Text(
                                text = if (userProgress?.isGiftedByOwner == true) {
                                    "Gifted by Owner: ${userProgress?.giftNote} • Google: ${userProgress?.googleUsername} • Lifetime Free Access"
                                } else {
                                    "Valid until: ${userProgress?.proExpiryDate} • 24hr Doubt Desk Unlocked"
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        } else {
            // Free Tier - Owner Free Pro Giveaway Desk Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("claim_owner_giveaway_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, GoldenAmber)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GoldenAmber.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, tint = GoldenAmber, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("🎁 Owner Free Pro Giveaway", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepIndigoPrimary)
                                Text("Did the owner gift free Pro to your Google username?", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = claimUsernameInput,
                            onValueChange = { claimUsernameInput = it },
                            label = { Text("Your Google Username or Email") },
                            placeholder = { Text(userProgress?.googleUsername ?: "@student") },
                            modifier = Modifier.fillMaxWidth().testTag("claim_username_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val target = if (claimUsernameInput.isNotBlank()) claimUsernameInput.trim() else (userProgress?.googleUsername ?: "")
                                viewModel.checkAndClaimOwnerGift(target)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldenAmber),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("claim_giveaway_btn")
                        ) {
                            Text("Check & Activate Free Pro Pass", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }


        // Plan Selection Cards
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Select Subsidized Plan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlanSelectionCard(
                        plan = ProPlan.ONE_MONTH,
                        isSelected = selectedPlan == ProPlan.ONE_MONTH,
                        onClick = { selectedPlan = ProPlan.ONE_MONTH },
                        modifier = Modifier.weight(1f)
                    )

                    PlanSelectionCard(
                        plan = ProPlan.SIX_MONTHS,
                        isSelected = selectedPlan == ProPlan.SIX_MONTHS,
                        onClick = { selectedPlan = ProPlan.SIX_MONTHS },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 100% Free Scholarship Option for Needy Students
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPlan = ProPlan.SCHOLARSHIP }
                        .border(
                            width = if (selectedPlan == ProPlan.SCHOLARSHIP) 2.dp else 1.dp,
                            color = if (selectedPlan == ProPlan.SCHOLARSHIP) GoldenAmber else Color.LightGray.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, tint = BrightSaffron, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Shiksha Samman (100% Free Scholarship)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Cannot afford ₹9? We sponsor you for 100% free with same 24hr doubt features.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }
                        Text(text = "₹0 Free", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }
                }
            }
        }

        // PROPER UPI QR CODE CONTAINER
        item {
            BharatUpiQrCard(
                selectedPlan = selectedPlan,
                upiId = configuredUpiId,
                merchantName = configuredMerchantName,
                onProActivated = { plan, utr ->
                    viewModel.activatePro(plan, utr)
                    isSubmitted = true
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Features Checklist
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Everything Included in VidyaSetu Pro:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProFeatureRow("Human Master Teacher Doubt Response under 24 hours guaranteed")
                    ProFeatureRow("Unlimited AI Guru (विद्यामित्र) Instant Study Chatbot")
                    ProFeatureRow("Download full HD/144p videos for 100% offline study")
                    ProFeatureRow("High quality handwritten formula books & NCERT mind maps")
                    ProFeatureRow("In-video live chat doubt priority in all live streams")
                }
            }
        }

        // 24x7 Student Helpline & Email Support Card
        item {
            val context = LocalContext.current
            var emailCopied by remember { mutableStateOf(false) }
            val supportEmail = "vidyasetu.support01@gmail.com"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("pro_support_helpdesk_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DeepIndigoPrimary.copy(alpha = 0.06f)),
                border = BorderStroke(1.dp, DeepIndigoPrimary.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DeepIndigoPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = "Support",
                                tint = GoldenAmber,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "24x7 Student Support Desk",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DeepIndigoPrimary
                            )
                            Text(
                                text = "Payment queries, scholarships & tech support",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Having questions about your UPI payment, need manual UTR verification, or applying for the free Shiksha Samman Scholarship? Reach our student support team directly:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, DeepIndigoPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = DeepIndigoPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = supportEmail,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = DeepIndigoPrimary
                                )
                            }

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    clipboard?.setPrimaryClip(ClipData.newPlainText("Support Email", supportEmail))
                                    emailCopied = true
                                },
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("copy_support_email_btn")
                            ) {
                                Icon(
                                    imageVector = if (emailCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Copy Email",
                                    tint = if (emailCopied) EmeraldSuccess else DeepIndigoPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            try {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$supportEmail")
                                    putExtra(Intent.EXTRA_SUBJECT, "VidyaSetu Support: ${selectedPlan.title}")
                                    putExtra(Intent.EXTRA_TEXT, "Hello VidyaSetu Support Team,\n\nI need help regarding:\n- Student Name:\n- Plan: ${selectedPlan.title}\n- Query / Details:\n\nThank you!")
                                }
                                context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
                            } catch (e: Exception) {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                clipboard?.setPrimaryClip(ClipData.newPlainText("Support Email", supportEmail))
                                emailCopied = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("send_support_email_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Email Support (vidyasetu.support01@gmail.com)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PlanSelectionCard(
    plan: ProPlan,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) DeepIndigoPrimary else Color.LightGray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DeepIndigoPrimary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (plan.isPopular) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BrightSaffron)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("POPULAR", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = plan.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "₹${plan.priceInr}",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepIndigoPrimary
            )

            Text(
                text = plan.savingsText,
                fontSize = 10.sp,
                color = EmeraldSuccess,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProFeatureRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = EmeraldSuccess,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            lineHeight = 16.sp
        )
    }
}
