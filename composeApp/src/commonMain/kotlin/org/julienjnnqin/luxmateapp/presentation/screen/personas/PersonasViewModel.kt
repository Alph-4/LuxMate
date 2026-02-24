package org.julienjnnqin.luxmateapp.presentation.screen.personas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.data.model.Persona
import org.julienjnnqin.luxmateapp.domain.repository.PersonaRepository

data class PersonasUiState(
        val personas: List<Persona> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
)

class PersonasViewModel(private val personaRepository: PersonaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonasUiState())
    val uiState: StateFlow<PersonasUiState> = _uiState.asStateFlow()

    init {
        loadPersonas()
    }

    private fun loadPersonas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val list = personaRepository.getPersonas()
                _uiState.value = _uiState.value.copy(personas = list, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
