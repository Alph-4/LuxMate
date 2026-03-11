package org.julienjnnqin.luxmateapp.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.entity.Persona
import org.julienjnnqin.luxmateapp.domain.entity.User


// Mock data models

@Preview
@Composable
fun HomeScreenPreview() {

    val fakeUser = User(
        id = "0", name = "test", email = "johndoe@gmail.com"
    )

    HomeScreenContent(fakeUser)
}


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    recentMsgSeeAllClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        uiState
    ) {
        println("HomeScreen LaunchedEffect - UI State: $uiState")

    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        println("UI State: $uiState")

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${uiState.error}", color = Color.Red)
            }
        } else {
            HomeScreenContent(
                user = uiState.user ?: User(
                    id = "0", name = "test", email = "",
                ),
                messages = uiState.chatHistory,
                teachers = uiState.teachers,
                recentMsgSeeAllClick
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    user: User,
    messages: List<ChatHistory> = emptyList(),
    teachers: List<Persona> = emptyList(),
    recentMsgSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 50.dp)
            ) {
                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    HeaderSection(name = user.email.split("@").firstOrNull() ?: "User")
                    Spacer(modifier = Modifier.height(18.dp))
                }

                /*
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(title = "Weekly Goal", subtitle = "+5% from yesterday", progress = 0.75f)
                        StatCard(title = "Daily Streak", subtitle = "You're on fire!", progressText = "12 Days")
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }*/

                item {
                    QuickResumeCard()
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    SectionHeader(title = "Start by theme", {})
                    Spacer(modifier = Modifier.height(8.dp))
                    SuggestedGrid()
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    SectionHeader(title = "Featured Teachers", {})
                    Spacer(modifier = Modifier.height(8.dp))
                    FeaturedTeachersRow(
                        personas = teachers
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }

                item {
                    SectionHeader(title = "Recent Messages", recentMsgSeeAllClick = recentMsgSeeAllClick)
                    Spacer(modifier = Modifier.height(8.dp))
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
            .height(100.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp, clip = true,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
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
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,

                ) {
                Column(modifier = Modifier) {
                    Text(text = "LAST LESSON WITH PIERRE", fontSize = 12.sp, color = Color(0xFF6B7280))
                    Text(text = "Neural Networks 101", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
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
}

@Composable
private fun SectionHeader(
    title: String,
    recentMsgSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F1724))
        Text(
            modifier = Modifier.clickable(onClick = recentMsgSeeAllClick),
            text = "See All",
            fontSize = 12.sp,
            color = Color(0xFF2563EB)
        )
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
private fun FeaturedTeachersRow(personas: List<Persona>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(personas) { persona ->
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
                    Text(text = persona.name, fontWeight = FontWeight.Bold)
                    Text(text = persona.subject, fontSize = 12.sp, color = Color(0xFF6B7280))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB020))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = persona.rating.toString(), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessagesList(messages: List<ChatHistory>, onOpenConversation: (String) -> Unit = {}) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        messages.forEach { chat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onOpenConversation(chat.id) })
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            chat.personaName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            chat.theme,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
