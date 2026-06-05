package org.example.project.data.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
