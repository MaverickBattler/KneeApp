package ru.leti.kneeapp.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.leti.kneeapp.dto.AuthenticationRequestDto
import ru.leti.kneeapp.dto.RegistrationRequestDto
import ru.leti.kneeapp.dto.UserDataDto

interface UserService {
    @POST("/verify-patient-id")
    fun verifyMedicalCardId(@Body medicalCardId: RequestBody) : Call<String>

    @POST("/register")
    fun register(@Body registrationRequestDto: RegistrationRequestDto) : Call<String>

    @POST("/login")
    fun login(@Body authenticationRequestDto: AuthenticationRequestDto) : Call<String>

    @POST("/get-user-data")
    fun getUserData(@Header("Authorization") authHeader: String,
                    @Body email: RequestBody
    ) : Call<UserDataDto>
}