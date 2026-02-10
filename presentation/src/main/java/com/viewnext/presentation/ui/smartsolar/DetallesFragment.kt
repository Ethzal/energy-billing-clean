package com.viewnext.presentation.ui.smartsolar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.viewnext.presentation.R;
import com.viewnext.presentation.databinding.FragmentDetallesBinding;
import com.viewnext.presentation.viewmodel.DetallesViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment que representa la sección "Detalles" dentro de Smart Solar.
 * Se encarga de:
 * - Cargar y mostrar detalles del autoconsumo usando DetallesViewModel.
 * - Observar LiveData para actualizar la UI de manera reactiva.
 * - Mostrar un pop-up informativo sobre el estado de la solicitud.
 */
@AndroidEntryPoint
public class DetallesFragment extends Fragment {
    private DetallesViewModel viewModel;
    private FragmentDetallesBinding binding;

    public DetallesFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Binding
        binding = FragmentDetallesBinding.inflate(inflater, container, false);

        // Creación ViewModel de Detalles con UsaCase y Repository
        viewModel = new ViewModelProvider(this).get(DetallesViewModel.class);

        // Cargar detalles y mostrarlos
        viewModel.loadDetalles();
        viewModel.getDetalles().observe(getViewLifecycleOwner(), detalles -> {
            if (detalles != null && !detalles.isEmpty()) {
                binding.cau.setText(detalles.get(0).getCau());
                binding.estadoSolicitud.setText(detalles.get(0).getEstadoSolicitud());
                binding.tipoAutoconsumo.setText(detalles.get(0).getTipoAutoconsumo());
                binding.compensacion.setText(detalles.get(0).getCompensacion());
                binding.potencia.setText(detalles.get(0).getPotencia());
            }
        });

        binding.btPopUp.setOnClickListener(this::showPopup);
        return binding.getRoot();
    }

    /**
     * Muestra un pop-up personalizado con información sobre el estado de la solicitud de autoconsumo.
     * @param view Vista que dispara el pop-up
     */
    @SuppressLint("SetTextI18n")
    private void showPopup(View view) {
        Context context = view.getContext();

        // Crear un TextView para el título
        TextView title = new TextView(context);
        title.setText("Estado solicitud autoconsumo");
        title.setPadding(20, 80, 20, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        title.setTypeface(null, Typeface.BOLD);

        // Crear un TextView para el mensaje
        TextView message = new TextView(context);
        message.setText("El tiempo estimado de activación de tu autoconsumo es de 1 a 2 meses, éste variará en función de tu comunidad autónoma y distribuidora");
        message.setPadding(100, 60, 100, 60);
        message.setTextColor(Color.BLACK);
        message.setGravity(Gravity.CENTER);
        message.setTextSize(16);

        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCustomTitle(title)
                .setView(message)
                .setPositiveButton("Aceptar", (d, which) -> d.dismiss());
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

        // Personalizar el botón después de mostrar el diálogo
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setBackground(ContextCompat.getDrawable(context, R.drawable.button_background));
            positiveButton.setTextColor(Color.parseColor("#ff99cc00"));
            positiveButton.setPadding(20, 10, 20, 60);
        }
    }
}