package com.viewnext.presentation.ui.smartsolar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.viewnext.presentation.databinding.FragmentMiInstalacionBinding;

/**
 * Fragment que muestra la sección "Mi Instalación" de Smart Solar.
 * Utiliza ViewBinding para acceder a los elementos de layout de manera segura.
 */
public class MiInstalacionFragment extends Fragment {
    private FragmentMiInstalacionBinding binding;

    public MiInstalacionFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Binding
        binding = FragmentMiInstalacionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}