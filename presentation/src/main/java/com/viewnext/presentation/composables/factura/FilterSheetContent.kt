package com.viewnext.presentation.composables.factura

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viewnext.presentation.viewmodel.FacturaViewModel
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheetContent(
    viewModel: FacturaViewModel,
    maxImporte: Float,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    var estadosSeleccionados by remember { mutableStateOf(listOf<String>()) }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var importeRange by remember { mutableStateOf(0f..maxImporte) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .imePadding()
    ) {
        // Título
        Text(
            "Filtros",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Por un importe", style = MaterialTheme.typography.bodyMedium)
        RangeSlider(
            value = importeRange,
            valueRange = 0f..maxImporte,
            onValueChange = { importeRange = it },
            steps = 10
        )
        Text(
            text = "${importeRange.start.toInt()} - ${importeRange.endInclusive.toInt()}€",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Por estado", style = MaterialTheme.typography.bodyMedium)
        CheckboxEstado("Pagada", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Anulada", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Cuota Fija", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Pendientes de pago", estadosSeleccionados) { estadosSeleccionados = it }
        CheckboxEstado("Plan de pago", estadosSeleccionados) { estadosSeleccionados = it }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    viewModel.clearFiltros()
                    onDismiss()
                }
            ) {
                Text("Borrar filtros")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }
                    viewModel.aplicarTodosLosFiltros(
                        estadosSeleccionados,
                        fechaInicio,
                        fechaFin,
                        listOf(importeRange.start, importeRange.endInclusive)
                    )
                    onDismiss()
                }
            ) {
                Text("Aplicar filtros")
            }
        }
    }
}

@Composable
private fun CheckboxEstado(
    texto: String,
    estados: List<String>,
    onEstadosChange: (List<String>) -> Unit
) {
    val isChecked = estados.contains(texto)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { checked ->
                val nuevosEstados = if (checked) {
                    estados + texto
                } else {
                    estados - texto
                }
                onEstadosChange(nuevosEstados)
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
