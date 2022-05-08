package ru.leti.kneeapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.leti.kneeapp.dto.OksResultDto
import ru.leti.kneeapp.dto.TrainingDto
import ru.leti.kneeapp.dto.TrainingRequestDto

interface ResultsApiService {
    @POST("/oksResult")
    fun addOksResult(
        @Header("Authorization") authHeader: String,
        @Body oksResultDto: OksResultDto
    ): Call<Long>

    @POST("/training")
    fun addTraining(
        @Header("Authorization") authHeader: String,
        @Body trainingDto: TrainingDto
    ): Call<Unit>

    @POST("/get-trainings")
    fun getTrainings(
        @Header("Authorization") authHeader: String,
        @Body trainingRequestDto: TrainingRequestDto
    ): Call<List<List<Boolean>>>
}