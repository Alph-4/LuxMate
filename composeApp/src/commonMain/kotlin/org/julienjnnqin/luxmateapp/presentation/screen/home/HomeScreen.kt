package org.julienjnnqin.luxmateapp.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview


// Mock data models
private data class Teacher(val name: String, val title: String, val rating: String)
private data class Message(val title: String, val subtitle: String, val time: String)

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = HomeViewModel())
}

@Composable
fun HomeScreen(
    name: String = "Jean-Sébastien",
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    HeaderSection(name = name)
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(title = "Weekly Goal", subtitle = "+5% from yesterday", progress = 0.75f)
                        StatCard(title = "Daily Streak", subtitle = "You're on fire!", progressText = "12 Days")
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    QuickResumeCard()
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    SectionHeader(title = "Suggested for You")
                    Spacer(modifier = Modifier.height(8.dp))
                    SuggestedGrid()
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    SectionHeader(title = "Featured Teachers")
                    Spacer(modifier = Modifier.height(8.dp))
                    FeaturedTeachersRow(
                        teachers = remember {
                            listOf(
                                Teacher("Sophia", "NLP Specialist", "4.9"),
                                Teacher("Marcus", "Ethics in AI", "4.8"),
                                Teacher("Ava", "Computer Vision", "4.7")
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    SectionHeader(title = "Recent Messages")
                    Spacer(modifier = Modifier.height(8.dp))
                    val messages = remember {
                        listOf(
                            Message(
                                "Neural Networks 101",
                                "That's a great observation about backpropagation...",
                                "10m ago"
                            ),
                            Message(
                                "NLP Foundations",
                                "Your analysis of the transformer architecture was spot on.",
                                "2h ago"
                            ),
                            Message(
                                "Python for AI",
                                "You completed the Data Cleaning module. Great progress!",
                                "Yesterday"
                            )
                        )
                    }
                    MessagesList(messages = messages)
                    Spacer(modifier = Modifier.height(80.dp)) // space for bottom nav
                }
            }

        }
    }
}

@Composable
private fun HeaderSection(name: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Bonjour ", fontSize = 28.sp, color = Color(0xFF0F1724))
            Text(text = name + " !", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Ready to continue your AI journey today?", fontSize = 14.sp, color = Color(0xFF6B7280))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFDDE7FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "avatar",
                tint = Color(0xFF1F2937),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun StatCard(title: String, subtitle: String = "", progress: Float? = null, progressText: String? = null) {
    Card(
        modifier = Modifier
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (progress != null) {
                Box(modifier = Modifier.size(52.dp), contentAlignment = Alignment.Center) {
                    // simple circular placeholder
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(brush = Brush.linearGradient(listOf(Color(0xFFEEF2FF), Color(0xFFDDE7FF))))
                    )
                    Text(text = "${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold, color = Color(0xFF0F1724))
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF4EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFF6B35))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = progressText ?: title, fontWeight = FontWeight.Bold, color = Color(0xFF0F1724))
                Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF6B7280))
            }
        }
    }
}

@Composable
private fun QuickResumeCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp))
                    .background(brush = Brush.linearGradient(listOf(Color(0xFF6EE7B7), Color(0xFF60A5FA)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "LAST LESSON WITH PIERRE", fontSize = 12.sp, color = Color(0xFF6B7280))
                Text(text = "Neural Networks 101", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
            ) {
                Text(
                    text = "Continue Learning",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    color = Color(0xFF2563EB)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F1724))
        Text(text = "See All", fontSize = 12.sp, color = Color(0xFF2563EB))
    }
}

@Composable
private fun SuggestedGrid() {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SuggestionPill(label = "Mathematics", color = Color(0xFFFFF4EB))
            SuggestionPill(label = "English", color = Color(0xFFEFF6FF))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SuggestionPill(label = "Science", color = Color(0xFFF0FFF4))
            SuggestionPill(label = "Creative Art", color = Color(0xFFF7F0FF))
        }
    }
}

@Composable
private fun SuggestionPill(label: String, color: Color) {
    Card(
        shape = RoundedCornerShape(20.dp), modifier = Modifier
            .height(86.dp), colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Book, contentDescription = null, tint = Color(0xFF2563EB))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FeaturedTeachersRow(teachers: List<Teacher>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(teachers) { teacher ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 150.dp, height = 150.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF111827)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = teacher.name, fontWeight = FontWeight.Bold)
                    Text(text = teacher.title, fontSize = 12.sp, color = Color(0xFF6B7280))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB020))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = teacher.rating, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessagesList(messages: List<Message>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        messages.forEach { msg ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Book, contentDescription = null, tint = Color(0xFF2563EB))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = msg.title, fontWeight = FontWeight.Bold)
                        Text(
                            text = msg.subtitle,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = msg.time, fontSize = 12.sp, color = Color(0xFF9CA3AF))
                }
            }
        }
    }
}
