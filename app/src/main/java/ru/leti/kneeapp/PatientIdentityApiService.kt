package ru.leti.kneeapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientIdentityApiService {
    //@POST("/kneeapp/verify")
    @POST("/verify")
    fun authenticate(@Body authenticationRequestDto: AuthenticationRequestDto) : Call<String>
}