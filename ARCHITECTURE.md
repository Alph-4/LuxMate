# LuxMate - Architecture Clean et Structure KMP

## 📋 Vue d'ensemble

LuxMate est une application Kotlin Multiplatform (KMP) utilisant une **clean architecture** avec 3 couches principales :

```
Presentation Layer (UI + ViewModels)
    ↓
Domain Layer (Entities + Use Cases + Repository Interfaces)
    ↓
Data Layer (Repository Implementations + Data Sources)
```

## 🏗️ Structure du projet

```
composeApp/src/commonMain/kotlin/org/julienjnnqin/luxmateapp/
├── core/
│   └── theme/
│       ├── Color.kt          # Palette de couleurs
│       ├── Typography.kt      # Styles de texte
│       └── Theme.kt           # Configuration Material3
├── data/
│   └── repository/
│       ├── AuthRepositoryImpl.kt
│       ├── OnboardingRepositoryImpl.kt
│       ├── TeacherRepositoryImpl.kt
│       └── UserRepositoryImpl.kt
├── domain/
│   ├── entity/
│   │   └── Entities.kt        # User, Teacher, ChatHistory, OnboardingState
│   ├── repository/
│   │   └── Repositories.kt    # Repository Interfaces
│   └── usecase/
│       └── UseCases.kt        # All Use Cases
├── di/
│   └── KoinModule.kt          # Dependency Injection Setup
├── presentation/
│   ├── AppViewModel.kt        # Main App State Management
│   ├── components/
│   │   └── Button.kt          # Reusable Components
│   ├── navigation/
│   │   └── Screen.kt          # Navigation Routes
│   └── screen/
│       ├── onboarding/
│       │   ├── OnboardingScreen.kt
│       │   └── OnboardingViewModel.kt
│       ├── auth/
│       │   ├── LoginScreen.kt
│       │   └── LoginViewModel.kt
│       ├── teachers/
│       │   ├── TeachersScreen.kt
│       │   └── TeachersViewModel.kt
│       └── profile/
│           ├── ProfileScreen.kt
│           └── ProfileViewModel.kt
└── App.kt                      # App Root + Navigation
```

## 🎨 Design System

### Couleurs principales
- **Primary Dark**: `#1e398a` (Onboarding/Login)
- **Primary Light**: `#3968f5` (Teachers/Profile)
- **Success Green**: `#22c55e` (Actions positives)
- **Background Light**: `#f6f6f8`
- **Background Dark**: `#121620`

### Typographie
- Police: **Inter**
- Styles adaptés Material3: Display, Headline, Title, Body, Label

## 🔄 Architecture Layers

### 1️⃣ Domain Layer (Pure Kotlin)
- **Entities**: Classes immuables représentant le domaine métier
- **Repository Interfaces**: Contrats pour accéder aux données
- **Use Cases**: Logique métier encapsulée

### 2️⃣ Data Layer (Implementation)
- **Repositories Impl**: Implémentations des interfaces
- **Data Sources**: Accès aux données (API, LocalStorage, Mock)
- **Models**: Objets pour la sérialisation

### 3️⃣ Presentation Layer (UI + State)
- **Screens**: Composables Jetpack Compose
- **ViewModels**: Gestion d'état avec StateFlow
- **Components**: Composables réutilisables
- **Navigation**: Gestion des routes avec Sealed classes

## 🚀 Écrans implémentés

### 1. Onboarding (3 pages)
- Page 1: Bienvenue
- Page 2: Des professeurs 24h/7
- Page 3: Progressez à votre rythme
- Navigation avec indicateurs de progression

### 2. Login
- Champs Email/Password
- Validation basique
- Mock authentication

### 3. Teachers List
- Filtrage par catégorie
- Liste des professeurs avec avatar
- Indicateur de disponibilité (online/offline)
- Statut rating

### 4. User Profile
- Avatar de l'utilisateur
- Historique des conversations
- Informations du profil

## 💉 Injection de dépendances

Utilise **Koin** pour l'IoC avec un module centralisé :

```kotlin
val appModule = module {
    // Repositories
    single<OnboardingRepository> { OnboardingRepositoryImpl() }
    single<AuthRepository> { AuthRepositoryImpl() }
    
    // Use Cases
    single { CheckOnboardingCompletedUseCase(get()) }
    single { LoginUseCase(get()) }
    
    // ViewModels
    single { OnboardingViewModel(get()) }
    single { LoginViewModel(get()) }
}
```

## 🔄 State Management Pattern

Chaque ViewModel utilise un pattern **unidirectionnel**:

```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null
)

class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    // UI observes the state
    val uiState.collectAsState().value
}
```

## 🌐 Navigation

Navigation type-safe utilisant `sealed class`:

```kotlin
sealed class Screen {
    data object Onboarding : Screen()
    data object Login : Screen()
    data object Teachers : Screen()
    data object Profile : Screen()
}
```

Gérée par `AppViewModel` via `AppState`.

## 📦 Dépendances principales

```toml
[versions]
androidx-lifecycle = "2.9.6"
androidx-navigation = "2.8.2"
androidx-datastore = "1.1.1"
coil = "3.0.0-rc01"
koin = "3.5.6"
kotlin = "2.3.0"
kotlinx-serialization = "1.7.1"
composeMultiplatform = "1.10.0"
material3 = "1.10.0-alpha05"
```

## 🛠️ Mise en route

### 1. Initialiser Koin
```kotlin
fun App() {
    initializeKoin()
    // ...
}
```

### 2. Utiliser les ViewModels
```kotlin
@Composable
fun MyScreen() {
    val viewModel: MyViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
}
```

### 3. Naviguer
```kotlin
val appViewModel: AppViewModel = koinViewModel()
appViewModel.navigateToTeachers()
```

## ✅ Bonnes pratiques

✔️ **Séparation des responsabilités**: Chaque couche a un rôle distinct
✔️ **Testabilité**: Repository interfaces facilitent les tests
✔️ **Réutilisabilité**: Code métier dans Domain, partageable
✔️ **Type-safety**: Navigation avec sealed classes, pas de strings
✔️ **Immuabilité**: Data classes avec copy() pour l'état
✔️ **Coroutines**: ViewModels avec viewModelScope
✔️ **Compose**: Fonction composables pures et prévisibles

## 🚧 Prochaines étapes

- [ ] Intégrer une API réelle (Ktor Client)
- [ ] Ajouter la persistance avec DataStore
- [ ] Implémenter l'authentification JWT
- [ ] Ajouter les tests unitaires
- [ ] Détail du professeur (Teacher Detail Screen)
- [ ] Chat avec le professeur
- [ ] Animations de transition

## 📝 Notes

- Toute la logique métier est en `commonMain` (partagée iOS/Android)
- Les repositories utilisent actuellement des mocks (easy to replace)
- Material3 theming fonctionne automatiquement en light/dark mode
- Coil3 gère les images réseau de manière efficace

