package com.viewnext.domain.repository

import com.viewnext.domain.model.Factura

interface GetFacturasRepository {

    /**
     * Obtiene las facturas almacenadas localmente.
     */
    fun getFacturas(): List<Factura>

    /**
     * Refresca las facturas desde la fuente de datos.
     */
    fun refreshFacturas(usingRetromock: Boolean, callback: RepositoryCallback)

    interface RepositoryCallback {
        fun onSuccess(facturas: List<Factura>)
        fun onError(error: Throwable)
    }
}
