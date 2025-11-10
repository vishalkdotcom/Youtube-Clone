package org.company.app.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.company.app.data.remote.tubearchivist.TubeArchivistApi
import org.company.app.data.repository.TubeArchivistRepositoryImpl
import org.company.app.domain.repository.TubeArchivistRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for TubeArchivist dependencies
 */
val tubeArchivistModule = module {

    // Separate HttpClient for TubeArchivist API
    single(named("TubeArchivistClient")) {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        explicitNulls = false
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Application.Json
                )
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("[TubeArchivist] $message")
                    }
                }
            }

            install(HttpTimeout) {
                connectTimeoutMillis = 30000
                requestTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }

            // Don't set default base URL here, TubeArchivistApi handles it
        }
    }

    // TubeArchivist API
    single {
        TubeArchivistApi(
            client = get(named("TubeArchivistClient")),
            baseUrl = "https://ta.vishalk.com",
            apiToken = "ddb865bf6f8970f8b52283a09f939316eb17c66d"
        )
    }

    // TubeArchivist Repository
    single<TubeArchivistRepository> {
        TubeArchivistRepositoryImpl(api = get())
    }
}
