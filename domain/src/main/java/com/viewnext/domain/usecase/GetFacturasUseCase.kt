package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import com.viewnext.domain.repository.GetFacturasRepository

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
    suspend operator fun invoke(usingRetromock: Boolean): Result<List<Factura>> =
        repository.refreshFacturas(usingRetromock)
}
