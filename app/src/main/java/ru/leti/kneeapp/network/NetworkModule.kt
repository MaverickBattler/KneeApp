package ru.leti.kneeapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.leti.kneeapp.network.UnsafeOkHttpClient.Companion.ignoreAllSSLErrors
import java.util.concurrent.TimeUnit

//Объект, в котором конфигурируется работа с сетью
object NetworkModule {
    private const val baseUrl = "https://192.168.1.40:8443/"
    //Interceptor для логирования
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    //Создание OkHttpClient
    private val httpClient = OkHttpClient.Builder().ignoreAllSSLErrors()
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    //Создание Moshi
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    //Создание Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()
    //Создание REST клиентов
    val resultsApiService: ResultsApiService = retrofit.create()
    val userApiService: UserApiService = retrofit.create()
}