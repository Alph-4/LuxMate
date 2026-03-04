package org.julienjnnqin.luxmateapp.presentation.screen.chat


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(sessionId) {
        viewModel.createSessionAndOpen(sessionId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        // --- COUCHE 1 : MESSAGES ---
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            // Important : On laisse un gros padding en bas pour que le dernier message
            // ne soit pas caché par l'input bar flottante
            contentPadding = PaddingValues(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 180.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                if (msg.isFromUser) OutgoingBubble(msg.content) else IncomingBubble(msg.content)
            }
            if (isThinking) {
                item { ThinkingIndicator() }
            }
        }

        // --- COUCHE 2 : TOP BAR (Flottante) ---
        HeaderBar(
            modifier = Modifier.align(Alignment.TopCenter),
            trailingBtn = true,
            trailingBtnAction = onBack,
            title = "Professor Pierre",
        )

        // --- COUCHE 3 : INPUT BAR (Flottante en bas) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) // Décolle un peu du bord bas
        ) {
            ChatInputBar(
                text = input,
                onTextChange = { input = it },
                onSend = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(input)
                        input = ""
                    }
                }
            )
        }
    }
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    // Le conteneur blanc arrondi avec ombre
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.94f) // Légèrement plus étroit que l'écran
            .shadow(20.dp, RoundedCornerShape(28.dp), ambientColor = Color.LightGray),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Ligne supérieure : Input ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icône Trombone (Attach)
                Icon(
                    imageVector = Icons.Default.AttachFile, // Ou un painterResource si tu as l'icône précise
                    contentDescription = "Attach",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.padding(horizontal = 8.dp).rotate(45f)
                )

                // Champ de texte stylisé
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(Color(0xFFF1F5F9), CircleShape)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text("Ask Professor Pierre a question...", color = Color(0xFF94A3B8), fontSize = 14.sp)
                    }
                    BasicTextField(
                        value = text,
                        onValueChange = onTextChange,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Bouton Send avec Dégradé
                val gradient = Brush.linearGradient(
                    colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                )

                IconButton(
                    onClick = onSend,
                    modifier = Modifier
                        .size(48.dp)
                        .background(gradient, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // --- Ligne inférieure : Suggestions (Scrollable horizontalement) ---
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChipUI("Explain with an example")
                SuggestionChipUI("Show me the formula")
            }
        }
    }
}

@Composable
fun SuggestionChipUI(label: String) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        color = Color.Transparent
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ThinkingIndicator() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Petit texte discret
        Text(
            text = "Professor Pierre réfléchit",
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Medium
        )

        // Animation de trois points
        ThreeDotsAnimation()
    }
}

@Composable
fun ThreeDotsAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    // On crée un décalage pour chaque point
    val animations = listOf(0, 1, 2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = index * 200, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dot $index"
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        animations.forEach { anim ->
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .graphicsLayer { alpha = anim.value }
                    .background(Color(0xFF6366F1), CircleShape)
            )
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
