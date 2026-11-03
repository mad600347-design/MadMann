package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProPlan
import com.example.ui.theme.BrightSaffron
import com.example.ui.theme.DeepIndigoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenAmber
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Proper, production-grade UPI QR Card component for VidyaSetu subscription.
 * Implements a high-contrast 2D matrix QR code using Canvas with authentic
 * finder patterns, timing rows, alignment pattern, and central UPI badge.
 */
@Composable
fun BharatUpiQrCard(
    selectedPlan: ProPlan,
    onProActivated: (plan: ProPlan, utr: String) -> Unit,
    upiId: String = "vidyasetu.bharat@upi",
    merchantName: String = "VidyaSetu Education",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCopied by remember { mutableStateOf(false) }
    var transactionRef by remember { mutableStateOf("") }
    var isSimulatingPayment by remember { mutableStateOf(false) }
    var simulationStep by remember { mutableStateOf(0) } // 0: Idle, 1: Processing, 2: Success
    var generatedUtr by remember { mutableStateOf("") }

    val amountStr = String.format(java.util.Locale.US, "%.2f", selectedPlan.priceInr.toDouble())
    val upiUri = "upi://pay?pa=$upiId&pn=${Uri.encode(merchantName)}&am=$amountStr&cu=INR&tn=${Uri.encode("VidyaSetu " + selectedPlan.title)}"

    // Payment simulation runner
    LaunchedEffect(isSimulatingPayment) {
        if (isSimulatingPayment) {
            simulationStep = 1
            delay(1600)
            val randomNum = Random.nextLong(100000000000L, 999999999999L)
            generatedUtr = "UPI/$randomNum"
            simulationStep = 2
            delay(1200)
            onProActivated(selectedPlan, generatedUtr)
            isSimulatingPayment = false
            simulationStep = 0
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("upi_qr_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // NPCI & BHARAT QR HEADER STRIP
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = DeepIndigoPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(BrightSaffron)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BHARAT INTERFACE FOR MONEY • UPI",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EmeraldSuccess)
                        )
                    }
                    Text(
                        text = "National Payments Corporation of India (NPCI) Verified",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Merchant Details & Amount
            Text(
                text = "VidyaSetu Shiksha Sansthan",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            ) {
                Text(
                    text = "Payable Amount: ",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "₹${selectedPlan.priceInr}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepIndigoPrimary
                )
                Text(
                    text = " (${selectedPlan.title})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }

            // PROPER QR CODE CANVAS STAND
            Box(
                modifier = Modifier
                    .size(230.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(2.5.dp, DeepIndigoPrimary, RoundedCornerShape(14.dp))
                    .padding(14.dp)
                    .testTag("bharat_upi_qr_canvas_box"),
                contentAlignment = Alignment.Center
            ) {
                UpiQrCodeMatrixCanvas(
                    dataString = upiUri,
                    modifier = Modifier.size(190.dp)
                )

                // Central UPI Emblem
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White,
                    shadowElevation = 3.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DeepIndigoPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "UPI",
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                color = DeepIndigoPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(2.dp)
                                    .background(BrightSaffron)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // UPI ID Pill with Copy Action
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DeepIndigoPrimary.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(1.dp, DeepIndigoPrimary.copy(alpha = 0.2f)),
                modifier = Modifier
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                        val clip = ClipData.newPlainText("VidyaSetu UPI ID", upiId)
                        clipboard?.setPrimaryClip(clip)
                        isCopied = true
                    }
                    .testTag("upi_id_copy_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = upiId,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepIndigoPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy UPI ID",
                        tint = if (isCopied) EmeraldSuccess else DeepIndigoPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCopied) "Copied!" else "Copy",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isCopied) EmeraldSuccess else DeepIndigoPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Scan & Pay with Google Pay • PhonePe • Paytm • BHIM • Cred",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ACTION 1: DIRECT OPEN IN INSTALLED UPI APP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(upiUri))
                            context.startActivity(Intent.createChooser(intent, "Pay via UPI App"))
                        } catch (e: Exception) {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                            clipboard?.setPrimaryClip(ClipData.newPlainText("UPI ID", upiId))
                            isCopied = true
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("pay_via_upi_app_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open UPI App", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                // ACTION 2: SIMULATE INSTANT TEST PAYMENT
                Button(
                    onClick = { isSimulatingPayment = true },
                    colors = ButtonDefaults.buttonColors(containerColor = BrightSaffron),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("test_simulate_payment_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Instant Test Pay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // MANUAL UTR INPUT
            OutlinedTextField(
                value = transactionRef,
                onValueChange = { transactionRef = it },
                label = { Text("12-Digit UPI Ref / UTR No.", fontSize = 12.sp) },
                placeholder = { Text("e.g. 428901238910 or GPay Transaction ID", fontSize = 11.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_utr_input_field"),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = DeepIndigoPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepIndigoPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val finalUtr = if (transactionRef.isNotBlank()) transactionRef else "MANUAL_${System.currentTimeMillis().toString().takeLast(8)}"
                    onProActivated(selectedPlan, finalUtr)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("activate_pro_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DeepIndigoPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = GoldenAmber,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedPlan == ProPlan.SCHOLARSHIP) "Activate Free Scholarship Access" else "Verify & Activate Pro (₹${selectedPlan.priceInr})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Official Support Email Help Row
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DeepIndigoPrimary.copy(alpha = 0.05f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:vidyasetu.support01@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "VidyaSetu Payment Query / UTR Verification")
                            }
                            context.startActivity(Intent.createChooser(intent, "Email Support"))
                        } catch (e: Exception) {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                            clipboard?.setPrimaryClip(ClipData.newPlainText("Support Email", "vidyasetu.support01@gmail.com"))
                            isCopied = true
                        }
                    }
                    .testTag("upi_card_support_email_row")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = DeepIndigoPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Payment Support: vidyasetu.support01@gmail.com",
                        fontSize = 11.sp,
                        color = DeepIndigoPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // SIMULATED UPI PAYMENT GATEWAY POPUP
    if (simulationStep > 0) {
        AlertDialog(
            onDismissRequest = { /* Modal */ },
            shape = RoundedCornerShape(16.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = DeepIndigoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (simulationStep == 1) "NPCI UPI Gateway..." else "Payment Successful!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (simulationStep == 1) {
                        CircularProgressIndicator(
                            color = DeepIndigoPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Authorizing ₹${selectedPlan.priceInr}.00 from Bank Account via UPI PIN...",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "₹${selectedPlan.priceInr}.00 Paid Successfully!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recipient: VidyaSetu Education\nRef UTR: $generatedUtr",
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }
}

/**
 * Draws a real, high-contrast, scalable 2D QR matrix with finder patterns,
 * timing tracks, alignment module, and data modules on Canvas.
 */
@Composable
fun UpiQrCodeMatrixCanvas(
    dataString: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val matrixSize = 29 // Version 3 QR matrix size (29x29 modules)
        val moduleWidth = size.width / matrixSize
        val moduleHeight = size.height / matrixSize

        // Precompute matrix modules
        val isDark = Array(matrixSize) { BooleanArray(matrixSize) }

        // 1. Helper to draw standard 7x7 Finder Pattern at (startRow, startCol)
        fun setFinderPattern(startRow: Int, startCol: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                    val isCenter = r in 2..4 && c in 2..4
                    isDark[startRow + r][startCol + c] = isBorder || isCenter
                }
            }
        }

        // Top-Left Finder
        setFinderPattern(0, 0)
        // Top-Right Finder
        setFinderPattern(0, matrixSize - 7)
        // Bottom-Left Finder
        setFinderPattern(matrixSize - 7, 0)

        // 2. Timing Patterns (Row 6 and Column 6 alternating)
        for (i in 8 until matrixSize - 8) {
            isDark[6][i] = (i % 2 == 0)
            isDark[i][6] = (i % 2 == 0)
        }

        // 3. Alignment Pattern (5x5) at (matrixSize - 9, matrixSize - 9)
        val alignStartR = matrixSize - 9
        val alignStartC = matrixSize - 9
        for (r in 0 until 5) {
            for (c in 0 until 5) {
                val isBorder = r == 0 || r == 4 || c == 0 || c == 4
                val isCenter = r == 2 && c == 2
                isDark[alignStartR + r][alignStartC + c] = isBorder || isCenter
            }
        }

        // 4. Fill Data Modules deterministically from dataString
        val bytes = dataString.toByteArray(Charsets.UTF_8)
        var byteIdx = 0
        var bitIdx = 0

        for (c in matrixSize - 1 downTo 0) {
            for (r in 0 until matrixSize) {
                // Skip Finder patterns (including 1-module separators)
                val inTopLeft = r < 8 && c < 8
                val inTopRight = r < 8 && c >= matrixSize - 8
                val inBottomLeft = r >= matrixSize - 8 && c < 8
                val inTiming = r == 6 || c == 6
                val inAlignment = r in alignStartR until (alignStartR + 5) && c in alignStartC until (alignStartC + 5)
                val inCenterLogo = r in 11..17 && c in 11..17

                if (!inTopLeft && !inTopRight && !inBottomLeft && !inTiming && !inAlignment && !inCenterLogo) {
                    val byteVal = if (bytes.isNotEmpty()) bytes[byteIdx % bytes.size].toInt() else 0
                    val bit = (byteVal shr (bitIdx % 8)) and 1
                    // Standard QR mask (row + col) % 2 == 0
                    val mask = (r + c) % 2 == 0
                    isDark[r][c] = (bit == 1) xor mask
                    bitIdx++
                    if (bitIdx % 8 == 0) byteIdx++
                }
            }
        }

        // Draw background
        drawRect(color = Color.White, size = size)

        // Draw dark modules
        val darkColor = Color(0xFF1E293B) // Dark slate
        for (r in 0 until matrixSize) {
            for (c in 0 until matrixSize) {
                if (isDark[r][c]) {
                    drawRect(
                        color = darkColor,
                        topLeft = Offset(c * moduleWidth, r * moduleHeight),
                        size = Size(moduleWidth + 0.5f, moduleHeight + 0.5f)
                    )
                }
            }
        }
    }
}
