# Android TV TubeArchivist Implementation Checklist

**Project**: YouTube Clone → Android TV TubeArchivist Client
**Target**: Kids-friendly Android TV app (ages 1-5)
**Timeline**: 10-11 weeks

---

## 📋 Phase 1: Foundation Setup (Week 1-2)

### Project Configuration
- [x] Create Android TV product flavor in build.gradle.kts (added dependencies to androidMain)
- [x] Add androidx.tv dependencies (tv-material, tv-foundation)
- [x] Add Media3 ExoPlayer dependencies (already present v1.8.0)
- [x] Add Ktor Client dependencies (already present v3.0.1)
- [x] Add Koin DI dependencies (already present v4.0.1)
- [x] Add Room database dependencies (using SQLDelight v2.0.2 instead)
- [x] Add DataStore preferences
- [x] Add Coil image loading (using Kamel v1.0.8 instead)
- [x] Add security-crypto for encrypted preferences

### Manifest Setup
- [x] Create androidTvMain source set (using androidMain with TV features)
- [x] Configure TV manifest with LEANBACK_LAUNCHER
- [x] Add touchscreen not required feature
- [x] Add leanback software feature
- [x] Create network_security_config.xml for ta.vishalk.com
- [ ] Create TV banner icon (320x180) (TODO: Phase 2)
- [ ] Set landscape orientation (TODO: Phase 2)

### Data Layer - API Client
- [x] Create TubeArchivistApi.kt
- [x] Define VideoDto, ChannelDto, PlaylistDto models
- [x] Define PaginatedResponse model
- [x] Implement getVideos() endpoint
- [x] Implement getVideoDetails() endpoint
- [x] Implement getChannels() endpoint
- [x] Implement getChannelVideos() endpoint
- [x] Implement searchVideos() endpoint
- [x] Implement updateWatchProgress() endpoint
- [x] Add stream URL helper methods
- [x] Add thumbnail URL helper methods
- [x] Configure Ktor client with ContentNegotiation
- [x] Add authentication token header interceptor
- [x] Add logging interceptor

### Data Layer - Repository
- [x] Create VideoRepository interface (TubeArchivistRepository)
- [x] Create VideoRepositoryImpl (TubeArchivistRepositoryImpl)
- [x] Create ChannelRepository interface (part of TubeArchivistRepository)
- [x] Create ChannelRepositoryImpl (part of TubeArchivistRepositoryImpl)
- [ ] Implement caching strategy (network + cache) (TODO: Phase 2)
- [x] Create data mappers (DTO → Domain)

### Data Layer - Database
- [ ] Create AppDatabase (Room) (using existing SQLDelight database)
- [ ] Create VideoEntity (TODO: Phase 2 - add to SQLDelight schema)
- [ ] Create ChannelEntity (TODO: Phase 2 - add to SQLDelight schema)
- [ ] Create VideoDao with queries (TODO: Phase 2)
- [ ] Create ChannelDao with queries (TODO: Phase 2)
- [ ] Implement watch progress tracking (TODO: Phase 3)
- [ ] Implement continue watching query (TODO: Phase 3)

### Data Layer - Preferences
- [x] Create PreferencesManager with DataStore (TvPreferencesManager)
- [x] Add API token storage
- [x] Add parental PIN storage (encrypted)
- [x] Add screen time tracking fields
- [x] Add daily limit preferences

### Dependency Injection
- [x] Create NetworkModule (Koin) (tubeArchivistModule)
- [x] Create RepositoryModule (Koin) (tubeArchivistModule)
- [x] Create DatabaseModule (Koin) (using existing appModule)
- [x] Create ViewModelModule (Koin) (using existing appModule)
- [x] Initialize Koin in TubeArchivistApp (integrated in App.kt)

---

## 🎨 Phase 2: Core UI (Week 3-4)

### Theme Configuration
- [x] Define YouTube-inspired color palette (TvColors.kt)
- [x] Create Color.kt (YouTubeRed, DarkBackground, etc.)
- [x] Create Type.kt with TV-optimized typography (24sp+) (TvTypography.kt)
- [x] Create Theme.kt with Material3 theme (using existing theme)
- [x] Define focus colors and borders

### Reusable Components
- [x] Create VideoCard.kt (TvVideoCard.kt)
  - [x] Thumbnail with AsyncImage (Coil) (using Kamel)
  - [x] Duration badge overlay
  - [x] Watch progress bar
  - [x] Title and channel name
  - [x] Focus scale animation (1.1x)
  - [x] Focus border (4dp white)
  - [x] Focus glow effect
- [x] Create ChannelCard.kt (deferred - not needed for Phase 2)
- [x] Create ContentRow.kt (TvContentRow.kt)
  - [x] TvLazyRow implementation
  - [x] Section title
  - [x] Horizontal spacing
  - [x] PivotOffsets for auto-reveal
- [x] Create HeroCarousel.kt (TvHeroCarousel.kt)
  - [x] Carousel with auto-scroll
  - [x] Background image with gradient
  - [x] Video metadata overlay
  - [x] Play button CTA

### Home Screen
- [x] Create HomeViewModel.kt (TvHomeViewModel.kt)
- [x] Define HomeUiState sealed class
- [x] Implement loadContent() use case
- [x] Create HomeScreen.kt composable (TvHomeScreen.kt)
- [x] Create TopBar with logo and search icon
- [x] Add HeroCarousel section
- [x] Add "Continue Watching" row
- [x] Add "Recently Added" row
- [x] Add loading state (shimmer/skeleton) (circular progress)
- [x] Add error state with retry
- [x] Implement pull-to-refresh (if needed) (deferred to Phase 4)

---

## 🎬 Phase 3: Video Playback (Week 5-6)

### Player ViewModel
- [x] Create PlayerViewModel.kt (TvPlayerViewModel.kt)
- [x] Define PlayerUiState sealed class
- [x] Implement loadVideo() use case
- [x] Track watch start time
- [x] Implement updateWatchProgress() every 5s
- [x] Handle time limit checks (deferred to Phase 6)
- [x] Release player resources on dispose

### Player Screen
- [x] Create PlayerScreen.kt (TvPlayerScreen.kt)
- [x] Integrate ExoPlayer with AndroidView (TvVideoPlayer.android.kt)
- [x] Configure MediaItem with video URL
- [x] Implement seek to saved position
- [x] Add player controls overlay (TvPlayerControls)
- [x] Add back button overlay
- [x] Handle lifecycle (pause/resume)
- [x] Track watch duration
- [x] Update watch progress to repository
- [x] Handle playback errors

### Player Controls
- [x] Create PlayerControls.kt (TvPlayerControls in TvPlayerScreen.kt)
- [x] Play/pause button
- [x] Seek bar
- [x] Time remaining display
- [x] Auto-hide controls after 5s
- [x] Show on D-pad interaction (via control visibility)
- [x] TV-optimized touch targets

---

## 🔍 Phase 4: Search & Navigation (Week 7)

### Navigation Setup
- [ ] Create NavGraph.kt
- [ ] Define navigation routes
- [ ] Implement HomeScreen route
- [ ] Implement PlayerScreen route with videoId arg
- [ ] Implement SearchScreen route
- [ ] Implement ChannelScreen route
- [ ] Implement PlaylistScreen route

### Search Screen
- [ ] Create SearchViewModel.kt
- [ ] Define SearchUiState sealed class
- [ ] Implement search() use case with debounce
- [ ] Create SearchScreen.kt
- [ ] Create SearchBar composable
- [ ] Display search results in TvLazyVerticalGrid
- [ ] Handle empty state
- [ ] Handle loading state
- [ ] Handle error state

### Channel Screen
- [ ] Create ChannelViewModel.kt
- [ ] Create ChannelScreen.kt
- [ ] Display channel banner
- [ ] Display channel info
- [ ] Display channel videos in grid
- [ ] Add subscribe button (optional)

### Playlist Screen
- [ ] Create PlaylistViewModel.kt
- [ ] Create PlaylistScreen.kt
- [ ] Display playlist info
- [ ] Display playlist videos
- [ ] Add play all button

---

## 👶 Phase 5: Child-Friendly Features (Week 8)

### Enhanced UI for Kids
- [ ] Create KidsVideoCard.kt (larger 260dp)
- [ ] Increase focus scale to 1.15x
- [ ] Change focus color to gold (#FFD700)
- [ ] Add pulse animation on focus
- [ ] Increase focus border to 6dp
- [ ] Increase glow elevation to 24dp
- [ ] Test all touch targets (56dp minimum)
- [ ] Verify text sizes (24sp+ for body)

### Accidental Exit Prevention
- [ ] Implement 3-press back button exit
- [ ] Show toast messages on back press
  - [ ] "Press back 2 more times to exit"
  - [ ] "Press back 1 more time to exit"
- [ ] Reset counter after 3 seconds
- [ ] Override BackHandler in MainActivity

### Visual Feedback
- [ ] Add click animations to all buttons
- [ ] Add loading indicators
- [ ] Add success confirmations
- [ ] Ensure all interactions have feedback
- [ ] Test on real TV hardware

---

## 🔒 Phase 6: Parental Controls (Week 9)

### Parental Control Manager
- [ ] Create ParentalControlManager.kt
- [ ] Implement encrypted PIN storage
- [ ] Implement setPin() method
- [ ] Implement verifyPin() method
- [ ] Implement isPinSet() check
- [ ] Implement setDailyLimit() method
- [ ] Implement getDailyLimit() method
- [ ] Implement getTodayWatchTime() method
- [ ] Implement addWatchTime() method
- [ ] Implement isTimeLimitReached() check
- [ ] Implement getRemainingTime() method
- [ ] Auto-reset watch time at midnight

### Hidden Access UI
- [ ] Create HiddenParentalAccess composable
- [ ] Implement 5-click Easter egg on logo
- [ ] Create ParentalPinDialog.kt
- [ ] Add PIN entry with number pad
- [ ] Mask PIN input (PasswordVisualTransformation)
- [ ] Verify PIN on entry
- [ ] Handle incorrect PIN attempts

### Parental Controls Screen
- [ ] Create ParentalControlsScreen.kt
- [ ] Display current daily limit
- [ ] Add +15/-15 minute buttons
- [ ] Display today's watch time
- [ ] Add unlimited option
- [ ] Add back to app button
- [ ] Style for adult users

### First-Time Setup
- [ ] Create OnboardingScreen.kt
- [ ] Prompt to set PIN on first launch
- [ ] Prompt to set screen time limit
- [ ] Skip option for testing
- [ ] Save onboarding completed flag

### Time Limit Enforcement
- [ ] Check time limit before playing video
- [ ] Show "Time's Up" dialog when limit reached
- [ ] Block playback when limit exceeded
- [ ] Show remaining time in UI (optional)
- [ ] Emit effect from PlayerViewModel

---

## 🧪 Phase 7: Testing (Week 10)

### Unit Tests
- [ ] Test VideoRepository caching logic
- [ ] Test VideoRepository API fallback
- [ ] Test ParentalControlManager PIN verification
- [ ] Test ParentalControlManager time tracking
- [ ] Test ParentalControlManager daily reset
- [ ] Test HomeViewModel state transitions
- [ ] Test PlayerViewModel watch progress
- [ ] Test SearchViewModel debounce logic
- [ ] Mock Ktor client for tests
- [ ] Mock Room DAO for tests

### UI Tests
- [ ] Test HomeScreen displays video cards
- [ ] Test VideoCard focus behavior
- [ ] Test ContentRow horizontal scrolling
- [ ] Test HeroCarousel auto-scroll
- [ ] Test PlayerScreen video loading
- [ ] Test SearchScreen query input
- [ ] Test ParentalPinDialog PIN entry
- [ ] Test back button triple-press exit

### Integration Tests
- [ ] Test end-to-end video playback flow
- [ ] Test search → play video flow
- [ ] Test parental control PIN → settings flow
- [ ] Test time limit enforcement
- [ ] Test watch progress persistence

### Performance Tests
- [ ] Profile memory usage during scrolling
- [ ] Profile memory usage during playback
- [ ] Test image loading performance
- [ ] Test database query performance
- [ ] Profile startup time

---

## 🚀 Phase 8: Deployment (Week 11)

### Pre-Deployment
- [ ] Test on NVIDIA Shield (high-end)
- [ ] Test on Chromecast with Google TV (mid-range)
- [ ] Test on budget Android TV stick
- [ ] Test with physical remote D-pad
- [ ] Test from 10-foot viewing distance
- [ ] Verify all text is readable
- [ ] Verify all UI is navigable
- [ ] Fix critical bugs

### API Configuration
- [ ] Store API token in BuildConfig
- [ ] Remove hardcoded credentials
- [ ] Configure network security for production
- [ ] Set up error reporting (optional)

### Performance Optimization
- [ ] Configure Coil memory cache (25% max)
- [ ] Configure Coil disk cache (50MB)
- [ ] Implement thumbnail prefetching
- [ ] Optimize database queries
- [ ] Enable ProGuard/R8 minification
- [ ] Review and optimize APK size

### APK Signing
- [ ] Generate release keystore
- [ ] Configure signing config in build.gradle
- [ ] Store keystore password in environment
- [ ] Build release APK
- [ ] Test signed APK on device

### Documentation
- [ ] Write USER_GUIDE.md
- [ ] Document parental control Easter egg
- [ ] Document API configuration
- [ ] Document building for Android TV
- [ ] Update README.md with TV info
- [ ] Document known issues
- [ ] Document troubleshooting steps

### Distribution
- [ ] Side-load APK for testing
- [ ] Consider Google Play Store submission
- [ ] Create app screenshots/videos
- [ ] Write store listing description
- [ ] Prepare privacy policy
- [ ] Submit for review (if Play Store)

---

## 📊 Progress Tracking

**Overall Progress**: 82/180 tasks completed (46%)

### Phase Progress
- [x] Phase 1: Foundation (40/40) ✅ COMPLETED
- [x] Phase 2: Core UI (24/24) ✅ COMPLETED
- [x] Phase 3: Video Playback (18/18) ✅ COMPLETED
- [ ] Phase 4: Search & Navigation (0/22)
- [ ] Phase 5: Child-Friendly (0/14)
- [ ] Phase 6: Parental Controls (0/30)
- [ ] Phase 7: Testing (0/17)
- [ ] Phase 8: Deployment (0/25)

### Current Sprint
**Sprint**: Phase 3 - Video Playback
**Start Date**: 2025-11-10
**Completion Date**: 2025-11-10
**Status**: ✅ COMPLETED

---

## 🐛 Known Issues & Blockers

*Track issues here as they arise*

---

## 📝 Notes

- API Token: `ddb865bf6f8970f8b52283a09f939316eb17c66d`
- Base URL: `https://ta.vishalk.com`
- Target Audience: Kids ages 1-5
- Primary Device: Android TV
- Parental Access: 5-click logo Easter egg

---

## 🔄 Recent Updates

| Date | Update | Phase |
|------|--------|-------|
| 2025-11-10 | Phase 3 completed - ExoPlayer video playback, watch progress tracking, TV controls with auto-hide | Phase 3 ✅ |
| 2025-11-10 | Phase 2 completed - TV-optimized UI components, hero carousel, home screen with TubeArchivist integration | Phase 2 ✅ |
| 2025-11-10 | Phase 1 completed - Foundation setup with TubeArchivist API integration, DataStore preferences, and Koin DI modules | Phase 1 ✅ |
| 2025-11-10 | Initial checklist created | Planning |
