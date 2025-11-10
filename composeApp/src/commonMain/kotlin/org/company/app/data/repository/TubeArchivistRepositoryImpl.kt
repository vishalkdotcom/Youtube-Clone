package org.company.app.data.repository

import org.company.app.data.remote.tubearchivist.TubeArchivistApi
import org.company.app.data.remote.tubearchivist.toDomain
import org.company.app.domain.model.tubearchivist.*
import org.company.app.domain.repository.TubeArchivistRepository

/**
 * Implementation of TubeArchivist repository
 * Handles data fetching from TubeArchivist API
 */
class TubeArchivistRepositoryImpl(
    private val api: TubeArchivistApi
) : TubeArchivistRepository {

    override suspend fun getVideos(page: Int, pageSize: Int): Result<List<TaVideo>> {
        return try {
            val response = api.getVideos(page, pageSize)
            Result.success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVideoDetails(videoId: String): Result<TaVideo> {
        return try {
            val response = api.getVideoDetails(videoId)
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchVideos(query: String, page: Int, pageSize: Int): Result<List<TaVideo>> {
        return try {
            val response = api.searchVideos(query, page, pageSize)
            Result.success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChannels(page: Int, pageSize: Int): Result<List<TaChannel>> {
        return try {
            val response = api.getChannels(page, pageSize)
            Result.success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChannelDetails(channelId: String): Result<TaChannel> {
        return try {
            val response = api.getChannelDetails(channelId)
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChannelVideos(channelId: String, page: Int, pageSize: Int): Result<List<TaVideo>> {
        return try {
            val response = api.getChannelVideos(channelId, page, pageSize)
            Result.success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylists(page: Int, pageSize: Int): Result<List<TaPlaylist>> {
        return try {
            val response = api.getPlaylists(page, pageSize)
            Result.success(response.data.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWatchProgress(videoId: String, position: Int): Result<Unit> {
        return api.updateWatchProgress(videoId, position)
    }
}
