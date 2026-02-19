package org.julienjnnqin.luxmateapp.presentation.screen.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.usecase.GetAllTeachersUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.SearchTeachersUseCase

data class TeachersUiState(
    val teachers: List<Teacher> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "Tous"
)

class TeachersViewModel(
    private val getAllTeachersUseCase: GetAllTeachersUseCase,
    private val searchTeachersUseCase: SearchTeachersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TeachersUiState())
    val uiState: StateFlow<TeachersUiState> = _uiState.asStateFlow()

    init {
        loadTeachers()
    }

    private fun loadTeachers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = getAllTeachersUseCase()
            result.onSuccess { teachers ->
                _uiState.value = _uiState.value.copy(
                    teachers = teachers,
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
            loadTeachers()
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
                    teachers = teachers,
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

    fun setCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
}

