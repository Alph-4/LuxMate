package org.julienjnnqin.luxmateapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.julienjnnqin.luxmateapp.domain.entity.ChatHistory
import org.julienjnnqin.luxmateapp.domain.entity.Persona
import org.julienjnnqin.luxmateapp.domain.entity.User
import org.julienjnnqin.luxmateapp.domain.usecase.GetAllFeaturedTeachersUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetChatHistoryUseCase
import org.julienjnnqin.luxmateapp.domain.usecase.GetUserProfileUseCase

data class HomeUiState(
    val user: User? = null,
    val teachers: List<Persona> = emptyList(),
    val chatHistory: List<ChatHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val getFeaturedTeachersUseCase: GetAllFeaturedTeachersUseCase // Supposons que tu l'as ajouté
) : ViewModel() {

    // 1. Les sources de données (Privées)
    private val _user = MutableStateFlow<User?>(null)
    private val _teachers = MutableStateFlow<List<Persona>>(emptyList())
    private val _history = MutableStateFlow<List<ChatHistory>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // 2. La fusion (Publique et réactive)
    // On combine les 5 flux. Dès qu'un seul bouge, le bloc est ré-exécuté.
    val uiState: StateFlow<HomeUiState> = combine(
        _user, _teachers, _history, _isLoading, _error
    ) { user, teachers, history, loading, error ->
        HomeUiState(user, teachers, history, loading, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Garde le flux en vie 5s après fermeture
        initialValue = HomeUiState(isLoading = true)
    )

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Déclenchement simultané (Le "Future.wait" commence ici)
            val userDeferred = async { getUserProfileUseCase() }
            val teachersDeferred = async { getFeaturedTeachersUseCase() }
            val historyDeferred = async { getChatHistoryUseCase() }

            // 2. Récupération typée (Pas de risque d'erreur d'index ou de type)
            val userRes = userDeferred.await()
            val teachersRes = teachersDeferred.await()
            val historyRes = historyDeferred.await()

            // 3. Mise à jour des flows
            _user.value = userRes.getOrNull()
            _teachers.value = teachersRes.getOrDefault(emptyList())
            _history.value = historyRes.getOrDefault(emptyList())

            _isLoading.value = false
        }
    }
}