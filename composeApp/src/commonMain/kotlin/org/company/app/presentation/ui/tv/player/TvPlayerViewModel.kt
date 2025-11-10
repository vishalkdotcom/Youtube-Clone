package org.company.app.presentation.ui.tv.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.company.app.domain.model.tubearchivist.TaVideo
import org.company.app.domain.repository.TubeArchivistRepository
import kotlin.time.Duration.Companion.seconds

/**
 * UI State for TV Player Screen
 */
sealed class TvPlayerUiState {
    data object Loading : TvPlayerUiState()
    data class Ready(
        val video: TaVideo,
        val isPlaying: Boolean = false,
        val currentPosition: Long = 0L, // in milliseconds
        val duration: Long = 0L, // in milliseconds
        val bufferedPercentage: Int = 0,
        val showControls: Boolean = true
    ) : TvPlayerUiState()
    data class Error(val message: String) : TvPlayerUiState()
}

/**
 * ViewModel for TV Player Screen
 *
 * Manages video playback state and watch progress tracking
 */
class TvPlayerViewModel(
    private val repository: TubeArchivistRepository,
    private val videoId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<TvPlayerUiState>(TvPlayerUiState.Loading)
    val uiState: StateFlow<TvPlayerUiState> = _uiState.asStateFlow()

    private var watchProgressJob: Job? = null
    private var controlsHideJob: Job? = null
    private var lastProgressUpdatePosition = 0L

    init {
        loadVideo()
    }

    /**
     * Load video details from TubeArchivist
     */
    private fun loadVideo() {
        viewModelScope.launch {
            _uiState.value = TvPlayerUiState.Loading

            try {
                val result = repository.getVideoDetails(videoId)

                result.fold(
                    onSuccess = { video ->
                        _uiState.value = TvPlayerUiState.Ready(
                            video = video,
                            showControls = true
                        )
                        startWatchProgressTracking()
                    },
                    onFailure = { error ->
                        _uiState.value = TvPlayerUiState.Error(
                            error.message ?: "Failed to load video"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = TvPlayerUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    /**
     * Update playback state
     */
    fun updatePlaybackState(
        isPlaying: Boolean,
        currentPosition: Long,
        duration: Long,
        bufferedPercentage: Int
    ) {
        val currentState = _uiState.value
        if (currentState is TvPlayerUiState.Ready) {
            _uiState.value = currentState.copy(
                isPlaying = isPlaying,
                currentPosition = currentPosition,
                duration = duration,
                bufferedPercentage = bufferedPercentage
            )
        }
    }

    /**
     * Toggle play/pause
     */
    fun togglePlayPause() {
        val currentState = _uiState.value
        if (currentState is TvPlayerUiState.Ready) {
            _uiState.value = currentState.copy(
                isPlaying = !currentState.isPlaying
            )
        }
    }

    /**
     * Show controls and start auto-hide timer
     */
    fun showControls() {
        val currentState = _uiState.value
        if (currentState is TvPlayerUiState.Ready) {
            _uiState.value = currentState.copy(showControls = true)
            startControlsHideTimer()
        }
    }

    /**
     * Hide controls
     */
    fun hideControls() {
        val currentState = _uiState.value
        if (currentState is TvPlayerUiState.Ready) {
            _uiState.value = currentState.copy(showControls = false)
        }
    }

    /**
     * Start auto-hide timer for controls (5 seconds)
     */
    private fun startControlsHideTimer() {
        controlsHideJob?.cancel()
        controlsHideJob = viewModelScope.launch {
            delay(5.seconds)
            hideControls()
        }
    }

    /**
     * Start watch progress tracking (update every 5 seconds)
     */
    private fun startWatchProgressTracking() {
        watchProgressJob?.cancel()
        watchProgressJob = viewModelScope.launch {
            while (isActive) {
                delay(5.seconds)

                val currentState = _uiState.value
                if (currentState is TvPlayerUiState.Ready && currentState.isPlaying) {
                    val position = currentState.currentPosition / 1000 // Convert to seconds

                    // Only update if position changed significantly (>2 seconds)
                    if (kotlin.math.abs(position - lastProgressUpdatePosition) > 2) {
                        updateWatchProgressToServer(position.toInt())
                        lastProgressUpdatePosition = position
                    }
                }
            }
        }
    }

    /**
     * Update watch progress to TubeArchivist server
     */
    private fun updateWatchProgressToServer(positionSeconds: Int) {
        viewModelScope.launch {
            try {
                repository.updateWatchProgress(videoId, positionSeconds)
            } catch (e: Exception) {
                // Log error but don't interrupt playback
                println("Failed to update watch progress: ${e.message}")
            }
        }
    }

    /**
     * Seek to position
     */
    fun seekTo(positionMillis: Long) {
        val currentState = _uiState.value
        if (currentState is TvPlayerUiState.Ready) {
            _uiState.value = currentState.copy(currentPosition = positionMillis)
        }
    }

    /**
     * Handle playback error
     */
    fun onPlaybackError(error: String) {
        _uiState.value = TvPlayerUiState.Error(error)
    }

    /**
     * Retry loading video
     */
    fun retry() {
        loadVideo()
    }

    /**
     * Clean up resources
     */
    override fun onCleared() {
        super.onCleared()
        watchProgressJob?.cancel()
        controlsHideJob?.cancel()

        // Save final watch progress
        val currentState = _uiState.value
        if (currentState is TvPlayerUiState.Ready) {
            val positionSeconds = (currentState.currentPosition / 1000).toInt()
            viewModelScope.launch {
                try {
                    repository.updateWatchProgress(videoId, positionSeconds)
                } catch (e: Exception) {
                    println("Failed to save final watch progress: ${e.message}")
                }
            }
        }
    }
}
