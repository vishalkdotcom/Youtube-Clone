package org.company.app.presentation.ui.tv.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import org.company.app.domain.model.tubearchivist.TaVideo

/**
 * TV Content Row - Horizontal scrolling row of video cards
 *
 * @param title Section title
 * @param videos List of videos to display
 * @param onVideoClick Callback when video is clicked
 * @param modifier Modifier for customization
 */
@Composable
fun TvContentRow(
    title: String,
    videos: List<TaVideo>,
    onVideoClick: (TaVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Section title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(start = 48.dp, bottom = 16.dp)
        )

        // Horizontal scrolling list
        TvLazyRow(
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(),
            pivotOffsets = androidx.tv.foundation.PivotOffsets(
                parentFraction = 0.07f // Auto-scroll when focus reaches 7% from edge
            )
        ) {
            items(
                items = videos,
                key = { it.id }
            ) { video ->
                TvVideoCardWithMeta(
                    video = video,
                    onClick = { onVideoClick(video) },
                    width = 280
                )
            }
        }
    }
}

/**
 * Content Row with custom card renderer
 */
@Composable
fun <T> TvContentRowGeneric(
    title: String,
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    cardContent: @Composable (T, () -> Unit) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Section title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(start = 48.dp, bottom = 16.dp)
        )

        // Horizontal scrolling list
        TvLazyRow(
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(),
            pivotOffsets = androidx.tv.foundation.PivotOffsets(
                parentFraction = 0.07f
            )
        ) {
            items(items.size) { index ->
                val item = items[index]
                cardContent(item) { onItemClick(item) }
            }
        }
    }
}
