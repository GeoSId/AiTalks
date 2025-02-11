package com.geosid.aitalks.models

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class CompletionsParam(
    val name: ApiType,
    val promptText: String = "",
    val selected: Boolean = false,
    val enabled: Boolean = false,
    val n: Int = 1,
    var stream: Boolean = true,
    val token: String = "",
    val maxTokens: Int = 10,
    val temperature: Double = 0.8,
    val topP: Double? = 0.0,
    val systemPrompt: String? = null,
    val assistantPrompt: String? = null,
    val model: AIModel = AIModel.Gpt35Turbo,
    val modelName: String = AIModel.Gemini15Flash.model,
): Parcelable