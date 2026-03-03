package com.viewnext.presentation.composables.factura

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxEstado(
    texto: String,
    estados: List<String>,
    onEstadosChange: (List<String>) -> Unit
) {
    val isChecked = estados.contains(texto)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { checked ->
                val nuevosEstados = if (checked) estados + texto else estados - texto
                onEstadosChange(nuevosEstados)
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(texto, style = MaterialTheme.typography.bodyMedium)
    }
}
