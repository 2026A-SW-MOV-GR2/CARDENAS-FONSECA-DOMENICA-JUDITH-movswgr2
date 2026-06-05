package org.example.project.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.data.model.PostPayload

class PostRepository(private val client: HttpClient) {
    private val baseUrl = "https://jsonplaceholder.typicode.com"

    suspend fun fetchPost(id: Int): PostPayload {
        return client.get("$baseUrl/posts/$id").body()
    }

    suspend fun updatePost(id: Int, title: String, body: String): PostPayload {
        val payload = PostPayload(
            userId = 1,
            id = id,
            title = title,
            body = body
        )

        val response: HttpResponse = client.put("$baseUrl/posts/$id") {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }

        if (response.status.value != 200) {
            error("Unexpected status ${response.status.value}")
        }

        return response.body()
    }

    fun close() {
        client.close()
    }
}
