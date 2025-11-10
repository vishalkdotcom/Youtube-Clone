package org.company.app.presentation.ui.tv.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.company.app.domain.model.tubearchivist.TaVideo

/**
 * JS stub implementation of TvVideoPlayer
 */
@Composable
actual fun TvVideoPlayer(
    video: TaVideo,
    isPlaying: Boolean,
    onPlaybackStateChanged: (Boolean) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Video playback not supported on Web yet",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}
