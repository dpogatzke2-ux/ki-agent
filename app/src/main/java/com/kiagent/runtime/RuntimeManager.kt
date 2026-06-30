package com.kiagent.runtime

import com.kiagent.model.LocalModel
import com.kiagent.model.ModelFormat

object RuntimeManager {

    private val runtimes = listOf<ModelRuntime>(
        GgufRuntime(),
        MnnRuntime(),
        OnnxRuntime(),
        LiteRtRuntime(),
        ExecuTorchRuntime()
    )

    fun runtimeFor(model: LocalModel): ModelRuntime? {
        return runtimes.firstOrNull {
            it.supports(model)
        }
    }

    fun supports(format: ModelFormat): Boolean {
        return runtimes.any {
            it.format == format
        }
    }

    fun installedFormats(): List<ModelFormat> {
        return runtimes.map {
            it.format
        }
    }
}
