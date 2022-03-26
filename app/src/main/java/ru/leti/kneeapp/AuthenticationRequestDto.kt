package ru.leti.kneeapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationRequestDto(
    @Json(name = "medicalCardNumber")
    val medicalCardNumber: String
)