package org.example.project.model

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val summary: String,
    val publishedDate: String,
    val isRead: Boolean,
    val coverTone: CoverTone,
)
