package ru.leti.kneeapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.leti.kneeapp.dto.OksResultDto

interface OksResultApiService {
    @POST("/oksResult")
    fun addOksResult(@Header("Authorization") authHeader: String,
                     @Body oksResultDto: OksResultDto) : Call<Long>
}