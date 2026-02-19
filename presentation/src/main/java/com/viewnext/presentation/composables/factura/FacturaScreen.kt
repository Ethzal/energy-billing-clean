package com.viewnext.presentation.composables.factura

import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.presentation.ui.theme.HoloGreenLight
import com.viewnext.presentation.viewmodel.FacturaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaScreen(
    viewModel: FacturaViewModel,
    onBack: () -> Unit,
    usingRetromock: Boolean,
    intent: Intent
) {
    val uiState by viewModel.uiState.collectAsState()
    val mensajes by viewModel.mensajes.collectAsState("")

    var showFilterSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler {
        if (showFilterSheet) {
            showFilterSheet = false
        } else {
            onBack()
        }
    }

    LaunchedEffect(usingRetromock) {
        viewModel.init(true, intent)
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
                    // Deja el título vacío o pon algo neutro
                    Text("")
                },
                navigationIcon = {
                    // Row clicable para icono + texto
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { onBack() }, // acción al hacer click
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Default.FilterList, "Filtros")
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

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState
        ) {
            FilterSheetContent(
                viewModel = viewModel,
                maxImporte = viewModel.getMaxImporte(),
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    showFilterSheet = false
                },
                sheetState = sheetState
            )
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
}
