package org.example.project.ui

sealed interface Screen {
    data object List : Screen
    data class Form(val bookId: Long?) : Screen
}
