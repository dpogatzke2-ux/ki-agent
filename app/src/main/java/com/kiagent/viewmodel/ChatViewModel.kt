package com.kiagent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kiagent.backend.ModelBackend
import com.kiagent.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val backend: ModelBackend
) : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(ChatMessage("assistant", "Bereit. Schreib eine Nachricht."))
    )
    val messages = _messages.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy = _busy.asStateFlow()

    fun send(text: String) {
        val prompt = text.trim()
        if (prompt.isBlank() || _busy.value) return

        viewModelScope.launch {
            _busy.value = true
            _messages.value = _messages.value + ChatMessage("user", prompt)

            try {
                val reply = backend.generate(_messages.value)
                _messages.value = _messages.value + ChatMessage("assistant", reply)
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    "assistant",
                    "Fehler: ${e.message ?: "unbekannt"}"
                )
            } finally {
                _busy.value = false
            }
        }
    }
}

class ChatViewModelFactory(
    private val backend: ModelBackend
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(backend) as T
    }
}
