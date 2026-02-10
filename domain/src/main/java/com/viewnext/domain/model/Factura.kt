package com.viewnext.domain.model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class Factura(
    val descEstado: String = "Sin estado",
    val importeOrdenacion: Double = 0.0,
    val fecha: String = ""
) {

    companion object {
        @JvmStatic
        fun stringToDate(fechaString: String?): Date? {
            if (fechaString.isNullOrEmpty() || fechaString == "día/mes/año") {
                return null
            }

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return try {
                sdf.parse(fechaString)
            } catch (_: ParseException) {
                null
            }
        }
    }
}
