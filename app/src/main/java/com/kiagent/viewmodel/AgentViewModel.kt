package com.kiagent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kiagent.backend.BackendRouter
import com.kiagent.config.AgentConfig
import com.kiagent.config.AgentPreferences
import com.kiagent.config.Provider
import com.kiagent.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AgentViewModel(
    private val preferences: AgentPreferences
) : ViewModel() {

    private val _config = MutableStateFlow(AgentConfig())
    val config = _config.asStateFlow()

    private val _messages = MutableStateFlow(
        listOf(ChatMessage("assistant", "Bereit. Wähle LOCAL oder REMOTE."))
    )
    val messages = _messages.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy = _busy.asStateFlow()

    private val router = BackendRouter { _config.value }

    init {
        viewModelScope.launch {
            preferences.config.collect { loaded ->
                _config.value = loaded
            }
        }
    }

    fun send(text: String) {
        val prompt = text.trim()
        if (prompt.isBlank() || _busy.value) return

        viewModelScope.launch {
            _busy.value = true
            _messages.value = _messages.value + ChatMessage("user", prompt)

            try {
                val reply = router.generate(_messages.value)
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

    fun setProvider(value: Provider) {
        viewModelScope.launch { preferences.saveProvider(value) }
    }

    fun setApiKey(value: String) {
        viewModelScope.launch { preferences.saveApiKey(value) }
    }

    fun setEndpoint(value: String) {
        viewModelScope.launch { preferences.saveEndpoint(value) }
    }

    fun setModel(value: String) {
        viewModelScope.launch { preferences.saveModel(value) }
    }

    fun setLocalModelPath(value: String) {
        viewModelScope.launch { preferences.saveLocalModelPath(value) }
    }
}

class AgentViewModelFactory(
    private val preferences: AgentPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AgentViewModel(preferences) as T
    }
}
