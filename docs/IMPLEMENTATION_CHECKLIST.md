# Android TV TubeArchivist Implementation Checklist

**Project**: YouTube Clone → Android TV TubeArchivist Client
**Target**: Kids-friendly Android TV app (ages 1-5)
**Timeline**: 10-11 weeks

---

## 📋 Phase 1: Foundation Setup (Week 1-2)

### Project Configuration
- [ ] Create Android TV product flavor in build.gradle.kts
- [ ] Add androidx.tv dependencies (tv-material, tv-foundation)
- [ ] Add Media3 ExoPlayer dependencies
- [ ] Add Ktor Client dependencies
- [ ] Add Koin DI dependencies
- [ ] Add Room database dependencies
- [ ] Add DataStore preferences
- [ ] Add Coil image loading
- [ ] Add security-crypto for encrypted preferences

### Manifest Setup
- [ ] Create androidTvMain source set
- [ ] Configure TV manifest with LEANBACK_LAUNCHER
- [ ] Add touchscreen not required feature
- [ ] Add leanback software feature
- [ ] Create network_security_config.xml for ta.vishalk.com
- [ ] Create TV banner icon (320x180)
- [ ] Set landscape orientation

### Data Layer - API Client
- [ ] Create TubeArchivistApi.kt
- [ ] Define VideoDto, ChannelDto, PlaylistDto models
- [ ] Define PaginatedResponse model
- [ ] Implement getVideos() endpoint
- [ ] Implement getVideoDetails() endpoint
- [ ] Implement getChannels() endpoint
- [ ] Implement getChannelVideos() endpoint
- [ ] Implement searchVideos() endpoint
- [ ] Implement updateWatchProgress() endpoint
- [ ] Add stream URL helper methods
- [ ] Add thumbnail URL helper methods
- [ ] Configure Ktor client with ContentNegotiation
- [ ] Add authentication token header interceptor
- [ ] Add logging interceptor

### Data Layer - Repository
- [ ] Create VideoRepository interface
- [ ] Create VideoRepositoryImpl
- [ ] Create ChannelRepository interface
- [ ] Create ChannelRepositoryImpl
- [ ] Implement caching strategy (network + cache)
- [ ] Create data mappers (DTO → Domain)

### Data Layer - Database
- [ ] Create AppDatabase (Room)
- [ ] Create VideoEntity
- [ ] Create ChannelEntity
- [ ] Create VideoDao with queries
- [ ] Create ChannelDao with queries
- [ ] Implement watch progress tracking
- [ ] Implement continue watching query

### Data Layer - Preferences
- [ ] Create PreferencesManager with DataStore
- [ ] Add API token storage
- [ ] Add parental PIN storage (encrypted)
- [ ] Add screen time tracking fields
- [ ] Add daily limit preferences

### Dependency Injection
- [ ] Create NetworkModule (Koin)
- [ ] Create RepositoryModule (Koin)
- [ ] Create DatabaseModule (Koin)
- [ ] Create ViewModelModule (Koin)
- [ ] Initialize Koin in TubeArchivistApp

---

## 🎨 Phase 2: Core UI (Week 3-4)

### Theme Configuration
- [ ] Define YouTube-inspired color palette
- [ ] Create Color.kt (YouTubeRed, DarkBackground, etc.)
- [ ] Create Type.kt with TV-optimized typography (24sp+)
- [ ] Create Theme.kt with Material3 theme
- [ ] Define focus colors and borders

### Reusable Components
- [ ] Create VideoCard.kt
  - [ ] Thumbnail with AsyncImage (Coil)
  - [ ] Duration badge overlay
  - [ ] Watch progress bar
  - [ ] Title and channel name
  - [ ] Focus scale animation (1.1x)
  - [ ] Focus border (4dp white)
  - [ ] Focus glow effect
- [ ] Create ChannelCard.kt
- [ ] Create ContentRow.kt
  - [ ] TvLazyRow implementation
  - [ ] Section title
  - [ ] Horizontal spacing
  - [ ] PivotOffsets for auto-reveal
- [ ] Create HeroCarousel.kt
  - [ ] Carousel with auto-scroll
  - [ ] Background image with gradient
  - [ ] Video metadata overlay
  - [ ] Play button CTA

### Home Screen
- [ ] Create HomeViewModel.kt
- [ ] Define HomeUiState sealed class
- [ ] Implement loadContent() use case
- [ ] Create HomeScreen.kt composable
- [ ] Create TopBar with logo and search icon
- [ ] Add HeroCarousel section
- [ ] Add "Continue Watching" row
- [ ] Add "Recently Added" row
- [ ] Add loading state (shimmer/skeleton)
- [ ] Add error state with retry
- [ ] Implement pull-to-refresh (if needed)

---

## 🎬 Phase 3: Video Playback (Week 5-6)

### Player ViewModel
- [ ] Create PlayerViewModel.kt
- [ ] Define PlayerUiState sealed class
- [ ] Implement loadVideo() use case
- [ ] Track watch start time
- [ ] Implement updateWatchProgress() every 5s
- [ ] Handle time limit checks
- [ ] Release player resources on dispose

### Player Screen
- [ ] Create PlayerScreen.kt
- [ ] Integrate ExoPlayer with AndroidView
- [ ] Configure MediaItem with video URL
- [ ] Implement seek to saved position
- [ ] Add player controls overlay
- [ ] Add back button overlay
- [ ] Handle lifecycle (pause/resume)
- [ ] Track watch duration
- [ ] Update watch progress to repository
- [ ] Handle playback errors

### Player Controls
- [ ] Create PlayerControls.kt
- [ ] Play/pause button
- [ ] Seek bar
- [ ] Time remaining display
- [ ] Auto-hide controls after 5s
- [ ] Show on D-pad interaction
- [ ] TV-optimized touch targets

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

**Overall Progress**: 0/180 tasks completed (0%)

### Phase Progress
- [ ] Phase 1: Foundation (0/40)
- [ ] Phase 2: Core UI (0/24)
- [ ] Phase 3: Video Playback (0/18)
- [ ] Phase 4: Search & Navigation (0/22)
- [ ] Phase 5: Child-Friendly (0/14)
- [ ] Phase 6: Parental Controls (0/30)
- [ ] Phase 7: Testing (0/17)
- [ ] Phase 8: Deployment (0/25)

### Current Sprint
**Sprint**: Foundation Setup
**Start Date**: 2025-11-10
**Target Completion**: TBD
**Status**: Not Started

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
| 2025-11-10 | Initial checklist created | Planning |
