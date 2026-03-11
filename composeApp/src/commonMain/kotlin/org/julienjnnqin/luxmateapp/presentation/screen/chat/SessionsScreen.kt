package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.julienjnnqin.luxmateapp.domain.entity.Conversation

/**
 * Conversations list (style Telegram) and chat detail screen.
 * - ConversationsScreen: shows list of conversations with avatar, last message, unread badge.
 * - ChatDetailScreen: shows messages, message bubbles, input bar with send/attach/mic.
 * Both screens use ChatViewModel provided in commonMain.
 */
@Preview
@Composable
fun SessionsListScreen(
    viewModel: ChatViewModel,
    onOpenConversation: (String) -> Unit
) {

    val uiState by viewModel.sessionsState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(), color = Color(0xFFF6F7FB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Messages", fontSize = 20.sp, color = Color(0xFF0F1724))
                    Text(text = "Recent conversations", fontSize = 12.sp, color = Color(0xFF6B7280))
                }
                // Placeholder action
                Button(onClick = { /* new chat */ }) { Text("New") }
            }

            Spacer(modifier = Modifier.height(12.dp))
            if (uiState.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No conversations yet. Start a new chat!", color = Color(0xFF9CA3AF))
                }
            } else {
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
}

@Composable
private fun ConversationRow(conversation: Conversation, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.Transparent)
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {

        // Avatar
        Box(
            modifier = Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFEFF6FF)),
            contentAlignment = Alignment.Center
        ) {
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
                Text(
                    text = conversation.lastMessage,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2563EB))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = conversation.unreadCount.toString(), color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
