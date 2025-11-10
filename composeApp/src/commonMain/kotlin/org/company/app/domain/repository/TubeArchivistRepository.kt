package org.company.app.domain.repository

import org.company.app.domain.model.tubearchivist.*

/**
 * Repository interface for TubeArchivist data access
 */
interface TubeArchivistRepository {

    /**
     * Get paginated list of videos
     */
    suspend fun getVideos(page: Int = 1, pageSize: Int = 25): Result<List<TaVideo>>

    /**
     * Get video details by ID
     */
    suspend fun getVideoDetails(videoId: String): Result<TaVideo>

    /**
     * Search for videos
     */
    suspend fun searchVideos(query: String, page: Int = 1, pageSize: Int = 25): Result<List<TaVideo>>

    /**
     * Get list of channels
     */
    suspend fun getChannels(page: Int = 1, pageSize: Int = 25): Result<List<TaChannel>>

    /**
     * Get channel details by ID
     */
    suspend fun getChannelDetails(channelId: String): Result<TaChannel>

    /**
     * Get videos from a specific channel
     */
    suspend fun getChannelVideos(channelId: String, page: Int = 1, pageSize: Int = 25): Result<List<TaVideo>>

    /**
     * Get list of playlists
     */
    suspend fun getPlaylists(page: Int = 1, pageSize: Int = 25): Result<List<TaPlaylist>>

    /**
     * Update watch progress for a video
     */
    suspend fun updateWatchProgress(videoId: String, position: Int): Result<Unit>
}
