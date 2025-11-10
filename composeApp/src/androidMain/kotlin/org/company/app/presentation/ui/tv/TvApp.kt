package org.company.app.presentation.ui.tv

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.company.app.presentation.ui.tv.home.TvHomeScreen
import org.company.app.presentation.ui.tv.player.TvPlayerScreen

/**
 * Main TubeArchivist TV App
 *
 * Simple navigation between Home and Player screens
 */
@Composable
fun TubeArchivistTvApp(
    modifier: Modifier = Modifier
) {
    // Simple navigation state
    var currentScreen by remember { mutableStateOf<TvScreen>(TvScreen.Home) }

    when (val screen = currentScreen) {
        is TvScreen.Home -> {
            TvHomeScreen(
                modifier = modifier,
                onVideoClick = { videoId ->
                    currentScreen = TvScreen.Player(videoId)
                }
            )
        }

        is TvScreen.Player -> {
            TvPlayerScreen(
                videoId = screen.videoId,
                onBack = {
                    currentScreen = TvScreen.Home
                },
                modifier = modifier
            )
        }
    }
}

/**
 * Navigation screens
 */
sealed class TvScreen {
    data object Home : TvScreen()
    data class Player(val videoId: String) : TvScreen()
}
