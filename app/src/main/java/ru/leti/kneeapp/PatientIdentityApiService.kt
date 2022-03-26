package ru.leti.kneeapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientIdentityApiService {
    @POST("/kneeapp/auth")
    fun authenticate(@Body authenticationRequestDto: AuthenticationRequestDto) : Call<String>
}