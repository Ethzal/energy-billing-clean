package com.viewnext.presentation.ui.home;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.viewnext.presentation.databinding.ActivityMainBinding;
import com.viewnext.presentation.ui.factura.FacturaActivity;
import com.viewnext.presentation.ui.smartsolar.SmartSolarActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity principal de la aplicación.
 * Funciones principales:
 * - Configuración edge-to-edge para que la UI ocupe toda la pantalla.
 * - Setup de visibilidad de controles debug.
 * - Navegación a FacturaActivity y SmartSolarActivity.
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    MainViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this); // La interfaz se extiende por toda la pantalla
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupWindowInsets();
        setupToggleVisibility();
        setupClickListeners();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupToggleVisibility() {
        boolean isDebug = (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        if (isDebug) {
            binding.btToggleApi.setVisibility(View.VISIBLE);
        } else {
            binding.btToggleApi.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        binding.btToggleApi.setOnClickListener(v -> viewModel.toggleApi());

        binding.btFacturas.setOnClickListener(v -> {
            Intent intent = new Intent(this, FacturaActivity.class);
            intent.putExtra("USING_RETROMOCK", viewModel.getUsingRetromock().getValue());
            startActivity(intent);
        });

        binding.btSmart.setOnClickListener(v ->
                startActivity(new Intent(this, SmartSolarActivity.class))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
