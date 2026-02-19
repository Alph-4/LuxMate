package org.julienjnnqin.luxmateapp.presentation.screen.teacherdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.julienjnnqin.luxmateapp.core.theme.PrimaryLight
import org.julienjnnqin.luxmateapp.core.theme.SuccessGreen
import org.julienjnnqin.luxmateapp.domain.entity.Teacher

@Composable
fun TeacherDetailScreen(
    teacher: Teacher,
    onBackClick: () -> Unit,
    onStartConversation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    "PROFIL TUTEUR",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(modifier = Modifier.size(40.dp))
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = teacher.avatar,
                        contentDescription = teacher.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (teacher.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(SuccessGreen, CircleShape)
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Name
                Text(
                    teacher.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = PrimaryLight,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subject
                Text(
                    "${teacher.subject} - ${teacher.level}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quote
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        "\"${teacher.description}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Expertise Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Filled.School,
                        contentDescription = null,
                        tint = PrimaryLight,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Expertise & Compétences",
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryLight
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Expertise chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExpertiseChip("Algèbre")
                    ExpertiseChip("Géométrie")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    ExpertiseChip("Calcul différentiel")
                }

                Spacer(modifier = Modifier.height(32.dp))

                // About section
                Text(
                    "À PROPOS DE L'IA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Pierre est conçu pour décomposer des concepts complexes en étapes logiques. Il utilise des exemples du monde réel pour rendre le calcul et l'algèbre intuitifs pour les étudiants de niveau collégial.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5f
                )

                Spacer(modifier = Modifier.height(40.dp))

                // CTA Button
                Button(
                    onClick = onStartConversation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryLight
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Démarrer une conversation",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ExpertiseChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}



