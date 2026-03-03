package com.viewnext.presentation.composables.smartsolar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewnext.presentation.viewmodel.DetallesViewModel
import kotlinx.coroutines.launch
import com.viewnext.presentation.ui.theme.HoloGreenLight

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SmartSolarScreen(
    detallesViewModel: DetallesViewModel,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

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
                colors = TopAppBarDefaults.topAppBarColors(
                    navigationIconContentColor = HoloGreenLight
                )
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Text(
                text = "Smart Solar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
            )
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text("Mi Instalación") }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text("Energía") }
                )
                Tab(
                    selected = pagerState.currentPage == 2,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } },
                    text = { Text("Detalles") }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                beyondViewportPageCount = 1,
                pageSpacing = 8.dp
            ) { page ->
                when (page) {
                    0 -> MiInstalacionScreen(detallesViewModel)
                    1 -> EnergiaScreen()
                    2 -> DetallesScreen(detallesViewModel)
                }
            }

        }
    }
}