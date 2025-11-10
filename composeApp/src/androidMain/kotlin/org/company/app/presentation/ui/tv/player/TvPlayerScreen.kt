package org.company.app.presentation.ui.tv.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.company.app.domain.model.tubearchivist.TaVideo
import org.company.app.presentation.ui.tv.theme.TvBackgroundDark
import org.company.app.presentation.ui.tv.theme.TvScrim
import org.company.app.presentation.ui.tv.theme.YouTubeRed
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * TV Player Screen
 *
 * Full-screen video player with TV-optimized controls
 */
@Composable
fun TvPlayerScreen(
    videoId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TvPlayerViewModel = koinViewModel { parametersOf(videoId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (val state = uiState) {
            is TvPlayerUiState.Loading -> {
                LoadingState()
            }

            is TvPlayerUiState.Ready -> {
                ReadyState(
                    state = state,
                    onPlayPauseClick = viewModel::togglePlayPause,
                    onSeek = viewModel::seekTo,
                    onShowControls = viewModel::showControls,
                    onBack = onBack
                )
            }

            is TvPlayerUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = viewModel::retry,
                    onBack = onBack
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
                text = "Loading video...",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

/**
 * Ready state - Show video player and controls
 */
@Composable
private fun ReadyState(
    state: TvPlayerUiState.Ready,
    onPlayPauseClick: () -> Unit,
    onSeek: (Long) -> Unit,
    onShowControls: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Video player (platform-specific)
        TvVideoPlayer(
            video = state.video,
            isPlaying = state.isPlaying,
            onPlaybackStateChanged = { /* Handled by ViewModel */ },
            modifier = Modifier.fillMaxSize()
        )

        // Controls overlay
        if (state.showControls) {
            TvPlayerControls(
                video = state.video,
                isPlaying = state.isPlaying,
                currentPosition = state.currentPosition,
                duration = state.duration,
                bufferedPercentage = state.bufferedPercentage,
                onPlayPauseClick = onPlayPauseClick,
                onSeek = onSeek,
                onBack = onBack,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Buffering indicator
        if (state.bufferedPercentage > 0 && state.bufferedPercentage < 100 && state.isPlaying) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                color = YouTubeRed
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
    onRetry: () -> Unit,
    onBack: () -> Unit
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
                text = "Playback Error",
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Retry button
                androidx.tv.material3.Button(
                    onClick = onRetry
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

                // Back button
                androidx.tv.material3.Button(
                    onClick = onBack
                ) {
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

/**
 * TV Player Controls - Overlay with play/pause, seek bar, time display
 */
@Composable
private fun TvPlayerControls(
    video: TaVideo,
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    bufferedPercentage: Int,
    onPlayPauseClick: () -> Unit,
    onSeek: (Long) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(TvScrim)
            .padding(48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Video title
        Text(
            text = video.title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            maxLines = 2
        )

        // Progress bar
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Seek bar
            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
                colors = SliderDefaults.colors(
                    thumbColor = YouTubeRed,
                    activeTrackColor = YouTubeRed,
                    inactiveTrackColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Time display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            androidx.tv.material3.Button(
                onClick = onBack
            ) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Play/Pause button (larger, centered)
            androidx.tv.material3.IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier.size(72.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(YouTubeRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

/**
 * Format time in milliseconds to MM:SS
 */
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
