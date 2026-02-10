package com.viewnext.presentation.util

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView

/**
 * Utils para dar estilos a la Factura
 */
object FacturaStyler {
    fun style(textView: TextView, estado: String) {
        when (estado) {
            "Pagada" -> textView.setVisibility(View.GONE)
            "Pendiente de pago" -> {
                textView.setTextColor(Color.RED)
                textView.setTypeface(null, Typeface.NORMAL)
            }

            "Anulada" -> {
                textView.setTextColor(Color.GRAY)
                textView.setTypeface(null, Typeface.ITALIC)
            }

            else -> {
                textView.setTextColor(Color.BLACK)
                textView.setTypeface(null, Typeface.NORMAL)
            }
        }
    }
}
