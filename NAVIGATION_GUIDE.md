# 🗺️ Guide complet Navigation

## 📍 Système de navigation LuxMate

LuxMate utilise une **navigation type-safe** basée sur des `sealed classes` plutôt que des strings.

---

## 🎯 Routes disponibles

```kotlin
// presentation/navigation/Screen.kt

sealed class Screen {
    data object Onboarding : Screen()
    data object Login : Screen()
    data object Teachers : Screen()
    data object Profile : Screen()
    
    // Futur (optionnel):
    // data class TeacherDetail(val teacherId: String) : Screen()
    // data class ChatScreen(val teacherId: String) : Screen()
}
```

---

## 📊 Flow de navigation global

```
AppViewModel contrôle l'état global:
    ↓
AppState (currentScreen, isAuthenticated, etc)
    ↓
AppNavigation() dans App.kt affiche le bon écran
    ↓
Les écrans ont des callbacks pour les changements
```

---

## 🔄 Comment naviguer

### 1️⃣ Depuis un écran vers un autre

```kotlin
// Dans App.kt (AppNavigation)
TeachersScreen(
    viewModel = teachersViewModel,
    onProfileClick = {
        appViewModel.navigateToProfile()  // ← Appel navigation
    }
)
```

### 2️⃣ Via AppViewModel

```kotlin
class AppViewModel(...) : ViewModel() {
    
    fun navigateToTeachers() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Teachers
        )
    }

    fun navigateToProfile() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Profile
        )
    }

    fun navigateToLogin() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Login
        )
    }

    fun navigateToOnboarding() {
        _appState.value = _appState.value.copy(
            currentScreen = Screen.Onboarding
        )
    }
}
```

---

## 🔐 Logique de navigation intelligente

### État d'authentification
```kotlin
// AppViewModel auto-décide l'écran basé sur l'état

private suspend fun checkAuthStatus() {
    val user = getCurrentUserUseCase()  // Vérifie session
    
    if (user != null) {
        // Utilisateur logué → Teachers
        _appState.value = _appState.value.copy(
            isAuthenticated = true,
            currentScreen = Screen.Teachers
        )
    } else {
        // Pas logué → Login
        _appState.value = _appState.value.copy(
            isAuthenticated = false,
            currentScreen = Screen.Login
        )
    }
}
```

### Onboarding guard
```kotlin
// Si onboarding pas fait → affiche Onboarding, pas Login
if (!isOnboardingCompleted) {
    showOnboardingScreen()
}
```

---

## 🎪 AppNavigation - Le cerveau de la navigation

```kotlin
@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = koinViewModel()
    val appState = appViewModel.appState.collectAsState().value

    when {
        // Show loading spinner
        appState.isLoading -> {
            LoadingScreen()
        }
        
        // No onboarding done yet
        !appState.isOnboardingCompleted -> {
            OnboardingScreen(
                onComplete = { 
                    appViewModel.navigateToLogin() 
                }
            )
        }
        
        // Not authenticated - show login
        appState.currentScreen == Screen.Login -> {
            LoginScreen(
                onLoginSuccess = { 
                    appViewModel.navigateToTeachers() 
                }
            )
        }
        
        // Authenticated - show teachers
        appState.currentScreen == Screen.Teachers -> {
            TeachersScreen(
                onProfileClick = { 
                    appViewModel.navigateToProfile() 
                }
            )
        }
        
        // Show profile
        appState.currentScreen == Screen.Profile -> {
            ProfileScreen(
                onBackClick = { 
                    appViewModel.navigateToTeachers() 
                }
            )
        }
    }
}
```

---

## 🎬 Exemples de navigation complète

### Exemple 1: Onboarding → Login
```kotlin
// Utilisateur termine onboarding
OnboardingScreen(
    onComplete = {
        appViewModel.navigateToLogin()  // ← Navigue vers Login
    }
)

// AppNavigation détecte le changement
appState.currentScreen == Screen.Login → {
    LoginScreen(...)  // Affiche Login
}
```

### Exemple 2: Login → Teachers
```kotlin
// Utilisateur se connecte
LoginScreen(
    onLoginSuccess = {
        appViewModel.navigateToTeachers()  // ← Navigue vers Teachers
    }
)

// Mise à jour AppState
_appState.value = _appState.value.copy(
    isAuthenticated = true,
    currentScreen = Screen.Teachers
)

// AppNavigation affiche Teachers
appState.currentScreen == Screen.Teachers → {
    TeachersScreen(...)
}
```

### Exemple 3: Teachers → Profile → Teachers
```kotlin
// De Teachers, utilisateur clique profil
TeachersScreen(
    onProfileClick = {
        appViewModel.navigateToProfile()  // ← Navigue vers Profile
    }
)

// AppNavigation affiche Profile
appState.currentScreen == Screen.Profile → {
    ProfileScreen(
        onBackClick = {
            appViewModel.navigateToTeachers()  // ← Retour à Teachers
        }
    )
}
```

---

## 🛡️ Propriétés type-safe

### Avantages vs strings

❌ **Sans type-safety** (❌ À ne PAS faire):
```kotlin
// Navigation par strings - risqué!
when (currentRoute) {
    "onboarding" -> OnboardingScreen()
    "login" -> LoginScreen()
    "teachers" -> TeachersScreen()
    // Typo? → Écran ne s'affiche jamais (bug silencieux)
}
```

✅ **Avec type-safety** (✅ À faire):
```kotlin
// Navigation par sealed class - sûr!
when (appState.currentScreen) {
    Screen.Onboarding -> OnboardingScreen()
    Screen.Login -> LoginScreen()
    Screen.Teachers -> TeachersScreen()
    Screen.Profile -> ProfileScreen()
    // Typo? → Erreur compile-time (attrapée immédiatement)
}
```

---

## 🔗 Avec paramètres (Futur)

### Exemple: Ajouter TeacherDetail

```kotlin
// 1. Ajouter la route avec paramètre
sealed class Screen {
    // ...
    data class TeacherDetail(val teacherId: String) : Screen()
}

// 2. Naviguer avec paramètre
TeachersScreen(
    onTeacherSelected = { teacherId ->
        appViewModel.navigateToTeacherDetail(teacherId)
    }
)

// 3. Ajouter fonction navigation
fun navigateToTeacherDetail(teacherId: String) {
    _appState.value = _appState.value.copy(
        currentScreen = Screen.TeacherDetail(teacherId)
    )
}

// 4. Ajouter case dans AppNavigation
is Screen.TeacherDetail -> {
    val teacherId = (appState.currentScreen as Screen.TeacherDetail).teacherId
    TeacherDetailScreen(
        teacherId = teacherId,
        onBackClick = { appViewModel.navigateToTeachers() }
    )
}
```

---

## 📱 Gestion des états spéciaux

### Loading state
```kotlin
when {
    appState.isLoading -> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
```

### Error state
```kotlin
// Optionnel: ajouter à AppState
data class AppState(
    val error: String? = null,
    // ...
)

// Utiliser dans AppNavigation
if (appState.error != null) {
    ErrorDialog(
        message = appState.error,
        onDismiss = { appViewModel.clearError() }
    )
}
```

---

## 🎯 Bonnes pratiques

### ✅ DO
- Utiliser sealed classes pour les routes
- Garder la logique navigation centralisée dans AppViewModel
- Passer les viewmodels par paramètre
- Utiliser des callbacks pour naviguer

### ❌ DON'T
- Ne pas utiliser de strings pour les routes
- Ne pas accéder directement à currentScreen en bas-niveau
- Ne pas créer de NavController local dans les composables
- Ne pas mélanger navigation logic dans les ViewModels métier

---

## 🔄 Cycle de vie navigation

```
Utilisateur lance app
    ↓
AppViewModel init: checkOnboardingStatus()
    ↓
Si non complété → Affiche OnboardingScreen
Si complété, checkAuthStatus()
    ↓
Si logué → Affiche TeachersScreen
Si pas logué → Affiche LoginScreen
    ↓
Utilisateur clique bouton
    ↓
Callback appelé → navigateTo...()
    ↓
AppViewModel met à jour currentScreen
    ↓
AppNavigation re-compose avec le nouvel écran
    ↓
[Écran s'affiche]
```

---

## 🧪 Test de navigation

```kotlin
@Test
fun testNavigateToProfile() {
    // Arrange
    val viewModel = AppViewModel(...)
    
    // Act
    viewModel.navigateToProfile()
    
    // Assert
    assertEquals(
        Screen.Profile, 
        viewModel.appState.value.currentScreen
    )
}

@Test
fun testOnboardingCompleted() {
    // Arrange
    val viewModel = AppViewModel(...)
    
    // Act
    viewModel.completeOnboarding()
    
    // Assert
    assertEquals(
        true,
        viewModel.appState.value.isOnboardingCompleted
    )
}
```

---

## 📊 Graphique de navigation

```
                    ┌─────────────────┐
                    │  App Loading    │
                    │ (splash screen) │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │ Check Onboarding│
                    └────────┬────────┘
                             │
                ┌────────────┴────────────┐
                │                         │
         [Never]│                  [Seen] │
                │                         │
        ┌───────▼────────┐    ┌──────────▼──────┐
        │  OnboardingSeq │    │  Check Auth     │
        │  (3 pages)     │    │                 │
        └───────┬────────┘    └────────┬────────┘
                │                      │
                │ Complete      ┌──────┴────────┐
                │              │                │
                └──────────────►[Logué]  [Pas logué]
                               │                │
                        ┌──────▼──┐     ┌──────▼────┐
                        │Teachers │     │   Login   │
                        │ (main)  │     │           │
                        └────┬────┘     └──────┬────┘
                             │                 │
                             │ Click Profile   │ Auth Success
                             │                 │
                        ┌────▼────┐        ────┘
                        │ Profile  │           │
                        │          │      ┌────▼────────┐
                        └─────┬────┘      │  Teachers   │
                              │          │  (loaded)   │
                              └──────────►└─────────────┘
                                Back
```

---

## 🚀 Prochains ajouts (optionnel)

- [ ] Back navigation avec Android back button
- [ ] Deep linking support
- [ ] Animated transitions
- [ ] Navigation state restoration
- [ ] Bottom navigation tabs

---

**Navigation type-safe = Moins de bugs! 🎯**

