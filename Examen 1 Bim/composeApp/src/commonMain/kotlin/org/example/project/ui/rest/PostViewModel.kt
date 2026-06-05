package org.example.project.ui.rest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.repository.PostRepository

data class PostUiState(
    val postIdInput: String = "",
    val title: String = "",
    val body: String = "",
    val isLoading: Boolean = false,
    val statusMessage: String? = null,
    val errorMessage: String? = null
)

class PostViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun onPostIdChange(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(postIdInput = filtered) }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onBodyChange(value: String) {
        _uiState.update { it.copy(body = value) }
    }

    fun fetchPost() {
        val id = parseId() ?: run {
            _uiState.update { it.copy(errorMessage = "ID invalido", statusMessage = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, statusMessage = null, errorMessage = null) }
            runCatching { repository.fetchPost(id) }
                .onSuccess { post ->
                    _uiState.update {
                        it.copy(
                            title = post.title,
                            body = post.body,
                            isLoading = false,
                            statusMessage = "Serie cargada"
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al obtener la serie"
                        )
                    }
                }
        }
    }

    fun updatePost() {
        val id = parseId() ?: run {
            _uiState.update { it.copy(errorMessage = "ID invalido", statusMessage = null) }
            return
        }

        val title = uiState.value.title.trim()
        val body = uiState.value.body.trim()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, statusMessage = null, errorMessage = null) }
            runCatching { repository.updatePost(id, title, body) }
                .onSuccess { post ->
                    _uiState.update {
                        it.copy(
                            title = post.title,
                            body = post.body,
                            isLoading = false,
                            statusMessage = "Serie actualizada"
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al actualizar la serie"
                        )
                    }
                }
        }
    }

    override fun onCleared() {
        repository.close()
        super.onCleared()
    }

    private fun parseId(): Int? {
        return uiState.value.postIdInput.trim().toIntOrNull()
    }
}
