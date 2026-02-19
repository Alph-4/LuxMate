# Architecture Navigation Compose avec Koin - LuxMate

## Vue d'ensemble

Ce projet utilise **Jetpack Navigation Compose** avec **Koin** pour l'injection de dépendances, en suivant les meilleures pratiques Android officielles de 2024-2026.

## Architecture

```
App.kt (Point d'entrée)
    ↓
AppContent() - Gère l'état de chargement
    ↓
NavigationHost() - Gère la navigation avec NavController
    ↓
Composables (Screens)
    ↓
ViewModels (Koin)
    ↓
Use Cases / Repositories
```

## Flux de navigation

### 1. **Démarrage de l'app**

```
App()
├─ initializeKoin() - Initialise le conteneur Koin
├─ LuxMateAppTheme - Applique le thème
└─ AppContent()
   ├─ AppViewModel - Détermine la destination initiale
   ├─ rememberNavController() - Crée le NavController
   └─ NavigationHost() - Lance la navigation
```

### 2. **Déterminant la destination initiale**

**AppViewModel.kt** vérifie l'état d'onboarding :
- ✅ Onboarding complété → Affiche `Screen.Login`
- ❌ Onboarding incomplété → Affiche `Screen.Onboarding`

### 3. **Navigation entre écrans**

Chaque route est définie en tant que **Sealed Class Serializable** :

```kotlin
@Serializable
sealed class Screen {
    @Serializable data object Onboarding : Screen()
    @Serializable data object Login : Screen()
    @Serializable data object Teachers : Screen()
    @Serializable data object Profile : Screen()
    @Serializable data class TeacherDetail(val teacherId: String) : Screen()
}
```

### 4. **Navigation d'un écran à un autre**

```kotlin
// Dans TeachersScreen
onTeacherSelected = { teacherId ->
    navController.navigate(Screen.TeacherDetail(teacherId))
}

// Revenir à l'écran précédent
onBackClick = {
    navController.popBackStack()
}
```

## Gestion des ViewModels avec Koin

### Déclaration dans KoinModule.kt

```kotlin
// ✅ BON - ViewModels avec viewModel()
viewModel { AppViewModel(get()) }
viewModel { TeachersViewModel(get(), get()) }

// ❌ MAUVAIS - Ne pas utiliser single pour les ViewModels
// single { TeachersViewModel(get(), get()) }
```

### Utilisation dans les Composables

```kotlin
@Composable
fun TeachersScreen() {
    // Koin injecte automatiquement et respecte le cycle de vie
    val viewModel: TeachersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    // ...
}
```

## Bonnes pratiques implémentées

### ✅ 1. **NavController pour la navigation**
- Navigation décentralisée (pas de ViewModel qui gère la navigation)
- Gestion automatique du back stack
- Routes typées avec Serializable

### ✅ 2. **Koin pour l'injection**
- `single` pour repositories et use cases
- `viewModel()` pour les ViewModels
- Inversion de dépendances

### ✅ 3. **Séparation des responsabilités**

| Composant | Responsabilité |
|-----------|-----------------|
| **App.kt** | Point d'entrée, thème |
| **AppViewModel** | Détermine la destination initiale |
| **NavigationHost** | Gère les routes et transitions |
| **Screens (Composables)** | UI et logique de présentation |
| **ViewModels** | Gestion d'état métier |
| **Use Cases** | Logique métier |
| **Repositories** | Accès aux données |

### ✅ 4. **Cycle de vie des ViewModels**
Les ViewModels sont créés/détruits avec les écrans grâce à `viewModel()`:
- Pas de fuites mémoire
- État conservé lors de recompositions
- Détruit quand l'écran est quitté

## Exemples de code

### Naviguer avec paramètres

```kotlin
// Navigation
navController.navigate(Screen.TeacherDetail("teacher-123"))

// Réception
composable<Screen.TeacherDetail> { backStackEntry ->
    val teacherDetail: Screen.TeacherDetail = backStackEntry.toRoute()
    TeacherDetailScreen(teacherId = teacherDetail.teacherId)
}
```

### Pop-up avec inclusive

```kotlin
// Supprimer l'historique jusqu'à un point
navController.navigate(Screen.Teachers) {
    popUpTo(Screen.Login) { inclusive = true }
}
```

### Retour arrière

```kotlin
onBackClick = {
    navController.popBackStack()
}
```

## Dépendances utilisées

```toml
# Navigation
androidx-navigation-compose = "2.9.7"

# Koin
koin-core = "4.1.1"
koin-compose = "4.1.1"

# ViewModels
androidx-lifecycle-viewmodelCompose = "2.9.6"
```

## Avantages de cette architecture

1. **🎯 Type-safe** : Routes compilées au moment de la compilation
2. **🔄 Cycle de vie correct** : ViewModels gérés par Compose
3. **🧹 Pas de fuites mémoire** : Nettoyage automatique
4. **📦 Testable** : Injection de dépendances facilite les tests
5. **🚀 Performance** : Pas de recompositions inutiles
6. **📱 Multiplateforme** : Compatible iOS et Android
7. **✨ Maintenable** : Code clair et documenté

## Fichiers clés

| Fichier | Rôle |
|---------|------|
| `App.kt` | Point d'entrée |
| `AppViewModel.kt` | Logique de démarrage |
| `NavigationHost.kt` | Configuration des routes |
| `KoinModule.kt` | Injection de dépendances |
| `Screen.kt` | Définition des routes |

---

**Dernière mise à jour** : Février 2026  
**Basé sur** : Meilleures pratiques Android officielles

