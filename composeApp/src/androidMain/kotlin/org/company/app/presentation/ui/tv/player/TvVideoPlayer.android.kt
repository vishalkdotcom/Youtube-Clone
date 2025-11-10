package org.company.app.presentation.ui.tv.player

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.company.app.domain.model.tubearchivist.TaVideo

/**
 * Android implementation of TvVideoPlayer using ExoPlayer
 */
@Composable
actual fun TvVideoPlayer(
    video: TaVideo,
    isPlaying: Boolean,
    onPlaybackStateChanged: (Boolean) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    // Remember ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Set media item
            val mediaItem = MediaItem.fromUri(video.streamUrl)
            setMediaItem(mediaItem)
            prepare()

            // Add listener for playback state changes
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    onPlaybackStateChanged(isPlaying)
                }
            })
        }
    }

    // Sync play/pause state with ExoPlayer
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    // Dispose ExoPlayer when leaving composition
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // AndroidView to host ExoPlayer PlayerView
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false // Use custom TV controls
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = modifier
    )
}
