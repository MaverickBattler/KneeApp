package ru.leti.kneeapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OKSResultApiService {
    //@POST("/kneeapp/oksResult")
    @POST("/oksResult")
    fun addOKSResult(@Body oksResult: OKSResult) : Call<Any>
}