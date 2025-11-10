package org.company.app.data.remote.tubearchivist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TubeArchivist API Channel Data Transfer Objects
 */

@Serializable
data class TaChannelDto(
    @SerialName("channel_id") val channelId: String,
    @SerialName("channel_name") val channelName: String,
    @SerialName("channel_banner_url") val channelBannerUrl: String? = null,
    @SerialName("channel_thumb_url") val channelThumbUrl: String? = null,
    @SerialName("channel_tvart_url") val channelTvartUrl: String? = null,
    @SerialName("channel_description") val channelDescription: String? = null,
    @SerialName("channel_last_refresh") val channelLastRefresh: String? = null,
    @SerialName("channel_subs") val channelSubs: Long? = null,
    @SerialName("channel_views") val channelViews: Long? = null,
    @SerialName("channel_active") val channelActive: Boolean = true
)

@Serializable
data class TaChannelDetailDto(
    @SerialName("data") val data: TaChannelDto
)
