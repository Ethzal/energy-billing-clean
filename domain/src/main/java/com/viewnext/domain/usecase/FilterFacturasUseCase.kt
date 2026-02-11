package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import com.viewnext.domain.model.Factura.Companion.stringToDate

/**
 * Caso de uso que filtra una lista de facturas según criterios de estado, fecha e importe.
 * Encapsula la lógica de filtrado utilizada en la presentación de facturas.
 */
class FilterFacturasUseCase {
    /**
     * Filtra las facturas según los criterios proporcionados.
     * @param facturas           Lista completa de facturas a filtrar
     * @param estadosSeleccionados Lista de estados a incluir; si es nula o vacía, no filtra por estado
     * @param fechaInicioString  Fecha de inicio en formato String ("dd/MM/yyyy"); puede ser nula o vacía
     * @param fechaFinString     Fecha de fin en formato String ("dd/MM/yyyy"); puede ser nula o vacía
     * @param importeMin         Importe mínimo a incluir; puede ser nulo
     * @param importeMax         Importe máximo a incluir; puede ser nulo
     * @return Lista de facturas que cumplen con todos los filtros
     */
    fun filtrarFacturas(
        facturas: List<Factura>,
        estadosSeleccionados: List<String?>? = null,
        fechaInicioString: String? = null,
        fechaFinString: String? = null,
        importeMin: Double? = null,
        importeMax: Double? = null
    ): List<Factura> {
        val fechaInicio = stringToDate(fechaInicioString)
        val fechaFin = stringToDate(fechaFinString)

        return facturas.filter { factura ->
            // Filtrado por estado
            val estadoOk = estadosSeleccionados.isNullOrEmpty() || estadosSeleccionados.contains(factura.descEstado)

            // Filtrado por fecha
            val fechaFactura = factura.fecha.takeIf { it.isNotEmpty() }?.let { stringToDate(it) }
            val fechaOk = (fechaInicio == null || (fechaFactura?.let { it >= fechaInicio } ?: false)) &&
                    (fechaFin == null || (fechaFactura?.let { it <= fechaFin } ?: false))

            // Filtrado por importe
            val importeOk = (importeMin == null || factura.importeOrdenacion >= importeMin) &&
                    (importeMax == null || factura.importeOrdenacion <= importeMax)

            estadoOk && fechaOk && importeOk
        }
    }
}
