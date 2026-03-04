package com.viewnext.presentation.composables.factura

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.presentation.R
import com.viewnext.presentation.ui.theme.HoloGreenLight
import com.viewnext.presentation.viewmodel.FacturaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenFull(
    viewModel: FacturaViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val maxImporte = viewModel.getMaxImporte()

    // Estado derivado desde ViewModel (FUENTE ÚNICA DE VERDAD)
    val fechaInicio = uiState.fechaInicio ?: "día/mes/año"
    val fechaFin = uiState.fechaFin ?: "día/mes/año"
    val importeRange = uiState.valoresSlider?.let { it[0]..it[1] } ?: (0f..maxImporte)
    val estadosSeleccionados = uiState.estados ?: emptyList()

    // Estado puramente visual
    var showFechaInicioPicker by remember { mutableStateOf(false) }
    var showFechaFinPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("")
                },
                actions = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = "Cerrar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Filtrar Facturas", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // COLUMNA 1: DESDE
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Desde", style = MaterialTheme.typography.bodyMedium)
                    OutlinedButton(
                        onClick = { showFechaInicioPicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(fechaInicio)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // COLUMNA 2: HASTA
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Hasta", style = MaterialTheme.typography.bodyMedium)
                    OutlinedButton(
                        onClick = { showFechaFinPicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(fechaFin)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Range slider
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
                    viewModel.setValoresSlider(
                        listOf(newRange.start, newRange.endInclusive)
                    )
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

            // Checkboxes estados
            Text("Por estado", style = MaterialTheme.typography.bodyMedium)
            CheckboxEstado("Pagada", estadosSeleccionados) { nuevosEstados ->
                viewModel.setEstados(nuevosEstados.ifEmpty { null })
            }
            CheckboxEstado("Anulada", estadosSeleccionados) { nuevosEstados ->
                viewModel.setEstados(nuevosEstados.ifEmpty { null })
            }
            CheckboxEstado("Cuota Fija", estadosSeleccionados) { nuevosEstados ->
                viewModel.setEstados(nuevosEstados.ifEmpty { null })
            }
            CheckboxEstado("Pendiente de pago", estadosSeleccionados) { nuevosEstados ->
                viewModel.setEstados(nuevosEstados.ifEmpty { null })
            }
            CheckboxEstado("Plan de pago", estadosSeleccionados) { nuevosEstados ->
                viewModel.setEstados(nuevosEstados.ifEmpty { null })
            }
            Spacer(modifier = Modifier.height(32.dp))


            // Botones
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
                    onBack()
                }) {
                    Text("Aplicar filtros")
                }
            }
        }
    }
    // En FilterScreenFull, CAMBIAR LAS LLAMADAS:
    if (showFechaInicioPicker) {
        DatePickerDialogComposable(
            currentDate = fechaInicio,
            isInicio = true,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            onDateSelected = { date ->
                viewModel.setFechaInicio(date)
                showFechaInicioPicker = false
            }
        )
    }
    Log.d("showFechaInicioPicker", "showFechaInicioPicker: $showFechaInicioPicker")

    if (showFechaFinPicker) {
        DatePickerDialogComposable(
            currentDate = fechaFin,
            isInicio = false,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            onDateSelected = { date ->
                viewModel.setFechaFin(date)
                showFechaFinPicker = false
            }
        )
    }
    Log.d("showFechaFinPicker", "showFechaFinPicker: $showFechaFinPicker")
}
