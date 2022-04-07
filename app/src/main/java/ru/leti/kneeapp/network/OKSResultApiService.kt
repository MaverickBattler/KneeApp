package ru.leti.kneeapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.leti.kneeapp.OKSResult

interface OKSResultApiService {
    //@POST("/kneeapp/oksResult")
    @POST("/oksResult")
    fun addOKSResult(@Body oksResult: OKSResult) : Call<Any>
}