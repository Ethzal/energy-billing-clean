package com.viewnext.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Clase que configura la instancia de Retrofit para realizar las solicitudes de red.
 * Se usa para acceder a los servicios de la API de forma centralizada.
 */
object RetrofitClient {

    // URL base de la API
    private const val BASE_URL = "https://francisco-pacheco.com/api/"

    // Instancia única de Retrofit
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Devuelve una instancia de la interfaz [ApiService] que se utiliza para realizar las solicitudes HTTP.
     * Esta interfaz contiene los métodos definidos para la API.
     *
     * @return La instancia de [ApiService] para hacer solicitudes a la API.
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
