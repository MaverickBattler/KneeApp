package ru.leti.kneeapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.leti.kneeapp.dto.OksResultDto
import ru.leti.kneeapp.dto.TrainingDto
import ru.leti.kneeapp.dto.TrainingRequestDto

//REST клиент с HTTP-запросами, связанными с результатами работы пользователя
interface ResultsApiService {
    //Отправка результата анкеты OKS для сохранения на сервер
    @POST("/oksResult")
    fun addOksResult(
        @Header("Authorization") authHeader: String,
        @Body oksResultDto: OksResultDto
    ): Call<Long>

    //Отправка результата тренировок для сохранения на сервер
    @POST("/training")
    fun addTraining(
        @Header("Authorization") authHeader: String,
        @Body trainingDto: TrainingDto
    ): Call<Unit>

    //Получение тренировок пользователя за последние 7 дней
    @POST("/get-trainings")
    fun getTrainings(
        @Header("Authorization") authHeader: String,
        @Body trainingRequestDto: TrainingRequestDto
    ): Call<List<List<Boolean>>>
}