package com.viewnext.presentation.composables.factura

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.viewnext.presentation.ui.theme.HoloGreenLight
import com.viewnext.presentation.viewmodel.FacturaViewModel
import java.util.*
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheetContent(
    viewModel: FacturaViewModel,
    maxImporte: Float,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    val context = LocalContext.current

    // Local state synced with ViewModel
    var estadosSeleccionados by remember { mutableStateOf(listOf<String>()) }
    var fechaInicio by remember { mutableStateOf("día/mes/año") }
    var fechaFin by remember { mutableStateOf("día/mes/año") }
    var importeRange by remember { mutableStateOf(0f..maxImporte) }

    // Control showing dialogs
    var showFechaInicioPicker by remember { mutableStateOf(false) }
    var showFechaFinPicker by remember { mutableStateOf(false) }

    // Restore state from ViewModel
    LaunchedEffect(viewModel.uiState.collectAsState().value) {
        val state = viewModel.uiState.value
        state.estados?.let { estadosSeleccionados = it }
        state.fechaInicio?.let { fechaInicio = it }
        state.fechaFin?.let { fechaFin = it }
        state.valoresSlider?.let {
            if (it.size == 2) importeRange = it[0]..it[1]
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .imePadding()
    ) {
        Text("Filtrar Facturas", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Spacer(modifier = Modifier.height(24.dp))

        // 📅 Fecha inicio
        Text("Desde", style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(
            onClick = { showFechaInicioPicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(fechaInicio)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 📅 Fecha fin
        Text("Hasta", style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(
            onClick = { showFechaFinPicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(fechaFin)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 💶 Range slider
        Text("Por un importe", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(), // ocupa todo el ancho
            contentAlignment = Alignment.Center  // centra todo el contenido dentro del Box
        ) {
            Text(
                text = "${importeRange.start.toInt()} - ${importeRange.endInclusive.toInt()}€",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                color = HoloGreenLight
            )
        }

        RangeSlider(
            value = importeRange,
            onValueChange = { newRange ->
                importeRange = newRange
            },
            valueRange = 0f..maxImporte,
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = HoloGreenLight,
                activeTrackColor = HoloGreenLight,
                inactiveTrackColor = Color(0xFFDDDDDD)
            ),
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(HoloGreenLight, CircleShape),
                )
            },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    modifier = Modifier.height(8.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = HoloGreenLight,
                        inactiveTrackColor = Color(0xFFDDDDDD)
                    )
                )
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(HoloGreenLight, CircleShape)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Checkboxes estados
        Text("Por estado", style = MaterialTheme.typography.bodyMedium)
        CheckboxEstado("Pagada", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Anulada", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Cuota Fija", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Pendiente de pago", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Plan de pago", estadosSeleccionados) { estadosSeleccionados = it }
        Spacer(modifier = Modifier.height(32.dp))

        // 🔘 Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                viewModel.clearFiltros()
            }) {
                Text("Borrar filtros")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                viewModel.aplicarTodosLosFiltros(
                    estadosSeleccionados,
                    fechaInicio.takeIf { it != "día/mes/año" },
                    fechaFin.takeIf { it != "día/mes/año" },
                    listOf(importeRange.start, importeRange.endInclusive)
                )
                onDismiss()
            }) {
                Text("Aplicar filtros")
            }
        }
    }

    // --- Compose-friendly DatePickers ---
    if (showFechaInicioPicker) {
        DatePickerDialogComposable(
            currentDate = fechaInicio,
            onDateSelected = { date ->
                fechaInicio = date
                viewModel.setFechaInicio(date)
                showFechaInicioPicker = false
            }
        )
    }

    if (showFechaFinPicker) {
        DatePickerDialogComposable(
            currentDate = fechaFin,
            onDateSelected = { date ->
                fechaFin = date
                viewModel.setFechaFin(date)
                showFechaFinPicker = false
            }
        )
    }
}

// Helper composable to show an Android DatePickerDialog
@Composable
fun DatePickerDialogComposable(
    currentDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Try parsing currentDate
    currentDate.split("/").let {
        if (it.size == 3) {
            calendar.set(it[2].toInt(), it[1].toInt() - 1, it[0].toInt())
        }
    }

    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val formatted = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
            onDateSelected(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
