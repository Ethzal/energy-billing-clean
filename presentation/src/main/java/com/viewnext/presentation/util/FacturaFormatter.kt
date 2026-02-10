package com.viewnext.presentation.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

/**
 * Utils para dar formato a la moneda y fecha de Factura
 */
object FacturaFormatter {
    fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        return format.format(amount)
    }

    fun formatFecha(fecha: String): String? {
        try {
            val original = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val target = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return target.format(Objects.requireNonNull<Date?>(original.parse(fecha)))
        } catch (_: Exception) {
            return fecha
        }
    }
}
