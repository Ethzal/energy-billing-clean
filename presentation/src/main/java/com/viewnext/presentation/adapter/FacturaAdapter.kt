package com.viewnext.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.domain.model.Factura
import com.viewnext.presentation.databinding.ItemFacturaBinding

/**
 * Adapter para mostrar la lista de facturas en un RecyclerView.
 * Formatea la fecha y el importe, y aplica estilos según el estado de cada factura.
 */
class FacturaAdapter // Constructor
    (// Adapter para mostrar las facturas con un RecyclerView
    private var facturas: MutableList<Factura> = mutableListOf()
) : RecyclerView.Adapter<FacturaViewHolder>() {
    private var listener: OnFacturaClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setFacturas(facturas: MutableList<Factura>) {
        this.facturas = facturas
        notifyDataSetChanged()
    }

    // Crear ViewHolder para mostrar un ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val binding =
            ItemFacturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacturaViewHolder(binding)
    }

    // Vincular datos al ViewHolder
    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        holder.bind(facturas[position], listener)
    }

    fun interface OnFacturaClickListener {
        fun onFacturaClick(factura: Factura?)
    }

    fun setOnFacturaClickListener(listener: OnFacturaClickListener?) {
        this.listener = listener
    }

    override fun getItemCount() = facturas.size

}