package org.julienjnnqin.luxmateapp.presentation.screen.chat


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.julienjnnqin.luxmateapp.presentation.components.HeaderBar

@Composable
fun ChatDetailScreen(
    viewModel: ChatViewModel,
    sessionId: String,
    onBack: () -> Unit
) {
    val messages by viewModel.messagesState.collectAsState()
    val isThinking by viewModel.isThinking.collectAsState()
    val currentSessionId by viewModel.currentSessionIdState.collectAsState()  // Assure-toi d'avoir un StateFlow pour ça
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Création/Récupération de la session au lancement
    LaunchedEffect(sessionId) {
        viewModel.createSessionAndOpen(sessionId)
    }

    // Auto-scroll vers le bas
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // --- TOP BAR ---
        HeaderBar(
            trailingBtn = true,
            trailingBtnAction = onBack,
            title = if (currentSessionId.isEmpty()) "Chargement..." else "Discussion",
        )

        // --- MESSAGES ---
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            // Augmente le padding bottom pour laisser de la place à la barre de saisie et à la nav bar
            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                if (msg.isFromUser) OutgoingBubble(msg.content) else IncomingBubble(msg.content)
            }
            if (isThinking) {
                item {
                    Text(
                        "Le persona réfléchit...",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // --- INPUT BAR ---
        ChatInputBar(
            text = input,
            onTextChange = { input = it },
            onSend = {
                if (input.isNotBlank()) {
                    viewModel.sendMessage(input)
                    input = ""
                }
            },
            onMic = { /* TODO: action micro */ }
        )

        // petit espace de sécurité sous la barre (utile si navigation bar est présente)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onMic: () -> Unit
) {
    // Container global
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Pill background + input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main pill (icône micro + champ)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(28.dp))
                    .padding(start = 12.dp, end = 72.dp), // laisse de la place pour le bouton send qui chevauche
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onMic, modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = "Micro",
                            tint = Color(0xFF6B7280)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextField(
                        value = text,
                        onValueChange = onTextChange,
                        placeholder = { Text("Ask Professor Pierre a question...") },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            textColor = Color(0xFF0F1724),
                            placeholderColor = Color(0xFF9CA3AF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Send button (chevauche la pastille)
            Box(
                modifier = Modifier
                    .offset(x = (-56).dp) // fait chevaucher le bouton sur la pastille
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                val gradient = Brush.linearGradient(
                    colors = listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED))
                )
                IconButton(
                    onClick = onSend,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(gradient)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Envoyer",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun IncomingBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF3F4F6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.Mic, contentDescription = null) // just placeholder icon
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(text = text, color = Color(0xFF0F1724))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Read 10:42 AM", fontSize = 11.sp, color = Color(0xFF9CA3AF))
        }
    }
}

@Composable
private fun OutgoingBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.End) {
        Column(horizontalAlignment = Alignment.End) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF2563EB), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(text = text, color = Color.White)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Sent", fontSize = 11.sp, color = Color(0xFF9CA3AF))
        }
    }
}
