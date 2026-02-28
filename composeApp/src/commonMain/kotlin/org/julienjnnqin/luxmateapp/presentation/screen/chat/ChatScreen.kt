package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Wrapper ChatScreen (utilisé par NavigationHost). Montre soit la liste des sessions soit le détail du chat
 * en fonction de l'état du ViewModel. Il crée et ouvre une session pour le `personaId` fourni via la factory Koin.
 */
@Composable
fun ChatScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    // create session for persona when screen opens
    LaunchedEffect(Unit) {
        viewModel.createSessionAndOpen()
    }

    // if a session is open, currentSessionId will be non-empty
    if (viewModel.currentSessionId.isNotBlank()) {
        ChatDetailScreen(viewModel = viewModel, conversationId = viewModel.currentSessionId, onBack = onBack)
    } else {
        ConversationsScreen(viewModel = viewModel, onOpenConversation = { /* navigation handled by outer wrapper */ })
    }
}


/**
 * Conversations list (style Telegram) and chat detail screen.
 * - ConversationsScreen: shows list of conversations with avatar, last message, unread badge.
 * - ChatDetailScreen: shows messages, message bubbles, input bar with send/attach/mic.
 * Both screens use ChatViewModel provided in commonMain.
 */

@Composable
fun ConversationsScreen(viewModel: ChatViewModel, onOpenConversation: (String) -> Unit) {
    val uiState by viewModel.sessionsState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF6F7FB)) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Messages", fontSize = 20.sp, color = Color(0xFF0F1724))
                    Text(text = "Recent conversations", fontSize = 12.sp, color = Color(0xFF6B7280))
                }
                // Placeholder action
                Button(onClick = { /* new chat */ }) { Text("New") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState) { conv ->
                    ConversationRow(conversation = conv, onClick = {
                        onOpenConversation(conv.id)
                        viewModel.openSession(conv.id)
                    })
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(conversation: Conversation, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .background(Color.Transparent)
        .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {

        // Avatar
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
            // Placeholder initial
            Text(text = conversation.title.take(1), fontSize = 20.sp, color = Color(0xFF2563EB))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = conversation.title, fontSize = 16.sp, color = Color(0xFF0F1724))
                Text(text = conversation.lastSeen, fontSize = 12.sp, color = Color(0xFF9CA3AF))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = conversation.lastMessage, fontSize = 14.sp, color = Color(0xFF6B7280), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.weight(1f))
                if (conversation.unreadCount > 0) {
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2563EB))
                        .padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(text = conversation.unreadCount.toString(), color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatDetailScreen(viewModel: ChatViewModel, conversationId: String, onBack: () -> Unit) {
    val uiState by viewModel.messagesState.collectAsState()
    val isThinking by viewModel.isThinking.collectAsState()
    var input by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(uiState.size) {
        if (uiState.isNotEmpty()) {
            listState.animateScrollToItem(uiState.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.clickable { onBack() })
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                    Text(text = viewModel.currentSessionId.take(1), color = Color(0xFF2563EB))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = viewModel.currentSessionId, fontSize = 16.sp, color = Color(0xFF0F1724))
                    Text(text = "Active now", fontSize = 12.sp, color = Color(0xFF6B7280))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Messages list
            LazyColumn(state = listState, modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState) { msg ->
                    if (msg.isFromUser) {
                        OutgoingBubble(msg.content)
                    } else {
                        IncomingBubble(msg.content)
                    }
                }

                if (isThinking) {
                    item {
                        // simple typing indicator
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.Start) {
                            Spacer(modifier = Modifier.width(64.dp))
                            Text(text = "Pierre is thinking...", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                        }
                    }
                }
            }
        }

        // Input Bar
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3F4F6)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* attach */ }) { Icon(imageVector = Icons.Filled.AttachFile, contentDescription = null) }
            TextField(value = input, onValueChange = { input = it }, modifier = Modifier.weight(1f), placeholder = { Text("Ask a question...") })
            Spacer(modifier = Modifier.width(8.dp))
            if (input.isBlank()) {
                IconButton(onClick = { /* mic */ }) { Icon(imageVector = Icons.Filled.Mic, contentDescription = null) }
            } else {
                IconButton(onClick = {
                    // send
                    viewModel.sendMessage(input)
                    input = ""
                }) { Icon(imageVector = Icons.Default.Send, contentDescription = null) }
            }
        }
    }

    // Launch loading of conversation
    LaunchedEffect(conversationId) {
        viewModel.openSession(conversationId)
    }
}

@Composable
private fun IncomingBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF3F4F6)), contentAlignment = Alignment.Center) {
            Icon(imageVector = Icons.Filled.Mic, contentDescription = null) // just placeholder icon
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Box(modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(12.dp)) {
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
            Box(modifier = Modifier
                .background(Color(0xFF2563EB), RoundedCornerShape(12.dp))
                .padding(12.dp)) {
                Text(text = text, color = Color.White)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Sent", fontSize = 11.sp, color = Color(0xFF9CA3AF))
        }
    }
}
