package org.julienjnnqin.luxmateapp.presentation.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(uiState.messages) { message ->
                Text((if (message.isFromUser) "You: " else "Bot: ") + message.content)
            }
        }
        Button(onClick = onBack) { Text("Back") }
    }
}
