package com.viewnext.presentation.ui.factura

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.LabelFormatter
import com.viewnext.presentation.databinding.FragmentFiltroBinding
import com.viewnext.presentation.viewmodel.FacturaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment que permite filtrar la lista de facturas.
 * Funcionalidades:
 * - Selección de fechas "Desde" y "Hasta" con validación.
 * - Slider de rango de importes.
 * - Selección de estados de factura mediante checkboxes.
 * - Restauración automática de filtros desde el ViewModel.
 * - Aplicar, cerrar o borrar filtros, comunicándose con FacturaActivity.
 */
class FiltroFragment : Fragment() {

    private var _binding: FragmentFiltroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FacturaViewModel by activityViewModels()

    private var maxImporte: Float = 0f

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltroBinding.inflate(inflater, container, false)

        // Restaurar filtros si están disponibles
        restoreFiltersViewModel()

        // Botón fecha desde
        binding.btnSelectDate.setOnClickListener { showDatePicker(binding.btnSelectDate) }
        binding.btnSelectDateUntil.setOnClickListener { showDatePicker(binding.btnSelectDateUntil) }

        // Recuperar el Bundle con maxImporte
        arguments?.let { bundle ->
            maxImporte = bundle.getFloat("MAX_IMPORTE", 0f)

            if (maxImporte > 0) {
                binding.rangeSlider.valueFrom = 0f
                binding.rangeSlider.valueTo = maxImporte
                binding.rangeSlider.values = listOf(0f, maxImporte)

                binding.tvMinValue.text = "0 €"
                binding.tvMaxValue.text = "$maxImporte €"
                binding.tvMaxImporte.text = "$maxImporte €"
            } else {
                binding.tvMinValue.text = "0 €"
                binding.tvMaxValue.text = "0 €"
            }
        }

        binding.rangeSlider.labelBehavior = LabelFormatter.LABEL_GONE

        // Listener para el RangeSlider
        binding.rangeSlider.addOnChangeListener { slider, _, _ ->
            slider.values.getOrNull(1)?.let { maxValue ->
                binding.tvMinValue.text = "%.0f €".format(slider.values[0])
                binding.tvMaxValue.text = if (maxValue >= maxImporte - 0.0001f)
                    "%.02f €".format(maxValue)
                else "%.0f €".format(maxValue)
            }
        }

        return binding.root
    }

    /**
     * Mostrar DatePicker y actualizar la fecha en el ViewModel y botón correspondiente.
     * Valida que la fecha "Desde" no sea mayor que "Hasta" y viceversa.
     */
    private fun showDatePicker(button: MaterialButton) {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Cargar la fecha existente en el calendario si está disponible
        val fechaInicio = viewModel.uiState.value.fechaInicio
        val fechaFin = viewModel.uiState.value.fechaFin

        if (button == binding.btnSelectDate) {
            fechaInicio?.takeIf { it != "día/mes/año" }?.let {
                sdf.parse(it)?.let { date -> calendar.time = date }
            }
        } else if (button == binding.btnSelectDateUntil) {
            fechaFin?.takeIf { it != "día/mes/año" }?.let {
                sdf.parse(it)?.let { date -> calendar.time = date }
            }
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val formattedDate = sdf.format(selectedCalendar.time)

                val fechaInicio = viewModel.uiState.value.fechaInicio
                val fechaFin = viewModel.uiState.value.fechaFin

                if (button == binding.btnSelectDate) {
                    // Validar que la fecha de inicio no sea mayor que la fecha de fin
                    fechaFin?.takeIf { it != "día/mes/año" }?.let {
                        sdf.parse(it)?.let { finDate ->
                            val finCalendar = Calendar.getInstance().apply { time = finDate }
                            if (selectedCalendar.after(finCalendar)) {
                                Toast.makeText(
                                    context,
                                    "La fecha de inicio no puede ser mayor que la fecha de fin",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@DatePickerDialog
                            }
                        }
                    }
                    binding.btnSelectDate.text = formattedDate
                    viewModel.setFechaInicio(formattedDate) // <-- usar función del ViewModel

                } else if (button == binding.btnSelectDateUntil) {
                    // Validar que la fecha de fin no sea menor que la fecha de inicio
                    fechaInicio?.takeIf { it != "día/mes/año" }?.let {
                        sdf.parse(it)?.let { inicioDate ->
                            val inicioCalendar = Calendar.getInstance().apply { time = inicioDate }
                            if (selectedCalendar.before(inicioCalendar)) {
                                Toast.makeText(
                                    context,
                                    "La fecha de fin no puede ser menor que la fecha de inicio",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@DatePickerDialog
                            }
                        }
                    }
                    binding.btnSelectDateUntil.text = formattedDate
                    viewModel.setFechaFin(formattedDate) // <-- usar función del ViewModel
                }
            },
            year, month, dayOfMonth
        ).show()
    }

    // Restaurar filtros desde el ViewModel a la vista
    private fun restoreFiltersViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Restaurar fecha inicio
                    state.fechaInicio?.let { binding.btnSelectDate.text = it }

                    // Restaurar fecha fin
                    state.fechaFin?.let { binding.btnSelectDateUntil.text = it }

                    // Restaurar valores del slider
                    state.valoresSlider?.let { valores ->
                        if (valores.size == 2) {
                            binding.rangeSlider.values = valores
                            binding.tvMinValue.text = String.format(Locale.getDefault(), "%.0f €", valores[0])
                            binding.tvMaxValue.text = String.format(Locale.getDefault(), "%.0f €", valores[1])
                        }
                    }

                    // Restaurar estados seleccionados
                    state.estados?.let { estados ->
                        binding.checkPagadas.isChecked = estados.contains("Pagada")
                        binding.checkPendientesPago.isChecked = estados.contains("Pendiente de pago")
                        binding.checkCuotaFija.isChecked = estados.contains("Cuota Fija")
                        binding.checkPlanPago.isChecked = estados.contains("Plan de pago")
                        binding.checkAnuladas.isChecked = estados.contains("Anulada")
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón aplicar filtros
        binding.btnAplicar.setOnClickListener {
            val estados = getStrings()
            val fechaInicio = binding.btnSelectDate.text.toString()
            val fechaFin = binding.btnSelectDateUntil.text.toString()
            val valoresSlider = binding.rangeSlider.values
            val importeMin = valoresSlider.getOrNull(0) ?: 0f
            val importeMax = valoresSlider.getOrNull(1) ?: maxImporte

            // Crear un Bundle con los filtros
            val bundle = Bundle().apply {
                if (estados.isNotEmpty()) {
                    putStringArrayList("ESTADOS", ArrayList(estados))
                }
                putString("FECHA_INICIO", fechaInicio)
                putString("FECHA_FIN", fechaFin)
                putDouble("IMPORTE_MIN", importeMin.toDouble())
                putDouble("IMPORTE_MAX", importeMax.toDouble())
            }

            // Pasar los datos a la actividad
            (activity as? FacturaActivity)?.let { activity ->
                activity.aplicarFiltros(bundle)
                activity.restoreMainView()
            }
            parentFragmentManager.popBackStack()
        }

        // Botón cerrar fragmento filtros
        binding.btnCerrar.setOnClickListener {
            val estados = getStrings()
            val fechaInicio = binding.btnSelectDate.text.toString()
            val fechaFin = binding.btnSelectDateUntil.text.toString()
            val valoresSlider = binding.rangeSlider.values

            // Guardar los filtros en el ViewModel
            viewModel.aplicarTodosLosFiltros(estados, fechaInicio, fechaFin, valoresSlider)

            activity?.let {
                (it as? FacturaActivity)?.restoreMainView()
            }
            parentFragmentManager.popBackStack()
        }

        // Botón borrar filtros
        binding.btnBorrar.setOnClickListener {
            viewModel.clearFiltros()

            binding.btnSelectDate.text = "día/mes/año"
            binding.btnSelectDateUntil.text = "día/mes/año"
            binding.rangeSlider.values = listOf(0f, maxImporte)

            binding.checkPagadas.isChecked = false
            binding.checkAnuladas.isChecked = false
            binding.checkCuotaFija.isChecked = false
            binding.checkPendientesPago.isChecked = false
            binding.checkPlanPago.isChecked = false
        }

    }

    // Obtiene los estados seleccionados como lista de strings
    private fun getStrings(): List<String> {
        return mutableListOf<String>().apply {
            if (binding.checkPagadas.isChecked) add("Pagada")
            if (binding.checkPendientesPago.isChecked) add("Pendiente de pago")
            if (binding.checkCuotaFija.isChecked) add("Cuota Fija")
            if (binding.checkPlanPago.isChecked) add("Plan de pago")
            if (binding.checkAnuladas.isChecked) add("Anulada")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
