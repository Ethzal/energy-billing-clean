package com.viewnext.presentation.composables.factura

import android.widget.Toast
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePickerDialogComposable(
    currentDate: String,
    isInicio: Boolean,
    fechaInicio: String,
    fechaFin: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val calendar = Calendar.getInstance()

    // Parsear fecha actual solo si es válida
    if (currentDate != "día/mes/año") {
        try {
            sdf.parse(currentDate)?.let { date ->
                calendar.time = date
            }
        } catch (_: Exception) {
            // Usar fecha actual si falla
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis
    )

    // Validaciones
    val fechaInicioMillis = fechaInicio.takeIf { it != "día/mes/año" }
        ?.let { sdf.parse(it)?.time } ?: 0L
    val fechaFinMillis = fechaFin.takeIf { it != "día/mes/año" }
        ?.let { sdf.parse(it)?.time } ?: Long.MAX_VALUE

    DatePickerDialog(
        onDismissRequest = { /* Se cierra solo */ },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDate = datePickerState.selectedDateMillis!!
                    val formatted = sdf.format(Date(selectedDate))

                    // VALIDACIÓN: Fecha inicio <= Fecha fin
                    if (isInicio && fechaFinMillis > 0 && selectedDate > fechaFinMillis) {
                        Toast.makeText(context, "Fecha inicio no puede ser posterior a fecha fin", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    if (!isInicio && fechaInicioMillis > 0 && selectedDate < fechaInicioMillis) {
                        Toast.makeText(context, "Fecha fin no puede ser anterior a fecha inicio", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    onDateSelected(formatted)
                }
            ) {
                Text("OK")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
