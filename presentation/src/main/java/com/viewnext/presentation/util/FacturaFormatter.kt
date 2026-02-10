package com.viewnext.presentation.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Utils para dar formato a la moneda y fecha de Factura
 */
public class FacturaFormatter {
    public static String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        return format.format(amount);
    }

    public static String formatFecha(String fecha) {
        try {
            SimpleDateFormat original = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat target = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return target.format(Objects.requireNonNull(original.parse(fecha)));
        } catch (Exception e) {
            return fecha;
        }
    }
}
