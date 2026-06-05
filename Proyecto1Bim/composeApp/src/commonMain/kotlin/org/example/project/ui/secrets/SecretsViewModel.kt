package org.example.project.ui.secrets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.storage.SecretsStorage
import org.example.project.data.storage.StorageEngine

data class SecretsUiState(
    val key: String = "",
    val value: String = "",
    val engine: StorageEngine = StorageEngine.SharedPreferences,
    val isBusy: Boolean = false,
    val statusMessage: String? = null
)

sealed interface SecretsEvent {
    data class ShowSnackbar(val message: String) : SecretsEvent
}

class SecretsViewModel(
    private val storage: SecretsStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecretsUiState())
    val uiState: StateFlow<SecretsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SecretsEvent>()
    val events = _events.asSharedFlow()

    fun onKeyChange(value: String) {
        _uiState.update { it.copy(key = value) }
    }

    fun onValueChange(value: String) {
        _uiState.update { it.copy(value = value) }
    }

    fun onEngineChange(engine: StorageEngine) {
        _uiState.update { it.copy(engine = engine) }
    }

    fun save() {
        val key = uiState.value.key.trim()
        if (key.isEmpty()) {
            emitMessage("Llave requerida")
            return
        }

        val value = uiState.value.value

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true, statusMessage = null) }
            runCatching { storage.save(key, value, uiState.value.engine) }
                .onSuccess {
                    _uiState.update { it.copy(isBusy = false, statusMessage = "Secreto guardado") }
                }
                .onFailure {
                    _uiState.update { it.copy(isBusy = false) }
                    emitMessage("Error al guardar")
                }
        }
    }

    fun load() {
        val key = uiState.value.key.trim()
        if (key.isEmpty()) {
            emitMessage("Llave requerida")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true, statusMessage = null) }
            runCatching { storage.load(key, uiState.value.engine) }
                .onSuccess { value ->
                    if (value == null) {
                        _uiState.update { it.copy(isBusy = false) }
                        emitMessage("Secreto no encontrado")
                    } else {
                        _uiState.update {
                            it.copy(
                                value = value,
                                isBusy = false,
                                statusMessage = "Secreto recuperado"
                            )
                        }
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isBusy = false) }
                    emitMessage("Error al recuperar")
                }
        }
    }

    private fun emitMessage(message: String) {
        viewModelScope.launch {
            _events.emit(SecretsEvent.ShowSnackbar(message))
        }
    }
}
