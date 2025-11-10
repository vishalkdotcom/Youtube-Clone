package org.company.app.domain.model.tubearchivist

/**
 * Domain model for TubeArchivist Video
 * Used in presentation layer
 */
data class TaVideo(
    val id: String,
    val title: String,
    val channelName: String,
    val channelId: String,
    val thumbnailUrl: String,
    val duration: Int, // in seconds
    val durationFormatted: String,
    val publishedDate: String,
    val downloadedDate: Long,
    val description: String,
    val tags: List<String>,
    val viewCount: Long,
    val likeCount: Long,
    val watched: Boolean,
    val streamUrl: String
)

data class TaChannel(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val bannerUrl: String,
    val description: String,
    val subscriberCount: Long,
    val viewCount: Long
)

data class TaPlaylist(
    val id: String,
    val name: String,
    val channelName: String,
    val channelId: String,
    val thumbnailUrl: String,
    val description: String
)

/**
 * Watch progress for a video
 */
data class TaWatchProgress(
    val videoId: String,
    val position: Int, // in seconds
    val duration: Int, // in seconds
    val percentComplete: Float
)
