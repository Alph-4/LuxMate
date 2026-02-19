# 🚀 Quick Start - Navigation moderne LuxMate

## En 2 minutes

### 1️⃣ La structure de navigation est déjà en place

```
App.kt (point d'entrée)
  ↓
AppContent() → Détermine la destination initiale
  ↓
NavigationHost() → Affiche l'écran courant
  ↓
Screens (Composables)
```

### 2️⃣ Pour naviguer d'un écran à un autre

```kotlin
// Dans TeachersScreen (callback)
onTeacherSelected = { teacherId ->
    onNavigate(Screen.TeacherDetail(teacherId))  // ← Passe au NavigationHost
}

// Dans NavigationHost
TeachersScreen(
    viewModel = viewModel,
    onTeacherSelected = { teacherId ->
        currentScreen.value = Screen.TeacherDetail(teacherId)  // ← Navigue
    }
)
```

### 3️⃣ Pour revenir en arrière

```kotlin
onBackClick = {
    onBack()  // ← Appelle la fonction onBack du NavigationHost
}
```

---

## Ajouter un nouvel écran

### Étape 1: Créer la route dans Screen.kt

```kotlin
@Serializable
data class ChatScreen(val teacherId: String) : Screen()
```

### Étape 2: Ajouter le composable dans NavigationHost.kt

```kotlin
is Screen.ChatScreen -> {
    val viewModel: ChatViewModel = koinViewModel()
    ChatScreen(
        viewModel = viewModel,
        teacherId = currentScreen.teacherId,
        onBackClick = { onBack() }
    )
}
```

### Étape 3: Naviguer vers cet écran

```kotlin
onStartConversation = {
    onNavigate(Screen.ChatScreen(teacherId))
}
```

### Étape 4: Enregistrer le ViewModel dans Koin (KoinModule.kt)

```kotlin
viewModel { ChatViewModel(get(), get()) }
```

C'est tout ! 🎉

---

## Commandes útiles

```bash
# Build le projet
./gradlew build

# Teste Android
./gradlew :composeApp:installDebug

# Lancer les tests
./gradlew test

# Clean complet
./gradlew clean build --refresh-dependencies
```

---

## Architecture en un coup d'œil

| Composant | Responsabilité |
|-----------|-----------------|
| **App.kt** | Entrée, thème |
| **AppViewModel** | Destination initiale |
| **NavigationHost** | Routes, injection VM |
| **Screens** | UI et logique présentation |
| **ViewModels** | Gestion d'état métier |
| **Use Cases** | Logique métier |
| **Repositories** | Accès aux données |

---

## Patterns à suivre

### ✅ Dans les Screens

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinViewModel(),  // ← Koin injecte
    onNavigate: (Screen) -> Unit = {},  // ← Callback de navigation
    onBack: () -> Unit = {}  // ← Callback retour
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        Button(onClick = { onNavigate(Screen.NextScreen) })
        Button(onClick = { onBack() })
    }
}
```

### ❌ À ÉVITER

```kotlin
// Ne pas créer le ViewModel manuellement
val viewModel = remember { MyViewModel() }  // ❌ NON

// Ne pas naviguer depuis le composable
navController.navigate(...)  // ❌ NON - Navigation via callback

// Ne pas utiliser directement Koin
GlobalContext.get().get<MyViewModel>()  // ❌ NON
```

---

## Fichiers clés à connaître

1. **App.kt** - Point d'entrée
2. **NavigationHost.kt** - Configuration des routes
3. **AppViewModel.kt** - Détermination destination initiale
4. **KoinModule.kt** - Injection de dépendances
5. **Screen.kt** - Définition des routes

---

## Débugguer la navigation

```kotlin
// Ajouter des logs
onNavigate = { newScreen ->
    println("Navigating to: $newScreen")
    // ... rest of code
}

// Afficher l'écran courant
Text("Current: ${currentScreen.value::class.simpleName}")
```

---

## FAQ

**Q: Comment passer des paramètres à un écran?**  
A: Via la data class de la route

```kotlin
data class TeacherDetail(val teacherId: String) : Screen()
// Puis dans le composable:
is Screen.TeacherDetail -> TeacherDetailScreen(teacherId = it.teacherId)
```

**Q: Pourquoi pas d'état de navigation dans le ViewModel?**  
A: Pour respecter la séparation des responsabilités. Le ViewModel gère l'état métier, la navigation est locale.

**Q: Comment tester la navigation?**  
A: Mockez les callbacks `onNavigate` et `onBack` dans vos tests.

**Q: Multiplatform, ça veut dire quoi?**  
A: Le code compile sur Android ET iOS sans modification.

---

## Ressources

- 📚 `NAVIGATION_GUIDE_UPDATED.md` - Guide détaillé
- 📚 `SCREENS_IMPLEMENTATION_GUIDE.md` - Template pour screens
- 📚 `IMPLEMENTATION_COMPLETE.md` - Résumé complet
- 📚 `CHANGES_SUMMARY.md` - Avant/après comparaison

---

**Status**: ✅ Prêt à l'emploi  
**Qualité**: ⭐⭐⭐⭐⭐ Production  
**Support**: Multiplatform (Android + iOS)

