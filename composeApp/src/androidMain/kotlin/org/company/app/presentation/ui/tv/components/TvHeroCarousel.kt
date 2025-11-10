package org.company.app.presentation.ui.tv.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import org.company.app.domain.model.tubearchivist.TaVideo
import org.company.app.presentation.ui.tv.theme.TvScrim
import org.company.app.presentation.ui.tv.theme.YouTubeGray

/**
 * Hero Carousel - Featured content section with auto-scroll
 *
 * @param videos List of featured videos
 * @param onVideoClick Callback when video is clicked
 * @param modifier Modifier for customization
 * @param autoScrollDurationMillis Duration for auto-scroll (0 to disable)
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvHeroCarousel(
    videos: List<TaVideo>,
    onVideoClick: (TaVideo) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollDurationMillis: Long = 5000
) {
    if (videos.isEmpty()) return

    val carouselState = remember { CarouselState() }

    // Auto-scroll effect
    if (autoScrollDurationMillis > 0) {
        LaunchedEffect(carouselState) {
            while (true) {
                delay(autoScrollDurationMillis)
                val nextIndex = (carouselState.activeItemIndex + 1) % videos.size
                carouselState.scrollToItem(nextIndex)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(540.dp) // 16:9 aspect ratio optimized for TV
    ) {
        Carousel(
            itemCount = videos.size,
            modifier = Modifier.fillMaxSize(),
            carouselState = carouselState,
            contentTransformEndToStart = CarouselDefaults.contentTransform(),
            contentTransformStartToEnd = CarouselDefaults.contentTransform()
        ) { index ->
            val video = videos[index]
            HeroCarouselItem(
                video = video,
                onClick = { onVideoClick(video) }
            )
        }
    }
}

/**
 * Individual Hero Carousel Item
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun HeroCarouselItem(
    video: TaVideo,
    onClick: () -> Unit
) {
    androidx.tv.material3.Card(
        onClick = onClick,
        modifier = Modifier.fillMaxSize(),
        shape = androidx.tv.material3.CardDefaults.shape(RoundedCornerShape(0.dp)),
        scale = androidx.tv.material3.CardDefaults.scale(
            focusedScale = 1.05f
        ),
        border = androidx.tv.material3.CardDefaults.border(
            focusedBorder = androidx.tv.foundation.Border(
                border = androidx.compose.foundation.BorderStroke(6.dp, Color.White),
                shape = RoundedCornerShape(0.dp)
            )
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
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
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                    }
                }
            )

            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                TvScrim
                            ),
                            startY = 200f
                        )
                    )
            )

            // Content overlay (bottom-left)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(48.dp)
                    .fillMaxWidth(0.6f)
            ) {
                // Play button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color.Red,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Metadata row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = video.channelName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.LightGray
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.LightGray
                    )

                    Text(
                        text = video.durationFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.LightGray
                    )

                    if (video.viewCount > 0) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.LightGray
                        )

                        Text(
                            text = formatViewCount(video.viewCount),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.LightGray
                        )
                    }
                }

                // Description (if available)
                if (video.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = video.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.LightGray,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun formatViewCount(count: Long): String {
    return when {
        count < 1000 -> "$count views"
        count < 1_000_000 -> "${count / 1000}K views"
        else -> "${count / 1_000_000}M views"
    }
}
