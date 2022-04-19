package ru.leti.kneeapp.network

import okhttp3.OkHttpClient

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class UnsafeOkHttpClient {
    // Создаем trust manager который не валидирует certificate chains
    // Используется только для разработки, так как это решение небезопасно
    // В production версии предполагается, что у сервера будет подписанный CA сертификат
    companion object {

        fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
            val naiveTrustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            }

            val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
                val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
                init(null, trustAllCerts, SecureRandom())
            }.socketFactory

            sslSocketFactory(insecureSocketFactory, naiveTrustManager)
            hostnameVerifier { _, _ -> true }
            return this
        }
    }
}