package org.company.app.presentation.ui.tv.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.company.app.domain.model.tubearchivist.TaVideo
import org.company.app.domain.repository.TubeArchivistRepository

/**
 * UI State for TubeArchivist TV Home Screen
 */
sealed class TvHomeUiState {
    data object Loading : TvHomeUiState()
    data class Success(
        val featuredVideos: List<TaVideo>,
        val recentVideos: List<TaVideo>,
        val continueWatching: List<TaVideo> = emptyList()
    ) : TvHomeUiState()
    data class Error(val message: String) : TvHomeUiState()
}

/**
 * ViewModel for TubeArchivist TV Home Screen
 *
 * Manages fetching and displaying videos from TubeArchivist backend
 */
class TvHomeViewModel(
    private val repository: TubeArchivistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TvHomeUiState>(TvHomeUiState.Loading)
    val uiState: StateFlow<TvHomeUiState> = _uiState.asStateFlow()

    init {
        loadContent()
    }

    /**
     * Load content from TubeArchivist API
     */
    fun loadContent() {
        viewModelScope.launch {
            _uiState.value = TvHomeUiState.Loading

            try {
                // Fetch videos from TubeArchivist
                val videosResult = repository.getVideos(page = 1, pageSize = 50)

                videosResult.fold(
                    onSuccess = { videos ->
                        if (videos.isEmpty()) {
                            _uiState.value = TvHomeUiState.Error("No videos found")
                        } else {
                            // Split videos into sections
                            val featuredVideos = videos.take(5) // First 5 for carousel
                            val recentVideos = videos.drop(5).take(20) // Next 20 for recent
                            val continueWatching = videos.filter { it.watched }.take(10)

                            _uiState.value = TvHomeUiState.Success(
                                featuredVideos = featuredVideos,
                                recentVideos = recentVideos,
                                continueWatching = continueWatching
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.value = TvHomeUiState.Error(
                            error.message ?: "Failed to load videos"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = TvHomeUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    /**
     * Retry loading content
     */
    fun retry() {
        loadContent()
    }

    /**
     * Handle video click
     */
    fun onVideoClick(video: TaVideo) {
        // TODO: Navigate to video player screen
        // This will be implemented in Phase 3
    }
}
