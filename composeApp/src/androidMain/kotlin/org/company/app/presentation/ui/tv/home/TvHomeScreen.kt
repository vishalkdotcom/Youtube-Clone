package org.company.app.presentation.ui.tv.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.company.app.presentation.ui.tv.components.TvContentRow
import org.company.app.presentation.ui.tv.components.TvHeroCarousel
import org.company.app.presentation.ui.tv.theme.TvBackgroundDark
import org.company.app.presentation.ui.tv.theme.YouTubeRed
import org.koin.compose.viewmodel.koinViewModel

/**
 * TubeArchivist TV Home Screen
 *
 * Main screen for browsing TubeArchivist videos on Android TV
 */
@Composable
fun TvHomeScreen(
    modifier: Modifier = Modifier,
    onVideoClick: (String) -> Unit = {},
    viewModel: TvHomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackgroundDark)
    ) {
        when (val state = uiState) {
            is TvHomeUiState.Loading -> {
                LoadingState()
            }

            is TvHomeUiState.Success -> {
                SuccessState(
                    state = state,
                    onVideoClick = { video -> onVideoClick(video.id) }
                )
            }

            is TvHomeUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = viewModel::retry
                )
            }
        }

        // Top Bar (always visible)
        TvTopBar(
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

/**
 * Success state - Display content
 */
@Composable
private fun SuccessState(
    state: TvHomeUiState.Success,
    onVideoClick: (org.company.app.domain.model.tubearchivist.TaVideo) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 80.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Hero Carousel
        if (state.featuredVideos.isNotEmpty()) {
            item {
                TvHeroCarousel(
                    videos = state.featuredVideos,
                    onVideoClick = onVideoClick,
                    autoScrollDurationMillis = 5000
                )
            }
        }

        // Continue Watching
        if (state.continueWatching.isNotEmpty()) {
            item {
                TvContentRow(
                    title = "Continue Watching",
                    videos = state.continueWatching,
                    onVideoClick = onVideoClick
                )
            }
        }

        // Recent Videos
        if (state.recentVideos.isNotEmpty()) {
            item {
                TvContentRow(
                    title = "Recently Added",
                    videos = state.recentVideos,
                    onVideoClick = onVideoClick
                )
            }
        }
    }
}

/**
 * Loading state
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = YouTubeRed,
                strokeWidth = 6.dp
            )

            Text(
                text = "Loading TubeArchivist content...",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

/**
 * Error state
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = YouTubeRed,
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )

            // Retry button
            androidx.tv.material3.Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

/**
 * Top Bar for TV
 */
@Composable
private fun TvTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(horizontal = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo/Title
        Text(
            text = "TubeArchivist",
            style = MaterialTheme.typography.headlineLarge,
            color = YouTubeRed
        )

        // Search icon (placeholder - will be implemented in Phase 4)
        androidx.tv.material3.IconButton(
            onClick = { /* TODO: Implement search */ }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
