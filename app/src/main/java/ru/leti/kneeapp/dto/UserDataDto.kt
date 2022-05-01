package ru.leti.kneeapp.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDataDto (
    @Json(name = "medicalCardId")
    val medicalCardId: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "fatherName")
    val fatherName: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "doctorEmail")
    val doctorEmail: String
)