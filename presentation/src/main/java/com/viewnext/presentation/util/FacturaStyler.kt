package com.viewnext.presentation.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

/**
 * Utils para dar estilos a la Factura
 */
public class FacturaStyler {
    public static void style(TextView textView, String estado) {
        switch (estado) {
            case "Pagada":
                textView.setVisibility(View.GONE);
                break;
            case "Pendiente de pago":
                textView.setTextColor(Color.RED);
                textView.setTypeface(null, Typeface.NORMAL);
                break;
            case "Anulada":
                textView.setTextColor(Color.GRAY);
                textView.setTypeface(null, Typeface.ITALIC);
                break;
            default:
                textView.setTextColor(Color.BLACK);
                textView.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }
}
