package com.kiagent.backend

class BackendManager {

    private var backend: ModelBackend? = null

    fun setBackend(modelBackend: ModelBackend) {
        backend = modelBackend
    }

    fun currentBackend(): ModelBackend? = backend
}
