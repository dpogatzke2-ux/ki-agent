package com.kiagent.scanner

import com.kiagent.model.ModelFormat
import java.io.File

object ModelScanner {

    fun detectFormat(file: File): ModelFormat {

        val name = file.name.lowercase()

        return when {

            name.endsWith(".gguf") ->
                ModelFormat.GGUF

            name.endsWith(".safetensors") ->
                ModelFormat.SAFETENSORS

            name.endsWith(".mnn") ->
                ModelFormat.MNN

            name.endsWith(".onnx") ->
                ModelFormat.ONNX

            name.endsWith(".tflite") ->
                ModelFormat.LITERT

            name.endsWith(".pte") ->
                ModelFormat.EXECUTORCH

            else ->
                ModelFormat.UNKNOWN
        }
    }

}
