package com.viewnext.presentation.composables.factura

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.presentation.R
import com.viewnext.presentation.composables.common.FilterButton
import com.viewnext.presentation.ui.theme.HoloGreenLight
import com.viewnext.presentation.viewmodel.FacturaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaScreen(
    viewModel: FacturaViewModel,
    onBack: () -> Unit,
    usingRetromock: Boolean,
    onOpenFilters: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val mensajes by viewModel.mensajes.collectAsState("")

    var showDialog by remember { mutableStateOf(false) }

    var showFilters by remember { mutableStateOf(false) }

    val context = LocalContext.current

    BackHandler {
        if (showFilters) {
            showFilters = false
        } else {
            onBack()
        }
    }

    // Toast para errores
    LaunchedEffect(mensajes) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("")
                },
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { onBack() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Atrás",
                            tint = HoloGreenLight
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Atrás",
                            fontSize = 20.sp,
                            color = HoloGreenLight
                        )
                    }
                },
                actions = {
                    FilterButton(
                        onClick = { showFilters = true }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.filtericon_3x), "Filtros")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    navigationIconContentColor = HoloGreenLight
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Título grande
            Text(
                text = "Facturas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista o shimmer
            if (uiState.isLoading) {
                ShimmerFacturaList()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.facturas) { factura ->
                        FacturaItem(
                            factura = factura,
                            onClick = { showDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Info") },
            text = { Text("Funcionalidad no disponible") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
    Log.d("showDialog", "showDialog: $showDialog")
    if (showFilters) {
        FilterScreenFull(
            viewModel = viewModel,
            onBack = { showFilters = false }
        )
    }
}
