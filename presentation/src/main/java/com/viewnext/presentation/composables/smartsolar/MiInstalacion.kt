package com.viewnext.presentation.composables.smartsolar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.presentation.viewmodel.DetallesViewModel
import com.viewnext.presentation.R

@Composable
fun MiInstalacionScreen(viewModel: DetallesViewModel) {

    LaunchedEffect(Unit) {
        viewModel.loadDetalles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.datos_instalacion_fotovoltaica_en_tiempo_real),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.autoconsumo),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "92%",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Image(
            painter = painterResource(R.drawable.grafico1),
            contentDescription = stringResource(R.string.grafico_1),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}
