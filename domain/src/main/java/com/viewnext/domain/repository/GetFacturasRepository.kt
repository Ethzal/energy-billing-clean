package com.viewnext.domain.repository

import com.viewnext.domain.model.Factura
import kotlinx.coroutines.flow.Flow

interface GetFacturasRepository {

    /**
     * Flow reactivo.
     */
    fun getFacturas(): Flow<List<Factura>>

    /**
     * Refresca las facturas desde la fuente de datos.
     */
    suspend fun refreshFacturas(usingRetromock: Boolean): Result<List<Factura>>
}
