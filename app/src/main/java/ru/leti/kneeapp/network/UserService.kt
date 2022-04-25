package ru.leti.kneeapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.leti.kneeapp.dto.AuthenticationRequestDto
import ru.leti.kneeapp.dto.IdVerificationRequestDto
import ru.leti.kneeapp.dto.RegistrationRequestDto

interface UserService {
    @POST("/verify-patient-id")
    fun verifyMedicalCardId(@Body idVerificationRequestDto: IdVerificationRequestDto) : Call<String>

    @POST("/register")
    fun register(@Body registrationRequestDto: RegistrationRequestDto) : Call<String>

    @POST("/login")
    fun login(@Body authenticationRequestDto: AuthenticationRequestDto) : Call<String>
}