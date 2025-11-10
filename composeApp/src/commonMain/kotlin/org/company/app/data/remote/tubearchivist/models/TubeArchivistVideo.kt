package org.company.app.data.remote.tubearchivist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TubeArchivist API Video Data Transfer Objects
 */

@Serializable
data class TaPaginatedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("paginate") val paginate: TaPagination? = null
)

@Serializable
data class TaPagination(
    @SerialName("page_size") val pageSize: Int,
    @SerialName("page_from") val pageFrom: Int,
    @SerialName("prev_pages") val prevPages: List<Int>? = null,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("max_hits") val maxHits: Boolean,
    @SerialName("params") val params: String,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("next_pages") val nextPages: List<Int>? = null,
    @SerialName("total_hits") val totalHits: Int? = null
)

@Serializable
data class TaVideoDto(
    @SerialName("youtube_id") val youtubeId: String,
    @SerialName("title") val title: String,
    @SerialName("channel") val channel: TaChannelSummary,
    @SerialName("published") val published: String,
    @SerialName("vid_last_refresh") val vidLastRefresh: String? = null,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("vid_thumb_url") val vidThumbUrl: String,
    @SerialName("vid_thumb_base64") val vidThumbBase64: String? = null,
    @SerialName("date_downloaded") val dateDownloaded: Long,
    @SerialName("description") val description: String? = null,
    @SerialName("vid_type") val vidType: String? = "videos",
    @SerialName("active") val active: Boolean = true,
    @SerialName("player") val player: TaPlayerInfo? = null,
    @SerialName("stats") val stats: TaVideoStats? = null
)

@Serializable
data class TaChannelSummary(
    @SerialName("channel_id") val channelId: String,
    @SerialName("channel_name") val channelName: String,
    @SerialName("channel_banner_url") val channelBannerUrl: String? = null,
    @SerialName("channel_thumb_url") val channelThumbUrl: String? = null,
    @SerialName("channel_tvart_url") val channelTvartUrl: String? = null
)

@Serializable
data class TaPlayerInfo(
    @SerialName("watched") val watched: Boolean = false,
    @SerialName("duration") val duration: Int,
    @SerialName("duration_str") val durationStr: String
)

@Serializable
data class TaVideoStats(
    @SerialName("view_count") val viewCount: Long? = null,
    @SerialName("like_count") val likeCount: Long? = null,
    @SerialName("dislike_count") val dislikeCount: Long? = null,
    @SerialName("average_rating") val averageRating: Double? = null
)

@Serializable
data class TaVideoDetailDto(
    @SerialName("data") val data: TaVideoDto
)

@Serializable
data class TaWatchProgressDto(
    @SerialName("youtube_id") val youtubeId: String,
    @SerialName("position") val position: Int
)
