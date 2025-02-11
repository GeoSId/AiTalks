package com.geosid.aitalks.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geosid.aitalks.BuildConfig
import com.geosid.aitalks.constants.ASSISTANT_OPENAI_PROMPT
import com.geosid.aitalks.constants.SYSTEM_GEMINI_PROMPT
import com.geosid.aitalks.constants.SYSTEM_OPENAI_PROMPT_B
import com.geosid.aitalks.constants.SYSTEM_OPENAI_PROMPT_F
import com.geosid.aitalks.data.api.ApiState
import com.geosid.aitalks.data.remote.ChatRepository
import com.geosid.aitalks.models.ApiType
import com.geosid.aitalks.models.CompletionsParam
import com.geosid.aitalks.models.Message
import com.geosid.aitalks.util.handleStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    sealed class LoadingState {
        data object Idle : LoadingState()
        data object Loading : LoadingState()
    }

    private val googleFlow = MutableSharedFlow<ApiState>()
    private val openAIFlow = MutableSharedFlow<ApiState>()

    // Loading state for each platforms
    private val _openaiLoadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val openaiLoadingState = _openaiLoadingState.asStateFlow()

    private val _openAIMessage = MutableStateFlow(Message( content = "", platformType = ApiType.OPENAI))
    val openAIMessage = _openAIMessage.asStateFlow()

    private val _googleLoadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val googleLoadingState = _googleLoadingState.asStateFlow()

    private val _googleMessage = MutableStateFlow(Message(content = "", platformType = ApiType.GOOGLE))
    val googleMessage = _googleMessage.asStateFlow()

    private val _messages = MutableStateFlow(listOf<Message>())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val hasStart = MutableStateFlow(false)

    init {
        observeFlow()
    }

    fun sendToOpenAi(message: String) {
        viewModelScope.launch {
            val params =
                CompletionsParam(
                    name = ApiType.OPENAI,
                    token = BuildConfig.openaiapiKey,
                    promptText = message,
                    systemPrompt = if (hasStart.value) null else SYSTEM_OPENAI_PROMPT_F + message + SYSTEM_OPENAI_PROMPT_B,
                    assistantPrompt = if (hasStart.value) null else ASSISTANT_OPENAI_PROMPT
                )
            val openAiFlow = chatRepository.completeOpenAiChat(params)

            openAiFlow.collect { chunk ->
                openAIFlow.emit(chunk)
            }
        }
    }

    private fun sendToGemini(
        prompt: String
    ) {
        viewModelScope.launch {
            val params  = CompletionsParam(
                name = ApiType.GOOGLE,
                token = BuildConfig.geminiapiKey,
                promptText = prompt,
                systemPrompt = if (hasStart.value) "" else SYSTEM_GEMINI_PROMPT
            )
            val geminiFlow = chatRepository.completeGoogleChat(params = params)
            geminiFlow.collect { chunk ->
                googleFlow.emit(chunk)
            }
        }
    }

    private fun observeFlow() {

        viewModelScope.launch {
            openAIFlow.handleStates(
                messageFlow = _openAIMessage,
                onLoadingComplete = {
                    sendToGemini(_openAIMessage.value.content)
                    updateLoadingState(ApiType.OPENAI, LoadingState.Idle)
                }
            )
        }

        viewModelScope.launch {
            googleFlow.handleStates(
                messageFlow = _googleMessage,
                onLoadingComplete = {
                    hasStart.value = false
                    sendToOpenAi(_googleMessage.value.content)
                    updateLoadingState(ApiType.GOOGLE, LoadingState.Idle)
                }
            )
        }
    }

    private fun clearQuestionAndAnswers() {
        _openAIMessage.update { it.copy(id = "0", content = "") }
        _googleMessage.update { it.copy(id = "0", content = "") }
    }

    private fun syncQuestionAndAnswers(apiType: ApiType,) {
        if(apiType == ApiType.OPENAI) {
            addMessage(_openAIMessage.value)
        }else if(apiType == ApiType.GOOGLE) {
            addMessage(_googleMessage.value)
        }
    }

    private fun addMessage(message: Message) = _messages.update { it + listOf(message) }

    private fun updateLoadingState(apiType: ApiType, loadingState: LoadingState) {
        syncQuestionAndAnswers(apiType)
        clearQuestionAndAnswers()
    }
}