package org.company.app.presentation.ui.tv.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.company.app.domain.model.tubearchivist.TaVideo
import org.company.app.presentation.ui.tv.theme.YouTubeDarkGray
import org.company.app.presentation.ui.tv.theme.YouTubeGray

/**
 * TV-optimized Video Card for TubeArchivist
 *
 * @param video The video to display
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for customization
 * @param width Card width (default 260dp for kids-friendly mode)
 */
@Composable
fun TvVideoCard(
    video: TaVideo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Int = 260
) {
    androidx.tv.material3.Card(
        onClick = onClick,
        modifier = modifier
            .width(width.dp)
            .aspectRatio(16f / 9f),
        shape = androidx.tv.material3.CardDefaults.shape(RoundedCornerShape(8.dp)),
        scale = androidx.tv.material3.CardDefaults.scale(
            focusedScale = 1.1f // Slightly larger scale on focus
        ),
        border = androidx.tv.material3.CardDefaults.border(
            focusedBorder = androidx.tv.foundation.Border(
                border = androidx.compose.foundation.BorderStroke(4.dp, Color.White),
                shape = RoundedCornerShape(8.dp)
            )
        ),
        glow = androidx.tv.material3.CardDefaults.glow(
            focusedGlow = androidx.tv.material3.Glow(
                elevationColor = Color.White,
                elevation = 16.dp
            )
        )
    ) {
        Box {
            // Thumbnail image
            KamelImage(
                resource = asyncPainterResource(video.thumbnailUrl),
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onLoading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(YouTubeGray)
                    )
                },
                onFailure = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(YouTubeGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            )

            // Duration badge (bottom-right corner)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = video.durationFormatted,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }

            // Watch progress bar (if watched)
            if (video.watched) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Red)
                )
            }

            // Gradient overlay for text readability (bottom)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
        }
    }
}

/**
 * Video Card with metadata (title and channel) below thumbnail
 */
@Composable
fun TvVideoCardWithMeta(
    video: TaVideo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Int = 260
) {
    Column(
        modifier = modifier.width(width.dp)
    ) {
        // Video card
        TvVideoCard(
            video = video,
            onClick = onClick,
            width = width
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Channel name and views
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = video.channelName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (video.viewCount > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatViewCount(video.viewCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}

/**
 * Format view count for display
 */
private fun formatViewCount(count: Long): String {
    return when {
        count < 1000 -> "$count views"
        count < 1_000_000 -> "${count / 1000}K views"
        else -> "${count / 1_000_000}M views"
    }
}
