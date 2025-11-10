package org.company.app.di

import org.company.app.data.local.preferences.TvPreferencesManager
import org.company.app.data.local.preferences.TvPreferencesManagerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific Koin module for TV features
 */
val androidTvModule = module {

    // TV Preferences Manager
    single<TvPreferencesManager> {
        TvPreferencesManagerFactory(androidContext()).create()
    }
}
