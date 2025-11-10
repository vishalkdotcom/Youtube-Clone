package org.company.app.data.local.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * iOS stub implementation of TvPreferencesManager
 * TV features are Android-only, so this is a no-op implementation
 */
class TvPreferencesManagerStub : TvPreferencesManager {
    override suspend fun setParentalPin(pin: String) {}
    override suspend fun getParentalPin(): String? = null
    override suspend fun clearParentalPin() {}
    override suspend fun isPinSet(): Boolean = false

    override suspend fun setDailyTimeLimit(minutes: Int) {}
    override suspend fun getDailyTimeLimit(): Int = -1
    override suspend fun getTodayWatchTime(): Long = 0L
    override suspend fun addWatchTime(milliseconds: Long) {}
    override suspend fun resetDailyWatchTime() {}

    override fun watchTodayWatchTimeFlow(): Flow<Long> = flowOf(0L)

    override suspend fun setTubeArchivistEnabled(enabled: Boolean) {}
    override suspend fun isTubeArchivistEnabled(): Boolean = false

    override suspend fun setApiToken(token: String) {}
    override suspend fun getApiToken(): String? = null

    override suspend fun setBaseUrl(url: String) {}
    override suspend fun getBaseUrl(): String? = null

    override suspend fun setFirstLaunchComplete(complete: Boolean) {}
    override suspend fun isFirstLaunchComplete(): Boolean = true
}

/**
 * iOS stub implementation of TvPreferencesManagerFactory
 */
actual class TvPreferencesManagerFactory {
    actual fun create(): TvPreferencesManager = TvPreferencesManagerStub()
}
