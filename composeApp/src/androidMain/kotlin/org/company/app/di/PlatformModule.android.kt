package org.company.app.di

import org.koin.core.module.Module

actual fun getPlatformModules(): List<Module> {
    return listOf(androidTvModule)
}
