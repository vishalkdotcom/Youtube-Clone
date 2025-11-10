package org.company.app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tv_preferences")

actual class TvPreferencesManagerFactory(private val context: Context) {
    actual fun create(): TvPreferencesManager {
        return TvPreferencesManagerImpl(context)
    }
}

class TvPreferencesManagerImpl(private val context: Context) : TvPreferencesManager {

    private val dataStore = context.dataStore

    // Encrypted preferences for PIN storage
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = try {
        EncryptedSharedPreferences.create(
            context,
            "tv_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        // Fallback to regular SharedPreferences if encryption fails
        context.getSharedPreferences("tv_secure_prefs_fallback", Context.MODE_PRIVATE)
    }

    companion object {
        private val DAILY_TIME_LIMIT = intPreferencesKey("daily_time_limit")
        private val TODAY_WATCH_TIME = longPreferencesKey("today_watch_time")
        private val LAST_RESET_DATE = longPreferencesKey("last_reset_date")
        private val TUBEARCHIVIST_ENABLED = booleanPreferencesKey("tubearchivist_enabled")
        private val API_TOKEN = stringPreferencesKey("api_token")
        private val BASE_URL = stringPreferencesKey("base_url")
        private val FIRST_LAUNCH_COMPLETE = booleanPreferencesKey("first_launch_complete")

        private const val PARENTAL_PIN_KEY = "parental_pin"
    }

    // Parental Control Settings
    override suspend fun setParentalPin(pin: String) {
        encryptedPrefs.edit().putString(PARENTAL_PIN_KEY, pin).apply()
    }

    override suspend fun getParentalPin(): String? {
        return encryptedPrefs.getString(PARENTAL_PIN_KEY, null)
    }

    override suspend fun clearParentalPin() {
        encryptedPrefs.edit().remove(PARENTAL_PIN_KEY).apply()
    }

    override suspend fun isPinSet(): Boolean {
        return encryptedPrefs.contains(PARENTAL_PIN_KEY)
    }

    // Screen Time Settings
    override suspend fun setDailyTimeLimit(minutes: Int) {
        dataStore.edit { preferences ->
            preferences[DAILY_TIME_LIMIT] = minutes
        }
    }

    override suspend fun getDailyTimeLimit(): Int {
        return dataStore.data.map { preferences ->
            preferences[DAILY_TIME_LIMIT] ?: -1 // -1 means unlimited
        }.first()
    }

    override suspend fun getTodayWatchTime(): Long {
        checkAndResetDaily()
        return dataStore.data.map { preferences ->
            preferences[TODAY_WATCH_TIME] ?: 0L
        }.first()
    }

    override suspend fun addWatchTime(milliseconds: Long) {
        checkAndResetDaily()
        dataStore.edit { preferences ->
            val current = preferences[TODAY_WATCH_TIME] ?: 0L
            preferences[TODAY_WATCH_TIME] = current + milliseconds
        }
    }

    override suspend fun resetDailyWatchTime() {
        dataStore.edit { preferences ->
            preferences[TODAY_WATCH_TIME] = 0L
            preferences[LAST_RESET_DATE] = System.currentTimeMillis()
        }
    }

    override fun watchTodayWatchTimeFlow(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[TODAY_WATCH_TIME] ?: 0L
        }
    }

    /**
     * Check if we need to reset daily watch time (new day)
     */
    private suspend fun checkAndResetDaily() {
        val lastResetDate = dataStore.data.map { it[LAST_RESET_DATE] ?: 0L }.first()
        val currentTime = System.currentTimeMillis()

        // Check if it's a new day (more than 24 hours)
        if (currentTime - lastResetDate > 24 * 60 * 60 * 1000) {
            resetDailyWatchTime()
        }
    }

    // App Settings
    override suspend fun setTubeArchivistEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[TUBEARCHIVIST_ENABLED] = enabled
        }
    }

    override suspend fun isTubeArchivistEnabled(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[TUBEARCHIVIST_ENABLED] ?: false
        }.first()
    }

    override suspend fun setApiToken(token: String) {
        dataStore.edit { preferences ->
            preferences[API_TOKEN] = token
        }
    }

    override suspend fun getApiToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[API_TOKEN]
        }.first()
    }

    override suspend fun setBaseUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[BASE_URL] = url
        }
    }

    override suspend fun getBaseUrl(): String? {
        return dataStore.data.map { preferences ->
            preferences[BASE_URL]
        }.first()
    }

    override suspend fun setFirstLaunchComplete(complete: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_COMPLETE] = complete
        }
    }

    override suspend fun isFirstLaunchComplete(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[FIRST_LAUNCH_COMPLETE] ?: false
        }.first()
    }
}
