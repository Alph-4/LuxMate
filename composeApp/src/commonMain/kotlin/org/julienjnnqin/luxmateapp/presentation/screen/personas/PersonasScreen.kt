package org.julienjnnqin.luxmateapp.presentation.screen.personas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.julienjnnqin.luxmateapp.data.model.Persona

@Composable
fun PersonasScreen(
    viewModel: PersonasViewModel,
    onPersonaSelected: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Text("Loading personas...")
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else {
            LazyColumn {
                items(uiState.personas) { persona: Persona ->
                    Text(
                        text = persona.name,
                        modifier = Modifier
                            .clickable { onPersonaSelected(persona.id) }
                    )
                }
            }
        }
    }
}
