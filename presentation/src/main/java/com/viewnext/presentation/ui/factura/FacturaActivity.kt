package com.viewnext.presentation.ui.factura;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;

import com.viewnext.presentation.R;
import com.viewnext.presentation.databinding.ActivityFacturaBinding;
import com.viewnext.presentation.viewmodel.FacturaViewModel;
import com.viewnext.presentation.adapter.FacturaAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity principal para la pantalla de facturas.
 * Funcionalidades:
 * - Mostrar lista de facturas en RecyclerView.
 * - Aplicar filtros mediante FiltroFragment.
 * - Skeleton (Shimmer) mientras se cargan datos.
 * - Manejo de errores y mensajes de estado.
 */
@AndroidEntryPoint
public class FacturaActivity extends AppCompatActivity {
    private FacturaAdapter adapter;
    ActivityFacturaBinding binding;
    FacturaViewModel facturaViewModel;
    FacturaNavigator facturaNavigator;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFacturaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigator
        facturaNavigator = new FacturaNavigator(getSupportFragmentManager());

        // Hacer visible el fragmento tras rotar
        FiltroFragment filtroFragment = (FiltroFragment) getSupportFragmentManager().findFragmentByTag("FILTRO_FRAGMENT");
        if (filtroFragment != null) {
            binding.fragmentContainer.setVisibility(View.VISIBLE);
        }

        // Creacion ViewModel de Factura
        facturaViewModel = new ViewModelProvider(this).get(FacturaViewModel.class);

        // Adapter
        adapter = new FacturaAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Pop-Up
        adapter.setOnFacturaClickListener(factura -> new AlertDialog.Builder(FacturaActivity.this)
                .setTitle(R.string.info)
                .setMessage(R.string.funcionalidad_no_disponible)
                .setPositiveButton(R.string.cerrar, (dialog, which) -> dialog.dismiss())
                .show());

        // Mostrar skeleton
        showShimmer();

        // Primera vez
        facturaViewModel.init(savedInstanceState == null, getIntent());

        facturaViewModel.getLoading().observe(this, isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                showShimmer();
            } else {
                hideShimmer();
            }
        });

        // Toolbar
        setSupportActionBar(binding.toolbar);

        // Botón atrás
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Configuración del título
        binding.toolbarTitle.setText("Facturas");

        // Actualizar RecyclerView con las nuevas facturas
        facturaViewModel.getFacturas().observe(this, facturas -> {
            if (facturas == null) return;

            if (facturas.isEmpty() && facturaViewModel.hayFiltrosActivos()) {
                Toast.makeText(this, "No se encontraron facturas", Toast.LENGTH_SHORT).show();
            }

            adapter.setFacturas(facturas);
        });

        // Mostrar mensaje de error
        facturaViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(FacturaActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Mostrar/ocultar skeleton
    private void showShimmer() {
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.shimmerLayout.startShimmer();
        binding.recyclerView.setVisibility(View.GONE);
    }

    private void hideShimmer() {
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Crear menú para el botón filtros
        getMenuInflater().inflate(R.menu.menu_factura, menu);
        return true;
    }

    /*******************
     *   NAVIGATOR
     *******************/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filters) {
            float maxImporte = facturaViewModel.getMaxImporte();
            facturaNavigator.openFilter(maxImporte);
            binding.fragmentContainer.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { // Sobreescribir el metodo deprecated por incompatibilidades
        if (!facturaNavigator.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    public void restoreMainView() { // Restaurar visibilidad de la actividad Factura
        binding.toolbar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.fragmentContainer.setVisibility(View.GONE);
    }

    public void aplicarFiltros(Bundle bundle) {
        // Recuperar los filtros desde el Bundle
        List<String> estadosSeleccionados = bundle.getStringArrayList("ESTADOS");
        String fechaInicio = bundle.getString("FECHA_INICIO");
        String fechaFin = bundle.getString("FECHA_FIN");
        Double importeMin = bundle.getDouble("IMPORTE_MIN");
        Double importeMax = bundle.getDouble("IMPORTE_MAX");

        // Llamar al ViewModel para aplicar los filtros
        facturaViewModel.aplicarFiltros(estadosSeleccionados, fechaInicio, fechaFin, importeMin, importeMax);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}