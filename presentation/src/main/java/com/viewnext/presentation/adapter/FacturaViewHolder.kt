package com.viewnext.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.domain.model.Factura
import com.viewnext.presentation.adapter.FacturaAdapter.OnFacturaClickListener
import com.viewnext.presentation.databinding.ItemFacturaBinding
import com.viewnext.presentation.util.FacturaFormatter
import com.viewnext.presentation.util.FacturaStyler

/**
 * ViewHolder responsable de vincular los datos de una factura.
 */
class FacturaViewHolder(private val binding: ItemFacturaBinding) : RecyclerView.ViewHolder(
    binding.getRoot()
) {
    fun bind(factura: Factura, listener: OnFacturaClickListener?) {
        binding.descEstado.text = factura.descEstado
        binding.descEstado.visibility = View.VISIBLE

        FacturaStyler.style(binding.descEstado, factura.descEstado)
        binding.importe.text = FacturaFormatter.formatCurrency(factura.importeOrdenacion)
        binding.fecha.text = FacturaFormatter.formatFecha(factura.fecha)

        itemView.setOnClickListener { _: View? ->
            listener?.onFacturaClick(factura)
        }
    }
}
