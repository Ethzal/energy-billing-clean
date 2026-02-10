package com.viewnext.presentation.ui.factura;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.viewnext.presentation.databinding.FragmentFiltroBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.viewnext.presentation.viewmodel.FacturaViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Fragment que permite filtrar la lista de facturas.
 * Funcionalidades:
 * - Selección de fechas "Desde" y "Hasta" con validación.
 * - Slider de rango de importes.
 * - Selección de estados de factura mediante checkboxes.
 * - Restauración automática de filtros desde el ViewModel.
 * - Aplicar, cerrar o borrar filtros, comunicándose con FacturaActivity.
 */
public class FiltroFragment extends Fragment {
    private FragmentFiltroBinding binding;
    private FacturaViewModel viewModel;
    float maxImporte;

    public FiltroFragment() { // Constructor vacío
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFiltroBinding.inflate(inflater, container, false);

        // Pasar el ViewModel de Factura
        viewModel = new ViewModelProvider(requireActivity()).get(FacturaViewModel.class);

        // Restaurar filtros si están disponibles
        restoreFiltersViewModel();

        // Botón fecha desde
        binding.btnSelectDate.setOnClickListener(v -> showDatePicker(binding.btnSelectDate));
        binding.btnSelectDateUntil.setOnClickListener(v -> showDatePicker(binding.btnSelectDateUntil));

        // Recuperar el Bundle con maxImporte
        Bundle bundle = getArguments();
        if (bundle != null) {
            maxImporte = bundle.getFloat("MAX_IMPORTE", 0f);

            if (maxImporte > 0) {
                binding.rangeSlider.setValueFrom(0f);
                binding.rangeSlider.setValueTo(maxImporte);
                binding.rangeSlider.setValues(0f, maxImporte);

                binding.tvMinValue.setText("0 €");
                binding.tvMaxValue.setText(maxImporte + " €");
                binding.tvMaxImporte.setText(maxImporte + " €");
            } else {
                binding.tvMinValue.setText("0 €");
                binding.tvMaxValue.setText("0 €");
            }
        }
        binding.rangeSlider.setLabelBehavior(LabelFormatter.LABEL_GONE);
        // Listener para el RangeSlider
        binding.rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> values = slider.getValues();
                float EPSILON = 0.0001f;
                if (values.size() == 2) {
                    float minValue = values.get(0);
                    float maxValue = values.get(1);

                    // Actualizar los TextViews con los valores seleccionados
                    binding.tvMinValue.setText(String.format("%.0f €", minValue));
                    if(maxValue >= maxImporte - EPSILON){
                        binding.tvMaxValue.setText(String.format("%.02f €", maxValue));
                    }else{
                        binding.tvMaxValue.setText(String.format("%.0f €", maxValue));
                    }
                }
            }
        });

        return binding.getRoot();
    }

    /**
     * Mostrar DatePicker y actualizar la fecha en el ViewModel y botón correspondiente.
     * Valida que la fecha "Desde" no sea mayor que "Hasta" y viceversa.
     */
    private void showDatePicker(MaterialButton button) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Cargar la fecha existente en el calendario si está disponible
        if (button == binding.btnSelectDate) {
            String fechaInicio = viewModel.getFechaInicio().getValue();
            if (fechaInicio != null && !fechaInicio.equals("día/mes/año")) {
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(fechaInicio)));
                } catch (Exception e) {
                    Log.e("FiltroFragment", "Error al parsear fecha de inicio", e);
                }
            }
        } else if (button == binding.btnSelectDateUntil) {
            String fechaFin = viewModel.getFechaFin().getValue();
            if (fechaFin != null && !fechaFin.equals("día/mes/año")) {
                try {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(fechaFin)));
                } catch (Exception e) {
                    Log.e("FiltroFragment", "Error al parsear fecha de fin", e);
                }
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    String formattedDate = sdf.format(selectedCalendar.getTime());

                    if (button == binding.btnSelectDate) {
                        // Validar que la fecha de inicio no sea mayor que la fecha de fin
                        String fechaFin = viewModel.getFechaFin().getValue();
                        if (fechaFin != null && !fechaFin.equals("día/mes/año")) { // Solo validar si la fecha "Hasta" ya está seleccionada
                            try {
                                Calendar fechaFinCalendar = Calendar.getInstance();
                                fechaFinCalendar.setTime(Objects.requireNonNull(sdf.parse(fechaFin)));
                                if (formattedDate.compareTo(fechaFin)>0) {
                                    Toast.makeText(getContext(), "La fecha de inicio no puede ser mayor que la fecha de fin", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (Exception e) {
                                Log.e("FiltroFragment", "Error al validar fecha", e);
                            }
                        }
                        binding.btnSelectDate.setText(formattedDate);
                        viewModel.getFechaInicio().setValue(formattedDate);

                    } else if (button == binding.btnSelectDateUntil) {
                        // Validar que la fecha de fin no sea menor que la fecha de inicio
                        String fechaInicio = viewModel.getFechaInicio().getValue();
                        if (fechaInicio != null && !fechaInicio.equals("día/mes/año")) { // Solo validar si la fecha "Desde" ya está seleccionada
                            try {
                                Calendar fechaInicioCalendar = Calendar.getInstance();
                                fechaInicioCalendar.setTime(Objects.requireNonNull(sdf.parse(fechaInicio)));

                                if (selectedCalendar.before(fechaInicioCalendar)) {
                                    Toast.makeText(getContext(), "La fecha de fin no puede ser menor que la fecha de inicio", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Log.e("FiltroFragment", "Error al validar fecha", e);
                            }
                        }
                        binding.btnSelectDateUntil.setText(formattedDate);
                        viewModel.getFechaFin().setValue(formattedDate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    // Restaurar filtros desde el ViewModel a la vista
    private void restoreFiltersViewModel() {
        // Restaurar fecha de inicio
        viewModel.getFechaInicio().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null) {
                binding.btnSelectDate.setText(fecha);
            }
        });

        // Restaurar fecha de fin
        viewModel.getFechaFin().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null) {
                binding.btnSelectDateUntil.setText(fecha);
            }
        });

        // Restaurar valores del slider
        viewModel.getValoresSlider().observe(getViewLifecycleOwner(), valores -> {
            if (valores != null && valores.size() == 2) {
                binding.rangeSlider.setValues(valores.get(0), valores.get(1));
                binding.tvMinValue.setText(String.format(Locale.getDefault(), "%.0f €", valores.get(0)));
                binding.tvMaxValue.setText(String.format(Locale.getDefault(), "%.0f €", valores.get(1)));
            }
        });

        // Restaurar estados seleccionados
        viewModel.getEstados().observe(getViewLifecycleOwner(), estados -> {
            if (estados != null) {
                binding.checkPagadas.setChecked(estados.contains("Pagada"));
                binding.checkPendientesPago.setChecked(estados.contains("Pendiente de pago"));
                binding.checkCuotaFija.setChecked(estados.contains("Cuota Fija"));
                binding.checkPlanPago.setChecked(estados.contains("Plan de pago"));
                binding.checkAnuladas.setChecked(estados.contains("Anulada"));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Botón aplicar filtros
        binding.btnAplicar.setOnClickListener(v -> {
            List<String> estados = getStrings();
            String fechaInicio = binding.btnSelectDate.getText().toString();
            String fechaFin = binding.btnSelectDateUntil.getText().toString();

            List<Float> valoresSlider = binding.rangeSlider.getValues();
            float importeMin = valoresSlider.get(0);
            float importeMax = valoresSlider.get(1);

            // Guardar los filtros en el ViewModel
            viewModel.getEstados().setValue(estados);
            viewModel.getFechaInicio().setValue(fechaInicio);
            viewModel.getFechaFin().setValue(fechaFin);
            viewModel.getValoresSlider().setValue(valoresSlider);

            // Crear un Bundle con los filtros
            Bundle bundle = new Bundle();
            if (!estados.isEmpty()) {
                bundle.putStringArrayList("ESTADOS", (ArrayList<String>) estados);
            }
            bundle.putString("FECHA_INICIO", fechaInicio);
            bundle.putString("FECHA_FIN", fechaFin);
            bundle.putDouble("IMPORTE_MIN", importeMin);
            bundle.putDouble("IMPORTE_MAX", importeMax);

            // Pasar los datos a la actividad
            FacturaActivity activity = (FacturaActivity) getActivity();
            if (activity != null) {
                activity.aplicarFiltros(bundle);
                activity.restoreMainView();
            }
            getParentFragmentManager().popBackStack();
        });

        // Botón cerrar fragmento filtros
        binding.btnCerrar.setOnClickListener(v -> {
            List<String> estados = getStrings();
            String fechaInicio = binding.btnSelectDate.getText().toString();
            String fechaFin = binding.btnSelectDateUntil.getText().toString();
            List<Float> valoresSlider = binding.rangeSlider.getValues();

            // Guardar los filtros en el ViewModel
            viewModel.getEstados().setValue(estados);
            viewModel.getFechaInicio().setValue(fechaInicio);
            viewModel.getFechaFin().setValue(fechaFin);
            viewModel.getValoresSlider().setValue(valoresSlider);
            if (getActivity() != null) {
                FacturaActivity activity = (FacturaActivity) getActivity();
                activity.restoreMainView();
            }
            getParentFragmentManager().popBackStack();
        });

        // Botón borrar filtros
        binding.btnBorrar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fecha = sdf.format(calendar.getTime());
            viewModel.getFechaInicio().setValue(fecha);
            viewModel.getFechaFin().setValue(fecha);

            binding.btnSelectDate.setText("día/mes/año");
            binding.btnSelectDateUntil.setText("día/mes/año");

            binding.rangeSlider.setValues(0f,viewModel.getMaxImporte());

            binding.checkPagadas.setChecked(false);
            binding.checkAnuladas.setChecked(false);
            binding.checkCuotaFija.setChecked(false);
            binding.checkPendientesPago.setChecked(false);
            binding.checkPlanPago.setChecked(false);
        });

        // Recuperar los datos tras rotar
        viewModel.getFechaInicio().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null && !fecha.isEmpty()) {
                binding.btnSelectDate.setText(fecha);
            }
        });
        viewModel.getFechaFin().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null && !fecha.isEmpty()) {
                binding.btnSelectDateUntil.setText(fecha);
            }
        });
        viewModel.getValoresSlider().observe(getViewLifecycleOwner(), valores -> {
            if (valores != null && valores.size() == 2) {
                binding.rangeSlider.setValues(valores.get(0), valores.get(1));
            }
        });
        viewModel.getEstados().observe(getViewLifecycleOwner(), estados -> {
            if (estados != null) {
                binding.checkPagadas.setChecked(estados.contains("Pagada"));
                binding.checkPendientesPago.setChecked(estados.contains("Pendiente de pago"));
                binding.checkCuotaFija.setChecked(estados.contains("Cuota Fija"));
                binding.checkPlanPago.setChecked(estados.contains("Plan de pago"));
                binding.checkAnuladas.setChecked(estados.contains("Anulada"));
            }
        });
    }

    // Obtiene los estados seleccionados como lista de strings
    @NonNull
    private List<String> getStrings() { // Estados
        List<String> estados = new ArrayList<>();

        if (binding.checkPagadas.isChecked()) {
            estados.add("Pagada");
        }
        if (binding.checkPendientesPago.isChecked()) {
            estados.add("Pendiente de pago");
        }
        if (binding.checkCuotaFija.isChecked()) {
            estados.add("Cuota Fija");
        }
        if (binding.checkPlanPago.isChecked()) {
            estados.add("Plan de pago");
        }
        if (binding.checkAnuladas.isChecked()) {
            estados.add("Anulada");
        }
        return estados;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
