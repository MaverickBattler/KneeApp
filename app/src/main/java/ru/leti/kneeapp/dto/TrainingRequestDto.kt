package ru.leti.kneeapp.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingRequestDto(
    @Json(name = "email")
    val email: String,
    @Json(name = "lastOks")
    val lastOks: Long
)