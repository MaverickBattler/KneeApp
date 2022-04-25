package ru.leti.kneeapp.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationRequestDto(
    @Json(name = "medicalCardId")
    var medicalCardId: String,
    @Json(name = "password")
    var password: String
)
