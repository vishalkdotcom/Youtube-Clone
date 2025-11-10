package org.company.app.data.remote.tubearchivist

import org.company.app.data.remote.tubearchivist.models.*
import org.company.app.domain.model.tubearchivist.*

/**
 * Mapper functions to convert TubeArchivist DTOs to Domain models
 */

private const val BASE_URL = "https://ta.vishalk.com"

fun TaVideoDto.toDomain(): TaVideo {
    return TaVideo(
        id = youtubeId,
        title = title,
        channelName = channel.channelName,
        channelId = channel.channelId,
        thumbnailUrl = "$BASE_URL/cache/videos/$youtubeId.jpg",
        duration = player?.duration ?: 0,
        durationFormatted = player?.durationStr ?: "0:00",
        publishedDate = published,
        downloadedDate = dateDownloaded,
        description = description ?: "",
        tags = tags ?: emptyList(),
        viewCount = stats?.viewCount ?: 0,
        likeCount = stats?.likeCount ?: 0,
        watched = player?.watched ?: false,
        streamUrl = "$BASE_URL/media/videos/$youtubeId.mp4"
    )
}

fun TaChannelDto.toDomain(): TaChannel {
    return TaChannel(
        id = channelId,
        name = channelName,
        thumbnailUrl = channelThumbUrl ?: "$BASE_URL/cache/channels/$channelId.jpg",
        bannerUrl = channelBannerUrl ?: "",
        description = channelDescription ?: "",
        subscriberCount = channelSubs ?: 0,
        viewCount = channelViews ?: 0
    )
}

fun TaPlaylistDto.toDomain(): TaPlaylist {
    return TaPlaylist(
        id = playlistId,
        name = playlistName,
        channelName = playlistChannel,
        channelId = playlistChannelId,
        thumbnailUrl = playlistThumbnail ?: "",
        description = playlistDescription ?: ""
    )
}
