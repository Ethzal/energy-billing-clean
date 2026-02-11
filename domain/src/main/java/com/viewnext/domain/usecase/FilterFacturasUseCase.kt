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
        facturas: MutableList<Factura>,
        estadosSeleccionados: MutableList<String?>?,
        fechaInicioString: String?,
        fechaFinString: String?,
        importeMin: Double?,
        importeMax: Double?
    ): MutableList<Factura?> {
        // Convertir las fechas de String a Date

        val fechaInicio = stringToDate(fechaInicioString)
        val fechaFin = stringToDate(fechaFinString)

        // Crear una lista para las facturas filtradas
        val facturasFiltradas: MutableList<Factura?> = ArrayList()

        // Iterar sobre todas las facturas y aplicar los filtros
        for (factura in facturas) {
            val cumpleEstado =
                (estadosSeleccionados == null || estadosSeleccionados.isEmpty() || estadosSeleccionados.contains(
                    factura.descEstado
                ))
            var cumpleFecha = true

            // Fecha INICIO
            if (fechaInicio != null && factura.fecha.isNotEmpty()) {
                stringToDate(factura.fecha)?.let { fechaFactura ->
                    cumpleFecha = fechaFactura >= fechaInicio
                }
            }

            // Fecha FIN
            if (fechaFin != null && factura.fecha.isNotEmpty()) {
                stringToDate(factura.fecha)?.let { fechaFactura ->
                    cumpleFecha = cumpleFecha && fechaFactura <= fechaFin
                }
            }

            var cumpleImporte = true

            if (importeMin != null) {
                cumpleImporte = cumpleImporte and (factura.importeOrdenacion >= importeMin)
            }

            if (importeMax != null) {
                cumpleImporte = cumpleImporte and (factura.importeOrdenacion <= importeMax)
            }

            // Si la factura cumple con todos los filtros, agregarla a la lista filtrada
            if (cumpleEstado && cumpleFecha && cumpleImporte) {
                facturasFiltradas.add(factura)
            }
        }

        return facturasFiltradas
    }
}
