package com.viewnext.presentation.composables.factura

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerDialogHelper(
    context: Context,
    currentDate: String,
    isInicio: Boolean,
    fechaInicio: String? = null,
    fechaFin: String? = null,
    onDateSelected: (String) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Cargar fecha actual si existe
    currentDate.takeIf { it != "día/mes/año" }?.let {
        sdf.parse(it)?.let { date -> calendar.time = date }
    }

    DisposableEffect(Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }

                // Validaciones (igual que Fragment)
                if (isInicio && fechaFin != null && fechaFin != "día/mes/año") {
                    sdf.parse(fechaFin)?.let { finDate ->
                        if (selectedCalendar.after(Calendar.getInstance().apply { time = finDate })) {
                            Toast.makeText(context, "Fecha inicio > fecha fin", Toast.LENGTH_SHORT).show()
                            return@DatePickerDialog
                        }
                    }
                } else if (!isInicio && fechaInicio != null && fechaInicio != "día/mes/año") {
                    sdf.parse(fechaInicio)?.let { inicioDate ->
                        if (selectedCalendar.before(Calendar.getInstance().apply { time = inicioDate })) {
                            Toast.makeText(context, "Fecha fin < fecha inicio", Toast.LENGTH_SHORT).show()
                            return@DatePickerDialog
                        }
                    }
                }

                onDateSelected(sdf.format(selectedCalendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()

        onDispose { }
    }
}

