package org.company.app.presentation.ui.screens.search

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import org.company.app.UserRegion
import org.company.app.core.common.formatVideoDuration
import org.company.app.core.common.formatViewCount
import org.company.app.core.common.getFormattedDateHome
import org.company.app.domain.model.search.Search
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.screens.detail.DetailScreen
import org.company.app.presentation.viewmodel.MainViewModel
import org.company.app.theme.LocalThemeIsDark
import org.koin.compose.koinInject
import org.company.app.domain.model.channel.Item as ChannelItem
import org.company.app.domain.model.videos.Item as YouTubeItem

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        SearchScreenContent()
    }
}

@OptIn(FlowPreview::class)
@Composable
fun SearchScreenContent(
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current

    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val searchQueryFlow = remember { MutableStateFlow("") }

    // Debounced search
    LaunchedEffect(Unit) {
        searchQueryFlow
            .debounce(500) // 500ms debounce
            .collect { debouncedQuery ->
                if (debouncedQuery.isNotEmpty()) {
                    isLoading = true
                    viewModel.getSearch(debouncedQuery, UserRegion())
                }
            }
    }

    // Update search flow when query changes
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            searchQueryFlow.value = query
        }
    }

    val searchState by viewModel.search.collectAsState()

    // Update loading state based on search state
    LaunchedEffect(searchState) {
        when (searchState) {
            is ResultState.LOADING -> isLoading = true
            is ResultState.SUCCESS, is ResultState.ERROR -> {
                delay(300) // Small delay for smoother transition
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        // Search Bar
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                if (query.isNotEmpty()) {
                    isLoading = true
                    viewModel.getSearch(query, UserRegion())
                }
            },
            onBack = {
                navigator?.pop()
            },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = if (isDark) Color.DarkGray else Color.LightGray
        )

        // Search Results
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            when (searchState) {
                is ResultState.LOADING -> {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is ResultState.SUCCESS -> {
                    val searchResults = (searchState as ResultState.SUCCESS).response
                    if (searchResults.items.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No results found for \"$query\"",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        SearchResultsList(searchResults)
                    }
                }

                is ResultState.ERROR -> {
                    val error = (searchState as ResultState.ERROR).error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ErrorBox(error)
                            Button(
                                onClick = {
                                    if (query.isNotEmpty()) {
                                        isLoading = true
                                        viewModel.getSearch(query, UserRegion())
                                    }
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    isDark: Boolean,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = if (isDark) Color.White else Color.Black
                )
            }

            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp)),
                placeholder = {
                    Text(
                        text = "Search videos, channels...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (isDark) Color(0xFF2D2D2D) else Color(0xFFF5F5F5),
                    unfocusedContainerColor = if (isDark) Color(0xFF2D2D2D) else Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                ),
                trailingIcon = {
                    Row {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = { onQueryChange("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = if (isDark) Color.White else Color.Black
                                )
                            }
                        }
                        IconButton(
                            onClick = onSearch
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (isDark) Color.White else Color.Black
                            )
                        }
                    }
                }
            )

            IconButton(
                onClick = { /* Voice search functionality */ },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Search",
                    tint = if (isDark) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun SearchResultsList(searchResults: Search) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        searchResults.items?.let { items ->
            items(items) { item ->
                SearchResultCard(item)
            }
        }
    }
}

@Composable
fun SearchResultCard(
    item: org.company.app.domain.model.search.Item,
    viewModel: MainViewModel = koinInject<MainViewModel>(),
) {
    val navigator = LocalNavigator.current
    val isDark by LocalThemeIsDark.current

    var channelDetails by remember { mutableStateOf<org.company.app.domain.model.channel.Channel?>(null) }
    var channel by remember { mutableStateOf<ChannelItem?>(null) }
    var singleVideo by remember { mutableStateOf<YouTubeItem?>(null) }
    var videoDetail by remember { mutableStateOf<org.company.app.domain.model.videos.Youtube?>(null) }

    // Load channel and video details
    LaunchedEffect(Unit) {
        if (item.id.videoId != null) {
            viewModel.getChannelDetails(item.snippet.channelId)
            viewModel.getSingleVideo(item.id.videoId)
        }
    }

    val channelState by viewModel.channelDetails.collectAsState()
    val videoState by viewModel.singleVideo.collectAsState()

    when (channelState) {
        is ResultState.SUCCESS -> {
            channelDetails = (channelState as ResultState.SUCCESS).response
            channelDetails?.items?.firstOrNull()?.let { channel = it }
        }
        else -> { /* Handle other states */ }
    }

    when (videoState) {
        is ResultState.SUCCESS -> {
            videoDetail = (videoState as ResultState.SUCCESS).response
            videoDetail?.items?.firstOrNull()?.let { singleVideo = it }
        }
        else -> { /* Handle other states */ }
    }

    // Skip if this is a channel result (not a video)
    if (item.snippet.title == channel?.snippet?.title) {
        return
    }

    val thumbnailUrl = item.snippet.thumbnails.high.url
    val image: Resource<Painter> = asyncPainterResource(data = thumbnailUrl)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator?.push(
                    DetailScreen(
                        video = singleVideo,
                        channelData = channel,
                        search = item,
                        logo = thumbnailUrl
                    )
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Thumbnail
            Box(modifier = Modifier.fillMaxWidth()) {
                KamelImage(
                    resource = image,
                    contentDescription = item.snippet.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    onLoading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(progress = { it })
                        }
                    },
                    onFailure = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Failed to load image")
                        }
                    },
                    animationSpec = tween()
                )

                // Duration badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = singleVideo?.contentDetails?.duration?.let { formatVideoDuration(it) } ?: "00:00",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Video Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Channel Avatar
                val channelImage: Resource<Painter> = asyncPainterResource(
                    data = channel?.snippet?.thumbnails?.default?.url ?: thumbnailUrl
                )
                KamelImage(
                    resource = channelImage,
                    contentDescription = item.snippet.channelTitle,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                // Text Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.snippet.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isDark) Color.White else Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.snippet.channelTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) Color.LightGray else Color.Gray
                        )

                        if (channel?.status?.isLinked == true) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = if (isDark) Color.White else Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        singleVideo?.statistics?.viewCount?.let { views ->
                            Text(
                                text = "${formatViewCount(views)} views",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) Color.LightGray else Color.Gray
                            )
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) Color.LightGray else Color.Gray
                            )
                        }

                        item.snippet.publishedAt?.let { publishDate ->
                            Text(
                                text = getFormattedDateHome(publishDate),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) Color.LightGray else Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
