# 🛠️ Exemples d'extension - Comment ajouter de nouvelles features

## 📝 Exemple 1: Ajouter une nouvelle page/écran

### Étapes:

#### 1. Ajouter une route dans `Screen.kt`
```kotlin
sealed class Screen {
    // ... existing screens
    data class TeacherDetail(val teacherId: String) : Screen()
}
```

#### 2. Créer le ViewModel
```kotlin
// presentation/screen/teacherdetail/TeacherDetailViewModel.kt

data class TeacherDetailUiState(
    val teacher: Teacher? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class TeacherDetailViewModel(
    private val getTeacherByIdUseCase: GetTeacherByIdUseCase,
    private val teacherId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(TeacherDetailUiState())
    val uiState: StateFlow<TeacherDetailUiState> = _uiState.asStateFlow()

    init {
        loadTeacher()
    }

    private fun loadTeacher() {
        viewModelScope.launch {
            val result = getTeacherByIdUseCase(teacherId)
            result.onSuccess { teacher ->
                _uiState.value = _uiState.value.copy(
                    teacher = teacher,
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    error = error.message,
                    isLoading = false
                )
            }
        }
    }
}
```

#### 3. Créer la Screen
```kotlin
// presentation/screen/teacherdetail/TeacherDetailScreen.kt

@Composable
fun TeacherDetailScreen(
    viewModel: TeacherDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(onBackClick = onBackClick)
        
        when {
            uiState.isLoading -> LoadingState()
            uiState.error != null -> ErrorState(uiState.error!!)
            uiState.teacher != null -> {
                TeacherContent(teacher = uiState.teacher!!)
            }
        }
    }
}
```

#### 4. Ajouter au Koin module
```kotlin
// di/KoinModule.kt

val appModule = module {
    // ... existing
    single { GetTeacherByIdUseCase(get()) }
    single { (teacherId: String) -> 
        TeacherDetailViewModel(get(), teacherId) 
    }
}
```

#### 5. Intégrer dans la navigation
```kotlin
// App.kt

@Composable
fun AppNavigation() {
    // ... existing code
    
    appState.currentScreen == Screen.Teachers -> {
        val teachersViewModel: TeachersViewModel = koinViewModel()
        TeachersScreen(
            viewModel = teachersViewModel,
            onTeacherSelected = { teacherId ->
                // Navigate to detail
                appViewModel.navigateToTeacherDetail(teacherId)
            }
        )
    }
    
    is Screen.TeacherDetail -> {
        val detailVM: TeacherDetailViewModel = koinViewModel(
            parameters = { parametersOf((appState.currentScreen as Screen.TeacherDetail).teacherId) }
        )
        TeacherDetailScreen(
            viewModel = detailVM,
            onBackClick = { appViewModel.navigateToTeachers() }
        )
    }
}
```

---

## 🔗 Exemple 2: Intégrer une API réelle

### Ajouter la dépendance Ktor

```toml
# gradle/libs.versions.toml
[versions]
ktor = "2.3.4"

[libraries]
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-contentNegotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
ktor-client-android = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }  # Android only
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }    # iOS only
```

### Créer un API client

```kotlin
// data/api/ApiClient.kt

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

class ApiClient {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getTeachers(): List<Teacher> {
        return httpClient.get("https://api.luxmate.com/teachers").body()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return httpClient.post("https://api.luxmate.com/auth/login") {
            setBody(LoginRequest(email, password))
        }.body()
    }
}
```

### Utiliser dans le Repository

```kotlin
// data/repository/TeacherRepositoryImpl.kt

class TeacherRepositoryImpl(
    private val apiClient: ApiClient
) : TeacherRepository {
    override suspend fun getAllTeachers(): Result<List<Teacher>> {
        return try {
            val teachers = apiClient.getTeachers()
            Result.success(teachers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Ajouter au Koin

```kotlin
// di/KoinModule.kt

single { ApiClient() }
single<TeacherRepository> { 
    TeacherRepositoryImpl(get()) 
}
```

---

## 💾 Exemple 3: Ajouter la persistance avec DataStore

### Créer un Data Store Provider

```kotlin
// data/datastore/PreferencesDataStore.kt

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

class PreferencesDataStore(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val ONBOARDING_COMPLETED = stringPreferencesKey("onboarding_completed")
        private val USER_ID = stringPreferencesKey("user_id")
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed.toString()
        }
    }

    suspend fun isOnboardingCompleted(): Boolean {
        return dataStore.data.first()[ONBOARDING_COMPLETED]?.toBoolean() ?: false
    }

    suspend fun setUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun getUserId(): String? {
        return dataStore.data.first()[USER_ID]
    }
}
```

### Utiliser dans le Repository

```kotlin
// data/repository/OnboardingRepositoryImpl.kt

class OnboardingRepositoryImpl(
    private val dataStore: PreferencesDataStore
) : OnboardingRepository {
    override suspend fun getOnboardingState(): OnboardingState {
        val completed = dataStore.isOnboardingCompleted()
        return OnboardingState(isCompleted = completed)
    }

    override suspend fun setOnboardingCompleted(): Result<Unit> {
        return try {
            dataStore.setOnboardingCompleted(true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

---

## 🎨 Exemple 4: Créer un composable réutilisable

```kotlin
// presentation/components/TeacherCard.kt

@Composable
fun TeacherCard(
    teacher: Teacher,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = teacher.avatar,
                contentDescription = teacher.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    teacher.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    teacher.subject,
                    style = MaterialTheme.typography.labelLarge,
                    color = SuccessGreen
                )
                Text(
                    teacher.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Usage:
TeacherCard(
    teacher = teacher,
    onClick = { /* navigate */ },
    modifier = Modifier.padding(16.dp)
)
```

---

## 🧪 Exemple 5: Tests unitaires

```kotlin
// test/presentation/screen/teachers/TeachersViewModelTest.kt

@Test
fun testLoadTeachersSuccess() = runTest {
    // Arrange
    val mockTeachers = listOf(
        Teacher(id = "1", name = "Prof A", /*...*/),
        Teacher(id = "2", name = "Prof B", /*...*/),
    )
    val mockUseCase = mockk<GetAllTeachersUseCase> {
        coEvery { this@mockk() } returns Result.success(mockTeachers)
    }

    // Act
    val viewModel = TeachersViewModel(mockUseCase, mockk())

    // Assert
    assertEquals(mockTeachers, viewModel.uiState.value.teachers)
    assertEquals(false, viewModel.uiState.value.isLoading)
}

@Test
fun testLoadTeachersError() = runTest {
    // Arrange
    val mockError = Exception("Network error")
    val mockUseCase = mockk<GetAllTeachersUseCase> {
        coEvery { this@mockk() } returns Result.failure(mockError)
    }

    // Act
    val viewModel = TeachersViewModel(mockUseCase, mockk())

    // Assert
    assertEquals(mockError.message, viewModel.uiState.value.error)
    assertEquals(true, viewModel.uiState.value.teachers.isEmpty())
}
```

---

## 🚀 Checklist pour ajouter une feature

- [ ] Créer l'entité dans `domain/entity/`
- [ ] Créer l'interface repository dans `domain/repository/`
- [ ] Créer le use case dans `domain/usecase/`
- [ ] Implémenter le repository dans `data/repository/`
- [ ] Créer le ViewModel
- [ ] Créer la Screen
- [ ] Ajouter la route à `Screen.kt`
- [ ] Enregistrer au Koin module
- [ ] Intégrer à la navigation `App.kt`
- [ ] Tester les flows
- [ ] Écrire les tests unitaires

---

## 💡 Pro Tips

### Tip 1: Réutilisez les composants
```kotlin
// Au lieu de dupliquer le code
PrimaryButton(text = "Ok", onClick = { })
PrimaryButton(text = "Next", onClick = { })
```

### Tip 2: Nommez explicitement
```kotlin
// ✅ Bon
val isUserAuthenticated by viewModel.isUserAuthenticated.collectAsState()

// ❌ Mauvais
val state by viewModel.state.collectAsState()
```

### Tip 3: Utilisez prématériel3
```kotlin
// ✅ Utiliser les tokens Material3
color = MaterialTheme.colorScheme.primary

// ❌ Ne pas hardcoder
color = Color(0xFF3968f5)
```

### Tip 4: Decompose les composables
```kotlin
// ✅ Petits composables réutilisables
@Composable
fun TeacherHeader(teacher: Teacher) { }

@Composable  
fun TeacherContent(teacher: Teacher) { }

@Composable
fun TeacherScreen(teacher: Teacher) {
    TeacherHeader(teacher)
    TeacherContent(teacher)
}
```

---

## 📚 Ressources supplémentaires

- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Koin Documentation](https://insert-koin.io/)
- [Ktor Client](https://ktor.io/docs/client.html)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

---

**Happy extending! 🎉**

