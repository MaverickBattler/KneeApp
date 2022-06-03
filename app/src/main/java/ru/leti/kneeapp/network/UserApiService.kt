package ru.leti.kneeapp.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.leti.kneeapp.dto.AuthenticationRequestDto
import ru.leti.kneeapp.dto.RegistrationRequestDto
import ru.leti.kneeapp.dto.UserDataDto

//REST клиент с HTTP-запросами, связанными с пользователем и его личными данными
interface UserApiService {
    //Подтверждение пациента по его ID медицинской карты
    @POST("/verify-patient-id")
    fun verifyMedicalCardId(@Body medicalCardId: RequestBody) : Call<String>

    //Регистрация пользователя
    @POST("/register")
    fun register(@Body registrationRequestDto: RegistrationRequestDto) : Call<String>

    //Вход пользователя в аккаунт
    @POST("/login")
    fun login(@Body authenticationRequestDto: AuthenticationRequestDto) : Call<String>

    //Получение персональных данных пользователя
    @POST("/get-user-data")
    fun getUserData(@Header("Authorization") authHeader: String,
                    @Body email: RequestBody
    ) : Call<UserDataDto>
}