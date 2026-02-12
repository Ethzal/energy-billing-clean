package com.viewnext.presentation.ui.smartsolar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.viewnext.presentation.R
import com.viewnext.presentation.databinding.FragmentDetallesBinding
import com.viewnext.presentation.viewmodel.DetallesViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

/**
 * Fragment que representa la sección "Detalles" dentro de Smart Solar.
 * Se encarga de:
 * - Cargar y mostrar detalles del autoconsumo usando DetallesViewModel.
 * - Observar LiveData para actualizar la UI de manera reactiva.
 * - Mostrar un pop-up informativo sobre el estado de la solicitud.
 */
@AndroidEntryPoint
class DetallesFragment : Fragment() {
    private var viewModel: DetallesViewModel? = null
    private var binding: FragmentDetallesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding
        binding = FragmentDetallesBinding.inflate(inflater, container, false)

        // Creación ViewModel de Detalles con UsaCase y Repository
        viewModel = ViewModelProvider(this)[DetallesViewModel::class.java]

        // StateFlow collect
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel?.uiState?.collect { state ->
                    if (state.detalles.isNotEmpty()) {
                        val first = state.detalles[0]
                        binding?.cau?.text = first.cau
                        binding?.estadoSolicitud?.text = first.estadoSolicitud
                        binding?.tipoAutoconsumo?.text = first.tipoAutoconsumo
                        binding?.compensacion?.text = first.compensacion
                        binding?.potencia?.text = first.potencia
                    }
                }
            }
        }

        // Cargar detalles y mostrarlos
        viewModel?.loadDetalles()

        binding?.btPopUp?.setOnClickListener { _: View? ->
            this.showPopup(
                requireView()
            )
        }
        return binding?.root ?: throw IllegalStateException("Binding is null")
    }

    /**
     * Muestra un pop-up personalizado con información sobre el estado de la solicitud de autoconsumo.
     * @param view Vista que dispara el pop-up
     */
    @SuppressLint("SetTextI18n")
    private fun showPopup(view: View) {
        val context = view.context

        // Crear un TextView para el título
        val title = TextView(context)
        title.text = "Estado solicitud autoconsumo"
        title.setPadding(20, 80, 20, 20)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.BLACK)
        title.textSize = 20f
        title.setTypeface(null, Typeface.BOLD)

        // Crear un TextView para el mensaje
        val message = TextView(context)
        message.text = "El tiempo estimado de activación de tu autoconsumo es de 1 a 2 meses, éste variará en función de tu comunidad autónoma y distribuidora"
        message.setPadding(100, 60, 100, 60)
        message.setTextColor(Color.BLACK)
        message.gravity = Gravity.CENTER
        message.textSize = 16f

        // Crear el diálogo
        val builder = AlertDialog.Builder(context)
            .setCustomTitle(title)
            .setView(message)
            .setPositiveButton(
                "Aceptar"
            ) { d: DialogInterface?, _: Int -> d?.dismiss() }
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(Color.WHITE.toDrawable())
        dialog.show()

        // Personalizar el botón después de mostrar el diálogo
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        if (positiveButton != null) {
            positiveButton.background = ContextCompat.getDrawable(
                context,
                R.drawable.button_background
            )
            positiveButton.setTextColor("#ff99cc00".toColorInt())
            positiveButton.setPadding(20, 10, 20, 60)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.btPopUp?.setOnClickListener(null)
        binding = null
    }
}