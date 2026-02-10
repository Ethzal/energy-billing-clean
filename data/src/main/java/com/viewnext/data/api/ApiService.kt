package com.viewnext.data.api

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockCircular
import co.infinum.retromock.meta.MockResponse
import com.viewnext.domain.model.DetallesResponse
import com.viewnext.domain.model.FacturaResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interfaz que define los endpoints de la API que Retrofit utilizará para realizar las solicitudes.
 * Las respuestas de cada endpoint están mapeadas a objetos de tipo [FacturaResponse] o [DetallesResponse].
 */
interface ApiService { // Retrofit genera el código para implementar esta interfaz
    /**
     * Endpoint para obtener las facturas desde la API.
     * @return Un {@link Call} que devolverá una respuesta de tipo {@link FacturaResponse} cuando la solicitud sea exitosa.
     */
    @get:GET("invoices.json")
    val facturas: Call<FacturaResponse?>?

    /**
     * Endpoint simulado usando Retromock para obtener las facturas desde un archivo de recursos local.
     * Se utilizan múltiples respuestas simuladas que se obtienen de archivos JSON ubicados en los assets.
     * @return Un {@link Call} que devolverá una respuesta simulada de tipo {@link FacturaResponse}.
     */
    @get:GET("/")
    @get:MockResponse(body = "facturas_mock3.json")
    @get:MockResponse(body = "facturas_mock2.json")
    @get:MockResponse(body = "facturas_mock.json")
    @get:MockCircular
    @get:Mock
    val facturasMock: Call<FacturaResponse?>?

    /**
     * Endpoint simulado usando Retromock para obtener los detalles desde un archivo de recursos local.
     * Se obtiene una respuesta simulada del archivo JSON correspondiente en los assets.
     * @return Un {@link Call} que devolverá una respuesta simulada de tipo {@link DetallesResponse}.
     */
    @get:GET("/")
    @get:MockResponse(body = "detalles_mock.json")
    @get:Mock
    val detallesMock: Call<DetallesResponse?>?
}
