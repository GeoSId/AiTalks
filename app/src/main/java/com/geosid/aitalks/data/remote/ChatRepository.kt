package com.geosid.aitalks.data.remote

import com.geosid.aitalks.data.api.ApiState
import com.geosid.aitalks.models.CompletionsParam
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun completeOpenAiChat(params: CompletionsParam): Flow<ApiState>
    suspend fun completeGoogleChat(params: CompletionsParam): Flow<ApiState>
}