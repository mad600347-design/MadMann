package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightSaffron
import com.example.ui.theme.DeepIndigoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.IndigoSecondary

data class DailyStudyMetric(
    val dayLabel: String,
    val fullDate: String,
    val minutesStudied: Int,
    val lessonsCompleted: Int,
    val quizScorePercent: Int
)

data class SubjectQuizScore(
    val subject: String,
    val scorePercent: Int,
    val quizzesTaken: Int,
    val targetPercent: Int = 90
)

enum class MetricViewType(val label: String, val unit: String) {
    TIME_SPENT("Study Time", "mins"),
    LESSONS("Lessons Done", "completed"),
    QUIZ_SCORES("Avg Quiz Score", "%")
}

@Composable
fun StudentProgressVisualizer(
    modifier: Modifier = Modifier
) {
    // 7-day study tracking data
    val weeklyMetrics = remember {
        listOf(
            DailyStudyMetric("Mon", "22 Sep", 45, 1, 76),
            DailyStudyMetric("Tue", "23 Sep", 60, 2, 84),
            DailyStudyMetric("Wed", "24 Sep", 35, 1, 80),
            DailyStudyMetric("Thu", "25 Sep", 90, 3, 88),
            DailyStudyMetric("Fri", "26 Sep", 75, 2, 82),
            DailyStudyMetric("Sat", "27 Sep", 110, 4, 94),
            DailyStudyMetric("Sun", "Today", 85, 2, 90)
        )
    }

    val subjectScores = remember {
        listOf(
            SubjectQuizScore("Mathematics (Quadratic & Trig)", 90, 12),
            SubjectQuizScore("Physics (Light & Motion)", 84, 9),
            SubjectQuizScore("Chemistry (Reactions & Acids)", 86, 8),
            SubjectQuizScore("Biology (Life Processes)", 92, 10),
            SubjectQuizScore("English (Grammar & Spoken)", 78, 6)
        )
    }

    var selectedMetricType by remember { mutableStateOf(MetricViewType.TIME_SPENT) }
    var selectedDayIndex by remember { mutableIntStateOf(6) } // Default to today (Sunday)

    val selectedDay = weeklyMetrics[selectedDayIndex]
    val totalMinutes = weeklyMetrics.sumOf { it.minutesStudied }
    val totalLessons = weeklyMetrics.sumOf { it.lessonsCompleted }
    val averageQuizScore = weeklyMetrics.map { it.quizScorePercent }.average().toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("student_progress_visualizer_container")
    ) {
        // SUMMARY KPI CARDS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryMetricKpiCard(
                title = "Total Study Time",
                value = "${totalMinutes / 60}h ${totalMinutes % 60}m",
                subtitle = "7-Day Total",
                icon = Icons.Default.Schedule,
                accentColor = DeepIndigoPrimary,
                modifier = Modifier.weight(1f)
            )

            SummaryMetricKpiCard(
                title = "Lessons Done",
                value = "$totalLessons",
                subtitle = "Vidya Videos",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                accentColor = BrightSaffron,
                modifier = Modifier.weight(1f)
            )

            SummaryMetricKpiCard(
                title = "Avg Quiz Score",
                value = "$averageQuizScore%",
                subtitle = "Weekly Accuracy",
                icon = Icons.Default.Assessment,
                accentColor = EmeraldSuccess,
                modifier = Modifier.weight(1f)
            )
        }

        // MAIN VISUALIZATION CHART CONTAINER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header & Metric Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.QueryStats,
                                contentDescription = null,
                                tint = DeepIndigoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Weekly Growth Analytics",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Track your learning curve & exam readiness",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }

                    // Consistency Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldSuccess.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+18% this week",
                                color = EmeraldSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metric Filter Chips (Recharts-style multi-metric toggle)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MetricViewType.entries.forEach { type ->
                        val isSelected = selectedMetricType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMetricType = type },
                            label = { Text(type.label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepIndigoPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("chart_filter_${type.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // INTERACTIVE CANVASES (Bar / Curve Chart)
                GrowthBarChartCanvas(
                    metrics = weeklyMetrics,
                    selectedType = selectedMetricType,
                    selectedIndex = selectedDayIndex,
                    onSelectIndex = { selectedDayIndex = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // SELECTED DAY DETAIL CARD (Interactive Tooltip / Callout)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedDay.dayLabel} (${selectedDay.fullDate}) Metrics",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepIndigoPrimary
                            )
                            Text(
                                text = "Tap any day column to view detailed breakdown",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${selectedDay.minutesStudied}m",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DeepIndigoPrimary
                                )
                                Text(text = "Study Time", fontSize = 9.sp, color = Color.Gray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${selectedDay.lessonsCompleted}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BrightSaffron
                                )
                                Text(text = "Lessons", fontSize = 9.sp, color = Color.Gray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${selectedDay.quizScorePercent}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = EmeraldSuccess
                                )
                                Text(text = "Quiz Avg", fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // SUBJECT-WISE QUIZ MASTERY BREAKDOWN (Progress Bar Visualizer)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Subject-Wise Quiz Performance",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Based on daily practice questions & DPP tests",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(IndigoSecondary.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Target: 90%+",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                subjectScores.forEach { subject ->
                    SubjectScoreProgressRow(subjectScore = subject)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SummaryMetricKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun GrowthBarChartCanvas(
    metrics: List<DailyStudyMetric>,
    selectedType: MetricViewType,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit
) {
    val maxVal = when (selectedType) {
        MetricViewType.TIME_SPENT -> 120f
        MetricViewType.LESSONS -> 5f
        MetricViewType.QUIZ_SCORES -> 100f
    }

    val primaryColor = when (selectedType) {
        MetricViewType.TIME_SPENT -> DeepIndigoPrimary
        MetricViewType.LESSONS -> BrightSaffron
        MetricViewType.QUIZ_SCORES -> EmeraldSuccess
    }

    val secondaryColor = when (selectedType) {
        MetricViewType.TIME_SPENT -> GoldenAmber
        MetricViewType.LESSONS -> GoldenAmber
        MetricViewType.QUIZ_SCORES -> BrightSaffron
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val barWidth = size.width / metrics.size
                        val tappedIndex = (offset.x / barWidth).toInt().coerceIn(0, metrics.size - 1)
                        onSelectIndex(tappedIndex)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val count = metrics.size
                val barSlotWidth = canvasWidth / count
                val barWidth = barSlotWidth * 0.45f

                // Draw horizontal guide reference lines (25%, 50%, 75%, 100%)
                val guideLines = 4
                for (i in 1..guideLines) {
                    val y = canvasHeight * (1f - (i.toFloat() / guideLines))
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.35f),
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Draw Bar Columns
                metrics.forEachIndexed { index, item ->
                    val rawValue = when (selectedType) {
                        MetricViewType.TIME_SPENT -> item.minutesStudied.toFloat()
                        MetricViewType.LESSONS -> item.lessonsCompleted.toFloat()
                        MetricViewType.QUIZ_SCORES -> item.quizScorePercent.toFloat()
                    }

                    val normalizedHeight = (rawValue / maxVal).coerceIn(0.08f, 1f) * (canvasHeight - 20f)
                    val barX = (index * barSlotWidth) + (barSlotWidth - barWidth) / 2f
                    val barY = canvasHeight - normalizedHeight

                    val isSelected = index == selectedIndex

                    // Draw Bar Background pill
                    drawRoundRect(
                        color = if (isSelected) primaryColor.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.15f),
                        topLeft = Offset(barX - 4f, 10f),
                        size = Size(barWidth + 8f, canvasHeight - 10f),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )

                    // Draw actual value bar with gradient
                    val brush = Brush.verticalGradient(
                        colors = if (isSelected) {
                            listOf(secondaryColor, primaryColor)
                        } else {
                            listOf(primaryColor.copy(alpha = 0.75f), primaryColor.copy(alpha = 0.5f))
                        },
                        startY = barY,
                        endY = canvasHeight
                    )

                    drawRoundRect(
                        brush = brush,
                        topLeft = Offset(barX, barY),
                        size = Size(barWidth, normalizedHeight),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // If selected, draw highlight marker indicator on top of bar
                    if (isSelected) {
                        drawCircle(
                            color = secondaryColor,
                            radius = 4.dp.toPx(),
                            center = Offset(barX + barWidth / 2f, barY - 6.dp.toPx())
                        )
                    }
                }
            }
        }

        // Day Labels Row below the chart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            metrics.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                Text(
                    text = item.dayLabel,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                    color = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { onSelectIndex(index) }
                )
            }
        }
    }
}

@Composable
fun SubjectScoreProgressRow(subjectScore: SubjectQuizScore) {
    val progress = subjectScore.scorePercent / 100f
    val barColor = when {
        subjectScore.scorePercent >= 90 -> EmeraldSuccess
        subjectScore.scorePercent >= 80 -> DeepIndigoPrimary
        else -> BrightSaffron
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subjectScore.subject,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${subjectScore.scorePercent}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${subjectScore.quizzesTaken} tests)",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
