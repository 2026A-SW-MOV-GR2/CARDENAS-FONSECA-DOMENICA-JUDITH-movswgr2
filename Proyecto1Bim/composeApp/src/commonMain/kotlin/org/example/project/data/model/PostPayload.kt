package org.example.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PostPayload(
    val userId: Int = 1,
    val id: Int,
    val title: String,
    val body: String
)
