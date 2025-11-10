package org.company.app.data.local.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing TV app preferences
 * Includes parental control settings and app configuration
 */
interface TvPreferencesManager {

    // Parental Control Settings
    suspend fun setParentalPin(pin: String)
    suspend fun getParentalPin(): String?
    suspend fun clearParentalPin()
    suspend fun isPinSet(): Boolean

    // Screen Time Settings
    suspend fun setDailyTimeLimit(minutes: Int)
    suspend fun getDailyTimeLimit(): Int // in minutes, -1 = unlimited
    suspend fun getTodayWatchTime(): Long // in milliseconds
    suspend fun addWatchTime(milliseconds: Long)
    suspend fun resetDailyWatchTime()

    // Watch Time as Flow
    fun watchTodayWatchTimeFlow(): Flow<Long>

    // App Settings
    suspend fun setTubeArchivistEnabled(enabled: Boolean)
    suspend fun isTubeArchivistEnabled(): Boolean

    suspend fun setApiToken(token: String)
    suspend fun getApiToken(): String?

    suspend fun setBaseUrl(url: String)
    suspend fun getBaseUrl(): String?

    // First launch flag
    suspend fun setFirstLaunchComplete(complete: Boolean)
    suspend fun isFirstLaunchComplete(): Boolean
}

/**
 * Expected platform-specific factory function
 */
expect class TvPreferencesManagerFactory {
    fun create(): TvPreferencesManager
}
