package com.geosid.aitalks.data.api

sealed class ApiState {
    data object Loading : ApiState()
    data class Success(val textChunk: String) : ApiState()
    data class Error(val message: String) : ApiState()
    data object Done : ApiState()
}
