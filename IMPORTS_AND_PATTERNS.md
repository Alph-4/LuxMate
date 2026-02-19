# 🔧 Imports courants et références

## ✅ Imports à utiliser dans vos fichiers

### Material 3
```kotlin
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.material3.CircularProgressIndicator
```

### Icons Material (pour Material 3)
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Email
```

### Compose Foundation
```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
```

### ViewModel & State
```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
```

### Navigation
```kotlin
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
```

### Koin (Dependency Injection)
```kotlin
import org.koin.compose.koinViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext  // Android only
```

### Images avec Coil
```kotlin
import coil3.compose.AsyncImage
import coil3.compose.LocalAsyncImagePainter
```

### Coroutines
```kotlin
import kotlinx.coroutines.flow.collectAsState
import kotlinx.coroutines.launch
```

### Serialization
```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
```

---

## 🎨 Utilities fréquemment utilisées

### Modifier utiles
```kotlin
Modifier.fillMaxSize()           // Remplit tout l'espace
Modifier.fillMaxWidth()          // Remplit la largeur
Modifier.height(56.dp)           // Hauteur fixe
Modifier.padding(16.dp)          // Padding
Modifier.background(color)       // Fond de couleur
Modifier.clip(CircleShape)       // Coupe circulaire
Modifier.weight(1f)              // Flex grow
```

### Layout
```kotlin
Column { }              // Stack vertical
Row { }                 // Stack horizontal
Box { }                 // Overlay/Centering
LazyColumn { }          // Liste lazy vertical
LazyRow { }             // Liste lazy horizontal
```

### Spacing
```kotlin
Spacer(modifier = Modifier.height(16.dp))
Spacer(modifier = Modifier.width(8.dp))
```

### Material3 Theme
```kotlin
MaterialTheme.colorScheme.primary
MaterialTheme.colorScheme.onPrimary
MaterialTheme.colorScheme.surface
MaterialTheme.colorScheme.background
MaterialTheme.typography.headlineLarge
MaterialTheme.typography.bodyMedium
MaterialTheme.shapes.large         // RoundedCornerShape
```

---

## 📋 Data Classes communes

### Entités
```kotlin
@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String? = null
)

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val subject: String,
    val level: String,
    val description: String,
    val avatar: String,
    val isOnline: Boolean = false,
    val rating: Float = 0f
)
```

### States
```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null
)
```

---

## 🔗 Navigation

### Type-safe routes
```kotlin
sealed class Screen {
    data object Onboarding : Screen()
    data object Login : Screen()
    data object Teachers : Screen()
    data class TeacherDetail(val teacherId: String) : Screen()
    data object Profile : Screen()
}
```

### Navigate
```kotlin
// Dans AppViewModel
fun navigate(screen: Screen) {
    _appState.value = _appState.value.copy(currentScreen = screen)
}
```

---

## 💾 Patterns utiles

### ViewModel avec StateFlow
```kotlin
class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow(MyState())
    val state: StateFlow<MyState> = _state.asStateFlow()
    
    fun doAction() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = myUseCase()
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}
```

### Composable avec ViewModel
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    
    Column {
        // UI based on state
    }
}
```

### Repository Pattern
```kotlin
interface MyRepository {
    suspend fun getData(): Result<Data>
}

class MyRepositoryImpl : MyRepository {
    override suspend fun getData(): Result<Data> {
        return try {
            Result.success(fetchData())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

---

## 🧪 Tests

```kotlin
@Test
fun testViewModel() = runTest {
    val viewModel = MyViewModel(mockUseCase)
    viewModel.doAction()
    
    assertEquals(true, viewModel.state.value.isLoading)
}
```

---

## 🎯 Bonnes pratiques

✅ **DO**:
- Utiliser `collectAsState()` dans les composables
- Passer les viewmodels par paramètre
- Utiliser des data classes immuables
- Séparer UI logic du business logic
- Nommer les états explicitement

❌ **DON'T**:
- Ne pas accéder directement aux repositories dans les composables
- Ne pas créer des ViewModels manuellement
- Ne pas utiliser de variables mutables dans les composables
- Ne pas passer d'objets complexes par paramètre
- Ne pas faire de réseau appels dans les composables

---

## 📞 Ressources

- [Kotlin Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Docs](https://developer.android.com/jetpack/compose)
- [Koin Docs](https://insert-koin.io/)
- [Coil Docs](https://coil-kt.github.io/coil/)

