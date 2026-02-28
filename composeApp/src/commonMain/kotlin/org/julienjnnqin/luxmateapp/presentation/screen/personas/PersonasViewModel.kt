package org.julienjnnqin.luxmateapp.presentation.screen.personas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.core.utils.TeacherTheme
import org.julienjnnqin.luxmateapp.domain.entity.Persona
import org.julienjnnqin.luxmateapp.domain.usecase.GetAllTeachersUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.SearchTeachersUseCase

data class PersonaUiState(
    val personas: List<Persona> = emptyList(),
    val selectedTheme: TeacherTheme? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class PersonasViewModel(
    private val getAllTeachersUseCase: GetAllTeachersUseCase,
    private val searchTeachersUseCase: SearchTeachersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonaUiState())
    val uiState: StateFlow<PersonaUiState> = _uiState.asStateFlow()

    init {
        loadPersonas()
    }


    private fun loadPersonas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = getAllTeachersUseCase()
            result.onSuccess { teachers ->
                _uiState.value = _uiState.value.copy(
                    personas = teachers,
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isEmpty()) {
            loadPersonas()
        } else {
            searchTeachers(query)
        }
    }

    private fun searchTeachers(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = searchTeachersUseCase(query)
            result.onSuccess { teachers ->
                _uiState.value = _uiState.value.copy(
                    personas = teachers,
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun setCategory(category: String?) {
            _uiState.value = _uiState.value.copy(selectedTheme = TeacherTheme.entries.firstOrNull { it.name == category })
    }
}
