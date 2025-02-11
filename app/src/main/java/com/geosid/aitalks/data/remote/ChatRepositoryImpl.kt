package com.geosid.aitalks.data.remote

import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIHost
import com.geosid.aitalks.constants.DELAY
import com.geosid.aitalks.constants.UNKNOWN_ERROR
import com.geosid.aitalks.constants.baseUrlOpenAI
import com.geosid.aitalks.data.api.ApiState
import com.geosid.aitalks.models.AIModel
import com.geosid.aitalks.models.CompletionsParam
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private lateinit var google: GenerativeModel
    private lateinit var openAI: OpenAI

    override suspend fun completeOpenAiChat(params: CompletionsParam): Flow<ApiState> {
        delay(DELAY)
        openAI = OpenAI(params.token, host = OpenAIHost(baseUrl = baseUrlOpenAI))

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(AIModel.Gpt35Turbo.model),
            messages = messageToOpenAICompatibleMessage(params),
            temperature = params.temperature
        )

        return openAI.chatCompletions(chatCompletionRequest)
            .map<ChatCompletionChunk, ApiState> { chunk ->
                ApiState.Success(
                    chunk.choices.getOrNull(
                        0
                    )?.delta?.content ?: ""
                )
            }
            .catch { throwable -> emit(ApiState.Error(throwable.message ?: UNKNOWN_ERROR)) }
            .onStart { emit(ApiState.Loading) }
            .onCompletion { emit(ApiState.Done) }
    }

    override suspend fun completeGoogleChat(params: CompletionsParam): Flow<ApiState> {
        delay(DELAY)
        /**
         *             topP Values can range from [0.0,1.0], inclusive. A value closer
         *             to 1.0 will produce responses that are more varied and
         *             creative, while a value closer to 0.0 will typically result
         *             in more straightforward responses from the model.
         */
        val config = generationConfig {
            temperature = params.temperature.toFloat()
//            topP = 0.9F
        }
        google = GenerativeModel(
            modelName = params.modelName,
            apiKey = params.token,
            systemInstruction = content { text(params.systemPrompt ?: "") },
            generationConfig = config,
            safetySettings = listOf(
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE)
            )
        )
        val chat = google.startChat()
        return chat.sendMessageStream(params.promptText)
            .map<GenerateContentResponse, ApiState> { response ->
                ApiState.Success(
                    response.text ?: ""
                )
            }
            .catch { throwable -> emit(ApiState.Error(throwable.message ?: UNKNOWN_ERROR)) }
            .onStart { emit(ApiState.Loading) }
            .onCompletion { emit(ApiState.Done) }
    }

    private fun messageToOpenAICompatibleMessage(params: CompletionsParam): List<ChatMessage> {
        val result = mutableListOf<ChatMessage>()
        if(params.assistantPrompt != null) {
            result.add(ChatMessage(role = ChatRole.Assistant, content = params.assistantPrompt))
        }
        if(params.systemPrompt != null) {
            result.add(ChatMessage(role = ChatRole.System, content = params.systemPrompt))
        }
        result.add(ChatMessage(role = ChatRole.User, content = params.promptText))

        return result
    }


}