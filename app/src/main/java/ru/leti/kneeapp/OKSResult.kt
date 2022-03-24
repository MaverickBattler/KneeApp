package ru.leti.kneeapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OKSResult (
    @Json(name = "answer1")
    val answer1: Short,
    @Json(name = "answer2")
    val answer2: Short,
    @Json(name = "answer3")
    val answer3: Short,
    @Json(name = "answer4")
    val answer4: Short,
    @Json(name = "answer5")
    val answer5: Short,
    @Json(name = "answer6")
    val answer6: Short,
    @Json(name = "answer7")
    val answer7: Short,
    @Json(name = "answer8")
    val answer8: Short,
    @Json(name = "answer9")
    val answer9: Short,
    @Json(name = "answer10")
    val answer10: Short,
    @Json(name = "answer11")
    val answer11: Short,
    @Json(name = "answer12")
    val answer12: Short
)