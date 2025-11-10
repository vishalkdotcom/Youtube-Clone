package org.company.app.data.remote.tubearchivist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TubeArchivist API Playlist Data Transfer Objects
 */

@Serializable
data class TaPlaylistDto(
    @SerialName("playlist_id") val playlistId: String,
    @SerialName("playlist_name") val playlistName: String,
    @SerialName("playlist_channel") val playlistChannel: String,
    @SerialName("playlist_channel_id") val playlistChannelId: String,
    @SerialName("playlist_thumbnail") val playlistThumbnail: String? = null,
    @SerialName("playlist_description") val playlistDescription: String? = null,
    @SerialName("playlist_last_refresh") val playlistLastRefresh: String? = null
)

@Serializable
data class TaPlaylistDetailDto(
    @SerialName("data") val data: TaPlaylistDto
)
