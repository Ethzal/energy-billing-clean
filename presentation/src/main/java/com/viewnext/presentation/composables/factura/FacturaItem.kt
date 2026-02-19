package com.viewnext.presentation.composables.factura

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.domain.model.Factura
import com.viewnext.presentation.util.FacturaFormatter

@Composable
fun FacturaItem(
    factura: Factura,
    onClick: (Factura?) -> Unit
) {
    val hasVisibleEstado = factura.descEstado != "Pagada"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),   // altura fija (ajusta a lo que quieras)
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),                // ocupa toda la altura del Row
                verticalArrangement = if (hasVisibleEstado) {
                    Arrangement.Center              // o Top si prefieres
                } else {
                    Arrangement.Center              // centra la fecha cuando no hay estado
                }
            ) {
                FacturaFormatter.formatFecha(factura.fecha)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }

                if (hasVisibleEstado) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = factura.descEstado,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = when (factura.descEstado) {
                            "Pendiente de pago" -> Color.Red
                            "Anulada" -> Color.Gray
                            else -> Color.Black
                        }
                    )
                }
            }

            Text(
                text = FacturaFormatter.formatCurrency(factura.importeOrdenacion),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                "Detalle",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onClick(factura) }
            )
        }
    }
    HorizontalDivider()
}


@Preview(showBackground = true)
@Composable
fun FacturaItemPreview() {
    FacturaItem(
        factura = Factura(
            fecha = "23 may 2025",
            descEstado = "Pagada",
            importeOrdenacion = 120.50
        ),
        onClick = {}
    )
}
