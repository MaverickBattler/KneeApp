package ru.leti.kneeapp;

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST;

interface PatientDataApiService {
    @POST("/kneeapp/patientData")
    fun addPatientTestResults(@Body patientData: PatientData) : Call<Any>
}