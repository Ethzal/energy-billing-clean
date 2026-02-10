package com.viewnext.data.api

import android.content.Context
import co.infinum.retromock.BodyFactory
import co.infinum.retromock.Retromock
import java.io.IOException
import java.io.InputStream

/**
 * Clase para configurar y gestionar la instancia de Retromock.
 * Retromock se utiliza para simular respuestas de la API de manera local
 * usando archivos de recursos, como los que se encuentran en el directorio "assets".
 */
class RetromockClient private constructor() {

    companion object {
        @Volatile
        private var retromock: Retromock? = null

        /**
         * Devuelve una instancia de Retromock configurada para usarse en Retrofit.
         * Utiliza un patrón de sincronización doble para garantizar que la instancia
         * solo se crea una vez, incluso si se accede desde múltiples hilos.
         *
         * @param context El contexto de la aplicación, utilizado para acceder a los recursos.
         * @return La instancia de Retromock configurada.
         */
        fun getRetromockInstance(context: Context): Retromock {
            return retromock ?: synchronized(this) {
                retromock ?: Retromock.Builder()
                    .retrofit(RetrofitClient.retrofit)
                    .defaultBodyFactory(ResourceBodyFactory(context))
                    .build()
                    .also { retromock = it }
            }
        }
    }

    /**
     * Implementación de la interfaz [BodyFactory] de Retromock para definir cómo generar el cuerpo de la respuesta
     * de los mocks, cargando los archivos correspondientes desde los recursos "assets".
     */
    private class ResourceBodyFactory(private val context: Context) : BodyFactory {

        /**
         * Método principal de [BodyFactory].
         * @param input Nombre del archivo mock solicitado.
         * @return InputStream del archivo mock correspondiente en assets o null si no existe.
         */
        @Throws(IOException::class)
        override fun create(input: String): InputStream? {
            return when (input) {
                "facturas_mock.json" -> context.assets.open("facturas_mock.json")
                "facturas_mock2.json" -> context.assets.open("facturas_mock2.json")
                "facturas_mock3.json" -> context.assets.open("facturas_mock3.json")
                "detalles_mock.json" -> context.assets.open("detalles_mock.json")
                else -> null
            }
        }
    }
}
