# Comprehensive Implementation Plan: Android TV TubeArchivist Client

## Project Overview

Build a YouTube TV-like Android TV application that streams archived content from a self-hosted TubeArchivist backend (ta.vishalk.com), designed for young children (ages 1-5) with familiar YouTube UI/UX and invisible parental controls.

**Key Finding**: The repository at https://github.com/vishalkdotcom/Youtube-Clone does not exist. This plan provides a complete from-scratch implementation using modern Android TV best practices.

---

## Architecture Overview

### Technology Stack

**Frontend (Android TV)**
- **UI Framework**: Jetpack Compose for TV (androidx.tv:tv-material:1.0.0+)
  - ✅ Modern, declarative UI
  - ✅ 90% less code than deprecated Leanback
  - ✅ Highly customizable for YouTube-like layouts
- **Video Player**: Media3 ExoPlayer (androidx.media3:media3-exoplayer:1.3.1)
- **Navigation**: Compose Navigation for TV
- **Image Loading**: Coil for Compose
- **Language**: Kotlin

**Architecture Pattern**: Clean Architecture with MVVM
```
UI Layer (Compose) → ViewModel (StateFlow) → UseCase → Repository → API/DataSource
```

**Backend Integration**
- **API**: TubeArchivist REST API at ta.vishalk.com
- **Networking**: Ktor Client 3.0+ (coroutine-based, multiplatform-ready)
- **Serialization**: kotlinx.serialization
- **Authentication**: Token-based (stored in EncryptedSharedPreferences)

**Data Layer**
- **Preferences**: Jetpack DataStore
- **Database**: Room (for offline caching, watch history)
- **Dependency Injection**: Koin 3.6+

**Kotlin Multiplatform Consideration**: While KMP can share 70-80% of business logic, for initial Android TV-only focus, use standard Android project with modular architecture that could be migrated to KMP later if mobile support is needed.

---

## Project Structure

```
TubeArchivistTV/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/tubearchivist/tv/
│   │   │   ├── data/
│   │   │   │   ├── api/
│   │   │   │   │   ├── TubeArchivistApi.kt
│   │   │   │   │   ├── AuthInterceptor.kt
│   │   │   │   │   └── models/ (Video, Channel, Playlist DTOs)
│   │   │   │   ├── repository/
│   │   │   │   │   ├── VideoRepository.kt
│   │   │   │   │   ├── ChannelRepository.kt
│   │   │   │   │   └── PlaylistRepository.kt
│   │   │   │   ├── local/
│   │   │   │   │   ├── database/ (Room entities, DAOs)
│   │   │   │   │   └── preferences/PreferencesManager.kt
│   │   │   │   └── mapper/ (DTO to Domain)
│   │   │   ├── domain/
│   │   │   │   ├── model/ (Video, Channel, Playlist domain models)
│   │   │   │   └── usecase/
│   │   │   │       ├── GetVideosUseCase.kt
│   │   │   │       ├── SearchVideosUseCase.kt
│   │   │   │       ├── GetChannelsUseCase.kt
│   │   │   │       └── TrackWatchProgressUseCase.kt
│   │   │   ├── presentation/
│   │   │   │   ├── home/
│   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   ├── HomeViewModel.kt
│   │   │   │   │   └── HomeUiState.kt
│   │   │   │   ├── player/
│   │   │   │   │   ├── PlayerScreen.kt
│   │   │   │   │   ├── PlayerViewModel.kt
│   │   │   │   │   └── PlayerControls.kt
│   │   │   │   ├── search/
│   │   │   │   │   ├── SearchScreen.kt
│   │   │   │   │   └── SearchViewModel.kt
│   │   │   │   ├── channels/
│   │   │   │   │   ├── ChannelsScreen.kt
│   │   │   │   │   └── ChannelDetailScreen.kt
│   │   │   │   ├── playlists/
│   │   │   │   │   └── PlaylistsScreen.kt
│   │   │   │   ├── parental/
│   │   │   │   │   ├── ParentalControlManager.kt
│   │   │   │   │   ├── PinDialog.kt
│   │   │   │   │   └── ScreenTimeTracker.kt
│   │   │   │   ├── components/ (Reusable UI)
│   │   │   │   │   ├── VideoCard.kt
│   │   │   │   │   ├── ChannelCard.kt
│   │   │   │   │   ├── ContentRow.kt
│   │   │   │   │   └── HeroCarousel.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Type.kt
│   │   │   │       └── Theme.kt
│   │   │   ├── di/ (Koin modules)
│   │   │   │   ├── NetworkModule.kt
│   │   │   │   ├── RepositoryModule.kt
│   │   │   │   ├── ViewModelModule.kt
│   │   │   │   └── DatabaseModule.kt
│   │   │   ├── navigation/
│   │   │   │   └── NavGraph.kt
│   │   │   └── TubeArchivistApp.kt
│   │   └── res/
│   │       ├── drawable/ (icons, placeholders)
│   │       ├── values/
│   │       │   ├── colors.xml
│   │       │   ├── strings.xml
│   │       │   └── dimens.xml
│   │       └── xml/
│   │           └── network_security_config.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Phase 1: Foundation Setup

### 1.1 Project Configuration

**build.gradle.kts (project level)**
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
}
```

**build.gradle.kts (app level)**
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tubearchivist.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tubearchivist.tv"
        minSdk = 21  // Android TV 5.0+
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Compose for TV
    val composeBom = platform("androidx.compose:compose-bom:2025.10.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.tv:tv-material:1.0.0")
    implementation("androidx.tv:tv-foundation:1.0.0")
    implementation("androidx.activity:activity-compose:1.11.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    // Ktor for networking
    implementation("io.ktor:ktor-client-core:3.0.0")
    implementation("io.ktor:ktor-client-okhttp:3.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
    implementation("io.ktor:ktor-client-logging:3.0.0")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Lifecycle + ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Koin DI
    implementation("io.insert-koin:koin-android:3.6.0")
    implementation("io.insert-koin:koin-androidx-compose:3.6.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Coil for images
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Security (Encrypted preferences)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:1.0.0")
}
```

### 1.2 Manifest Configuration

**AndroidManifest.xml**
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- TV Permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-feature android:name="android.hardware.touchscreen" android:required="false" />
    <uses-feature android:name="android.software.leanback" android:required="true" />

    <application
        android:name=".TubeArchivistApp"
        android:allowBackup="true"
        android:banner="@drawable/app_banner"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.TubeArchivistTV"
        android:networkSecurityConfig="@xml/network_security_config">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:screenOrientation="landscape"
            android:configChanges="keyboard|keyboardHidden|navigation">

            <!-- TV Launcher Intent -->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**res/xml/network_security_config.xml** (for custom domain)
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">ta.vishalk.com</domain>
    </domain-config>
</network-security-config>
```

---

## Implementation Timeline

**Week 1-2**: Foundation
- Project setup, dependencies
- Data layer (API client, repositories, database)
- Koin DI modules

**Week 3-4**: Core UI
- Theme and design system
- Home screen with video browsing
- Video cards and content rows
- Hero carousel

**Week 5-6**: Video Playback
- ExoPlayer integration
- Player UI and controls
- Watch progress tracking

**Week 7**: Search & Navigation
- Search screen
- Navigation graph
- Channel and playlist screens

**Week 8**: Child-Friendly Features
- Large touch targets
- Visual feedback enhancements
- Accidental exit prevention
- UI polish for kids

**Week 9**: Parental Controls
- PIN system
- Screen time tracking
- Hidden settings access
- Time limit enforcement

**Week 10**: Testing & Polish
- Unit tests
- UI tests
- Performance optimization
- Bug fixes

**Week 11**: Deployment
- Testing on real hardware
- APK signing
- Documentation

---

## Key Recommendations

### 1. **Start with Android TV Only**
- Don't use Kotlin Multiplatform initially
- Focus on solid TV experience first
- Architecture allows KMP migration later if needed

### 2. **Use Jetpack Compose for TV**
- Leanback is deprecated
- Compose for TV is modern and flexible
- 90% less code than Leanback

### 3. **TubeArchivist Integration Notes**
- May need to disable `DISABLE_STATIC_AUTH` on server for video streaming
- Direct MP4 URLs work with ExoPlayer
- Token-based auth for API, handle in OkHttp interceptor

### 4. **Child-Friendly Best Practices**
- Extra-large UI elements (260dp cards vs 240dp)
- 1.15x focus scale (vs 1.1x for adults)
- Gold color (#FFD700) for focus (friendly, bright)
- 3-press back button exit (prevent accidents)

### 5. **Parental Controls Strategy**
- Hidden via 5-click Easter egg (invisible to kids)
- Encrypted PIN storage
- Screen time tracked locally
- Time limits enforced at playback start

---

## Conclusion

This implementation plan provides a complete, production-ready architecture for an Android TV app that:

✅ **Mimics YouTube TV UI** with Compose for TV components
✅ **Integrates TubeArchivist API** with Ktor networking
✅ **Child-friendly design** with large targets, strong feedback
✅ **Invisible parental controls** with PIN and screen time limits
✅ **Modern architecture** using MVVM, Clean Architecture, Koin DI
✅ **Optimal for young children** with familiar YouTube interface

The existing GitHub repository doesn't exist at the specified URL, but this plan provides a complete from-scratch implementation using 2025 best practices. The architecture is modular and allows future expansion to Android mobile or KMP if needed.

Estimated development time: **10-11 weeks** for full implementation with all features.

---

## API Configuration

**TubeArchivist API Details:**
- **Base URL**: https://ta.vishalk.com
- **API Token**: ddb865bf6f8970f8b52283a09f939316eb17c66d
- **Authentication**: Token-based (Header: `Authorization: Token <token>`)
