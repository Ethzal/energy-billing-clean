package com.viewnext.presentation.composables.smartsolar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.viewnext.presentation.R
import com.viewnext.presentation.viewmodel.DetallesViewModel

@Composable
fun DetallesScreen(viewModel: DetallesViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val detalles = uiState.detalles.firstOrNull()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDetalles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        detalles?.let {

            DetailRow(
                label = stringResource(R.string.cau_codigo_autoconsumo),
                value = it.cau
            )

            DetailRowWithButton(
                label = stringResource(R.string.estado_solicitud_alta_autoconsumidor),
                value = it.estadoSolicitud,
                onButtonClick = { showDialog = true }
            )

            DetailRow(
                label = stringResource(R.string.tipo_autoconsumo),
                value = it.tipoAutoconsumo
            )

            DetailRow(
                label = stringResource(R.string.compensacion_de_excedentes),
                value = it.compensacion
            )

            DetailRow(
                label = stringResource(R.string.potencia_de_instalacion),
                value = it.potencia
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            },
            title = {
                Text("Estado solicitud autoconsumo")
            },
            text = {
                Text("El tiempo estimado de activación de tu autoconsumo es de 1 a 2 meses, éste variará en función de tu comunidad autónoma y distribuidora")
            }
        )
    }
    Log.d("showDialog", "showDialog: $showDialog")
}

@Composable
private fun DetailRow(label: String, value: String?) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun DetailRowWithButton(label: String, value: String?, onButtonClick: () -> Unit) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(onClick = onButtonClick) {
                Icon(
                    imageVector = Icons.Default.Info,  // ← Icono nativo Material3
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
