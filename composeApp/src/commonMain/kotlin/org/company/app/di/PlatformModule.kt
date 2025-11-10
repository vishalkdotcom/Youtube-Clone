package org.company.app.di

import org.koin.core.module.Module

/**
 * Expect/actual pattern for platform-specific Koin modules
 */
expect fun getPlatformModules(): List<Module>
