package com.kiagent.config

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "agent_settings")

class AgentPreferences(private val context: Context) {

    private object Keys {
        val PROVIDER = stringPreferencesKey("provider")
        val API_KEY = stringPreferencesKey("api_key")
        val ENDPOINT = stringPreferencesKey("endpoint")
        val MODEL = stringPreferencesKey("model")
        val LOCAL_MODEL_PATH = stringPreferencesKey("local_model_path")
    }

    val config: Flow<AgentConfig> = context.dataStore.data.map { prefs ->
        AgentConfig(
            provider = runCatching {
                Provider.valueOf(prefs[Keys.PROVIDER] ?: Provider.LOCAL.name)
            }.getOrDefault(Provider.LOCAL),
            apiKey = prefs[Keys.API_KEY] ?: "",
            endpoint = prefs[Keys.ENDPOINT] ?: "",
            model = prefs[Keys.MODEL] ?: "",
            localModelPath = prefs[Keys.LOCAL_MODEL_PATH] ?: ""
        )
    }

    suspend fun saveProvider(value: Provider) {
        context.dataStore.edit { it[Keys.PROVIDER] = value.name }
    }

    suspend fun saveApiKey(value: String) {
        context.dataStore.edit { it[Keys.API_KEY] = value }
    }

    suspend fun saveEndpoint(value: String) {
        context.dataStore.edit { it[Keys.ENDPOINT] = value }
    }

    suspend fun saveModel(value: String) {
        context.dataStore.edit { it[Keys.MODEL] = value }
    }

    suspend fun saveLocalModelPath(value: String) {
        context.dataStore.edit { it[Keys.LOCAL_MODEL_PATH] = value }
    }
}
