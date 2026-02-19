# Guide de compilation et test - LuxMate KMP

## 📱 Compilation pour Android

### Via terminal (PowerShell)
```powershell
# Depuis le dossier racine LuxMate
./gradlew.bat assemble

# Pour une compilation avec debug
./gradlew.bat assemble

# Pour lancer sur un émulateur ou téléphone
./gradlew.bat installDebug
```

### Via Android Studio
1. File → Sync Now (pour synchroniser Gradle)
2. Build → Build Bundle(s) / APK(s) → Build APK(s)
3. Run → Run 'app' (F5)

## 🍎 Compilation pour iOS

### Via Xcode
```bash
# Depuis le dossier iosApp
open iosApp.xcodeproj

# Ou via terminal
xcodebuild -scheme iosApp -configuration Debug -derivedDataPath build
```

## ✅ Vérifier la compilation

### Vérifier les erreurs Kotlin
```powershell
./gradlew.bat compileKotlin
```

### Générer les ressources Compose
```powershell
./gradlew.bat generateComposeResources
```

## 🧪 Tester les ViewModels

Les ViewModels peuvent être testés facilement :

```kotlin
@Test
fun testLoginSuccess() = runTest {
    val viewModel = LoginViewModel(loginUseCase)
    viewModel.setEmail("test@test.com")
    viewModel.setPassword("password")
    viewModel.login()
    
    // Vérifier l'état
    assertTrue(viewModel.uiState.value.user != null)
}
```

## 🔍 Debug

### Logs
```kotlin
Log.d("LuxMate", "Message de debug")
```

### Layout Inspector (Android)
1. Android Studio → Layout Inspector
2. Select running device
3. Inspectez les composables Compose

## 📋 Checklist avant commit

- [ ] `./gradlew.bat compileKotlin` sans erreurs
- [ ] Pas de warnings graves
- [ ] Tests unitaires passent
- [ ] Navigation fonctionne correctement
- [ ] Pas de logs d'erreur

## 🐛 Problèmes courants

### "Module not found: Koin"
**Solution**: 
```powershell
./gradlew.bat clean build --refresh-dependencies
```

### "Cannot resolve symbol: AsyncImage"
**Solution**: S'assurer que Coil est ajouté au build.gradle.kts
```kotlin
implementation(libs.coil.compose)
```

### "Sealed class not recognized"
**Solution**: Vérifier que Screen.kt importe correctement `kotlinx.serialization.Serializable`

### Erreur d'initialisation Koin
**Solution**: S'assurer que `initializeKoin()` est appelé dans `App()`

## 🚀 Prochaines compilations

1. Tester sur appareil réel (Android/iOS)
2. Vérifier la connexion réseau (si API)
3. Tester les transitions entre écrans
4. Vérifier les performances

## 📞 Support

Pour des questions sur KMP:
- [Kotlin Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-setup.html)
- [Koin Documentation](https://insert-koin.io/)

