package org.company.app.data.remote.tubearchivist

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.company.app.data.remote.tubearchivist.models.*

/**
 * TubeArchivist API Client
 *
 * Communicates with TubeArchivist backend at ta.vishalk.com
 * Uses token-based authentication
 */
class TubeArchivistApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://ta.vishalk.com",
    private val apiToken: String = "ddb865bf6f8970f8b52283a09f939316eb17c66d"
) {

    /**
     * Get paginated list of videos
     * @param page Page number (default: 1)
     * @param pageSize Number of items per page (default: 25)
     */
    suspend fun getVideos(page: Int = 1, pageSize: Int = 25): TaPaginatedResponse<TaVideoDto> {
        return client.get("$baseUrl/api/video/") {
            header("Authorization", "Token $apiToken")
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
    }

    /**
     * Get details for a specific video
     * @param videoId YouTube video ID
     */
    suspend fun getVideoDetails(videoId: String): TaVideoDetailDto {
        return client.get("$baseUrl/api/video/$videoId/") {
            header("Authorization", "Token $apiToken")
        }.body()
    }

    /**
     * Search for videos
     * @param query Search query
     * @param page Page number
     * @param pageSize Items per page
     */
    suspend fun searchVideos(
        query: String,
        page: Int = 1,
        pageSize: Int = 25
    ): TaPaginatedResponse<TaVideoDto> {
        return client.get("$baseUrl/api/video/") {
            header("Authorization", "Token $apiToken")
            parameter("search", query)
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
    }

    /**
     * Get list of channels
     * @param page Page number
     * @param pageSize Items per page
     */
    suspend fun getChannels(page: Int = 1, pageSize: Int = 25): TaPaginatedResponse<TaChannelDto> {
        return client.get("$baseUrl/api/channel/") {
            header("Authorization", "Token $apiToken")
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
    }

    /**
     * Get details for a specific channel
     * @param channelId YouTube channel ID
     */
    suspend fun getChannelDetails(channelId: String): TaChannelDetailDto {
        return client.get("$baseUrl/api/channel/$channelId/") {
            header("Authorization", "Token $apiToken")
        }.body()
    }

    /**
     * Get videos from a specific channel
     * @param channelId YouTube channel ID
     * @param page Page number
     * @param pageSize Items per page
     */
    suspend fun getChannelVideos(
        channelId: String,
        page: Int = 1,
        pageSize: Int = 25
    ): TaPaginatedResponse<TaVideoDto> {
        return client.get("$baseUrl/api/channel/$channelId/video/") {
            header("Authorization", "Token $apiToken")
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
    }

    /**
     * Get list of playlists
     * @param page Page number
     * @param pageSize Items per page
     */
    suspend fun getPlaylists(page: Int = 1, pageSize: Int = 25): TaPaginatedResponse<TaPlaylistDto> {
        return client.get("$baseUrl/api/playlist/") {
            header("Authorization", "Token $apiToken")
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()
    }

    /**
     * Update watch progress for a video
     * @param videoId YouTube video ID
     * @param position Current playback position in seconds
     */
    suspend fun updateWatchProgress(videoId: String, position: Int): Result<Unit> {
        return try {
            client.post("$baseUrl/api/video/$videoId/progress/") {
                header("Authorization", "Token $apiToken")
                contentType(ContentType.Application.Json)
                setBody(TaWatchProgressDto(youtubeId = videoId, position = position))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get video stream URL
     * @param videoId YouTube video ID
     * @return Full URL to video stream
     */
    fun getVideoStreamUrl(videoId: String): String {
        return "$baseUrl/media/videos/$videoId.mp4"
    }

    /**
     * Get thumbnail URL for video
     * @param videoId YouTube video ID
     * @return Full URL to thumbnail image
     */
    fun getVideoThumbnailUrl(videoId: String): String {
        return "$baseUrl/cache/videos/$videoId.jpg"
    }

    /**
     * Get channel thumbnail URL
     * @param channelId YouTube channel ID
     * @return Full URL to channel thumbnail
     */
    fun getChannelThumbnailUrl(channelId: String): String {
        return "$baseUrl/cache/channels/$channelId.jpg"
    }
}
