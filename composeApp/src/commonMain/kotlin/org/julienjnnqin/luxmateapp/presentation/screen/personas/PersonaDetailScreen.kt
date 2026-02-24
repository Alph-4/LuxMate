package org.julienjnnqin.luxmateapp.presentation.screen.personas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.julienjnnqin.luxmateapp.data.model.Persona

@Composable
fun PersonaDetailScreen(
    persona: Persona,
    onBackClick: () -> Unit,
    onStartConversation: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = persona.name)
        Text(text = persona.description ?: "")
        Button(onClick = { onStartConversation(persona.id) }) {
            Text("Start conversation")
        }
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}
