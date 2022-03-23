package ru.leti.kneeapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OKSResult (
    @Json(name = "answer1")
    val answer1: Int,
    @Json(name = "answer2")
    val answer2: Int,
    @Json(name = "answer3")
    val answer3: Int,
    @Json(name = "answer4")
    val answer4: Int,
    @Json(name = "answer5")
    val answer5: Int,
    @Json(name = "answer6")
    val answer6: Int,
    @Json(name = "answer7")
    val answer7: Int,
    @Json(name = "answer8")
    val answer8: Int,
    @Json(name = "answer9")
    val answer9: Int,
    @Json(name = "answer10")
    val answer10: Int,
    @Json(name = "answer11")
    val answer11: Int,
    @Json(name = "answer12")
    val answer12: Int
)