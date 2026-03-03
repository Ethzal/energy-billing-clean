package com.viewnext.presentation.composables.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.viewnext.presentation.R
import com.viewnext.presentation.composables.common.BigButtonSection
import com.viewnext.presentation.composables.common.HeaderSection
import com.viewnext.presentation.composables.common.ToggleDebugSection
import com.viewnext.presentation.ui.factura.FacturaActivity
import com.viewnext.presentation.ui.home.MainViewModel
import com.viewnext.presentation.ui.smartsolar.SmartSolarActivity
import com.viewnext.presentation.ui.theme.EnergyAppTheme
import com.viewnext.presentation.ui.theme.White

@Composable
fun MainScreen(viewModel: MainViewModel) {
    EnergyAppTheme {
        MainContent(viewModel = viewModel)
    }
}

@Composable
private fun MainContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val usingRetromock by viewModel.usingRetromock.collectAsState()

    val isDebug = context.applicationInfo.flags and
            android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()

            BigButtonSection(
                title = stringResource(R.string.mi_energia),
                buttonText = stringResource(R.string.facturas),
                icon = Icons.Default.Lightbulb,
                onClick = {
                    context.startActivity(Intent(context, FacturaActivity::class.java).apply {
                        putExtra("USING_RETROMOCK", usingRetromock)
                    })
                }
            )

            BigButtonSection(
                title = stringResource(R.string.servicios_smart),
                buttonText = stringResource(R.string.smart_solar),
                icon = Icons.Default.WbSunny,
                onClick = {
                    context.startActivity(Intent(context, SmartSolarActivity::class.java))
                }
            )
        }

        if (isDebug) {
            ToggleDebugSection(
                checked = usingRetromock,
                onCheckedChange = { viewModel.toggleApi() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}
