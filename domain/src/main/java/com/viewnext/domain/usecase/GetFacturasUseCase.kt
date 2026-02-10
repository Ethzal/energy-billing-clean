package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import com.viewnext.domain.repository.GetFacturasRepository
import com.viewnext.domain.repository.GetFacturasRepository.RepositoryCallback

/**
 * Caso de uso para obtener la lista de facturas desde el repositorio.
 * Encapsula la lógica de negocio para la recuperación de facturas,
 * permitiendo diferenciar entre fuentes de datos (Retrofit o Retromock).
 */
class GetFacturasUseCase
/**
 * Constructor del caso de uso.
 * @param repository Repositorio que maneja la obtención de facturas
 */(private val repository: GetFacturasRepository) {
    // Callback para notificar el resultado de la ejecución del caso de uso
    interface Callback {
        fun onSuccess(facturas: MutableList<Factura?>?)
        fun onError(error: String?)
    }

    /**
     * Ejecuta el caso de uso para obtener las facturas.
     * @param usingRetromock Si es true, usa datos simulados; si es false, usa la API real
     * @param callback Callback que será notificado con los resultados
     */
    fun execute(usingRetromock: Boolean, callback: Callback) {
        repository.refreshFacturas(usingRetromock, object : RepositoryCallback {
            override fun onSuccess(facturas: List<Factura>) {
                callback.onSuccess(facturas.toMutableList())
            }

            override fun onError(error: Throwable) {
                callback.onError(error.message)
            }
        })
    }

}
