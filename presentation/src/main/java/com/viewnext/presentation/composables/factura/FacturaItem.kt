package com.viewnext.presentation.composables.factura

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.viewnext.domain.model.Factura
import com.viewnext.presentation.util.FacturaFormatter

@Composable
fun FacturaItem(
    factura: Factura,
    onClick: (Factura?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                FacturaFormatter.formatFecha(factura.fecha)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = factura.descEstado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (factura.descEstado) {
                        "Pagada" -> Color.Transparent
                        "Pendiente de pago" -> Color.Red
                        "Anulada" -> Color.Gray
                        else -> Color.Black
                    },
                    fontStyle = when (factura.descEstado) {
                        "Anulada" -> FontStyle.Italic
                        else -> FontStyle.Normal
                    }
                )

            }
            Text(
                text = FacturaFormatter.formatCurrency(factura.importeOrdenacion),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                "Detalle",
                modifier = Modifier.padding(start = 8.dp).clickable { onClick(factura) }
            )
        }
        HorizontalDivider()
    }
}
