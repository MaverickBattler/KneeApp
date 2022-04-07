package ru.leti.kneeapp.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdVerificationRequestDto(
    @Json(name = "medicalCardId")
    val medicalCardId: String
)