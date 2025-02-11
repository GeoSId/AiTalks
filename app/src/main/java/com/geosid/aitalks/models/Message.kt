package com.geosid.aitalks.models

import android.os.Parcelable
import java.util.*

@kotlinx.parcelize.Parcelize
data class Message (
    var id: String = Date().time.toString(),
    var question: String = "",
    var answer: String = "",
    val content: String,
    val platformType: ApiType?,
    val createdAt: Long = System.currentTimeMillis() / 1000
): Parcelable