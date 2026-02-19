# Guide d'implémentation des Screens avec Navigation Compose

## Pattern à suivre pour chaque Screen

### ✅ Template correct

```kotlin
@Composable
fun TeachersScreen(
    viewModel: TeachersViewModel = koinViewModel(),
    onTeacherSelected: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // UI implementation
}
```

### ❌ À ÉVITER

```kotlin
@Composable
fun TeachersScreen() {
    val viewModel: TeachersViewModel = rememberKoinInstance()  // ❌ Non-standard
    // ...
}
```

---

## Signature des callbacks

### Pour la navigation en arrière

```kotlin
// ✅ CORRECT - Utiliser NavController dans NavigationHost
TeachersScreen(
    viewModel = viewModel,
    onTeacherSelected = { teacherId ->
        navController.navigate(Screen.TeacherDetail(teacherId))  // ← Navigation ici
    }
)
```

### Pour les actions métier

```kotlin
// ✅ CORRECT - Actions dans le ViewModel
Button(onClick = { viewModel.selectTeacher(teacherId) })

// ❌ INCORRECT - Navigation depuis le Screen
Button(onClick = { onTeacherSelected(teacherId) })  // Ne pas naviguer du Screen
```

---

## Refactorisations requises des Screens existants

### TeachersScreen.kt

```diff
@Composable
fun TeachersScreen(
-   viewModel: TeachersViewModel,
+   viewModel: TeachersViewModel = koinViewModel(),
    onTeacherSelected: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(...) {
        TeacherCard(
            teacher = teacher,
            onSelect = { onTeacherSelected(teacher.id) }  // ← Navigation del à NavigationHost
        )
    }
}
```

### TeacherDetailScreen.kt

```diff
@Composable
fun TeacherDetailScreen(
+   teacherId: String,  // ← Via toRoute()
-   teacher: Teacher,
-   viewModel: TeachersViewModel,
+   viewModel: TeachersViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onStartConversation: () -> Unit
) {
    // Récupérer le teacher via viewModel si nécessaire
    val teacher = viewModel.uiState.collectAsState().value.teachers
        .find { it.id == teacherId }
        ?: return  // Fallback si non trouvé
    
    // UI
}
```

### ProfileScreen.kt

```diff
@Composable
fun ProfileScreen(
-   viewModel: ProfileViewModel,
+   viewModel: ProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    // Implementation
}
```

---

## Architecture type pour les paramètres

### Cas 1: ID simple

```kotlin
@Serializable
data class TeacherDetail(val teacherId: String) : Screen()

// Navigation
navController.navigate(Screen.TeacherDetail("teacher-123"))

// Réception
composable<Screen.TeacherDetail> { backStackEntry ->
    val args: Screen.TeacherDetail = backStackEntry.toRoute()
    TeacherDetailScreen(teacherId = args.teacherId)
}
```

### Cas 2: Paramètres complexes

```kotlin
@Serializable
data class ChatScreen(
    val teacherId: String,
    val teacherName: String
) : Screen()

// Navigation
navController.navigate(Screen.ChatScreen(teacherId, teacherName))

// Réception
composable<Screen.ChatScreen> { backStackEntry ->
    val args: Screen.ChatScreen = backStackEntry.toRoute()
    ChatScreen(teacherId = args.teacherId, teacherName = args.teacherName)
}
```

### Cas 3: Éviter les objets complexes

```kotlin
// ❌ MAUVAIS - Teacher non sérializable
data class TeacherDetail(val teacher: Teacher) : Screen()

// ✅ BON - Passer juste l'ID
data class TeacherDetail(val teacherId: String) : Screen()
```

---

## Gestion du back stack

### Pop simple

```kotlin
Button(onClick = { navController.popBackStack() })
```

### Pop jusqu'à une destination

```kotlin
Button(onClick = {
    navController.popBackStack(Screen.Teachers, inclusive = false)
})
```

### Remplacer la stack

```kotlin
Button(onClick = {
    navController.navigate(Screen.Login) {
        popUpTo(Screen.Onboarding) { inclusive = true }
    }
})
```

---

## State management best practices

### ✅ Correct

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinViewModel()  // ← Koin injecte
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        when (uiState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Success -> SuccessScreen(uiState.data)
            is UiState.Error -> ErrorScreen(uiState.error)
        }
    }
}
```

### ❌ Incorrect

```kotlin
@Composable
fun MyScreen() {
    val (uiState, setUiState) = remember { mutableStateOf(...) }  // ❌ State local
}
```

---

## Checklist pour chaque Screen

- [ ] Utilise `viewModel = koinViewModel()`
- [ ] N'accepte que les callbacks pour navigation
- [ ] Utilise `collectAsState()` pour le state
- [ ] N'a pas de logique métier
- [ ] Commentée avec `@Composable`
- [ ] Pas d'imports Koin direct (`GlobalContext`)
- [ ] Pas de `remember { ... }`pour le ViewModel

---

## Ressources

- [Compose Navigation Doc](https://developer.android.com/guide/navigation)
- [MVVM Pattern](https://developer.android.com/jetpack/guide)
- [Koin Documentation](https://insert-koin.io/)


