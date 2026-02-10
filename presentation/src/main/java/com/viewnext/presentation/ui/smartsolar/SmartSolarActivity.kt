package com.viewnext.presentation.ui.smartsolar;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.viewnext.presentation.R;
import com.viewnext.presentation.databinding.ActivitySmartSolarBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.viewnext.presentation.adapter.SmartSolarPagerAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity principal de Smart Solar.
 * Muestra una interfaz con un ViewPager2 y un TabLayout para navegar
 * entre las secciones "Mi Instalación", "Energía" y "Detalles".
 * También gestiona el edge-to-edge layout y el botón de retroceso.
 */
@AndroidEntryPoint
public class SmartSolarActivity extends AppCompatActivity {

    private ActivitySmartSolarBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private SmartSolarPagerAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        binding = ActivitySmartSolarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón atrás
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Configuración del título
        binding.toolbarTitle.setText("Smart Solar");

        // Creación de lista de fragmentos
        List<Fragment> fragments = List.of(
                new MiInstalacionFragment(),
                new EnergiaFragment(),
                new DetallesFragment()
        );

        // Configurar ViewPager2 con el adaptador
        SmartSolarPagerAdapter adapter =
                new SmartSolarPagerAdapter(this, fragments);

        binding.viewPager.setAdapter(adapter);

        // Vincular TabLayout con ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Mi Instalación");
                    break;
                case 1:
                    tab.setText("Energía");
                    break;
                case 2:
                    tab.setText("Detalles");
                    break;
            }
        }).attach();

        // Aplicar el fondo personalizado al TabLayout
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.view.setBackgroundResource(R.drawable.tab_selector);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setBackgroundResource(android.R.color.white);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}