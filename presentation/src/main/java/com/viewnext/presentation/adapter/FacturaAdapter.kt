package com.viewnext.presentation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viewnext.presentation.databinding.ItemFacturaBinding;
import com.viewnext.domain.model.Factura;

import java.util.List;

/**
 * Adapter para mostrar la lista de facturas en un RecyclerView.
 * Formatea la fecha y el importe, y aplica estilos según el estado de cada factura.
 */
public class FacturaAdapter extends RecyclerView.Adapter<FacturaViewHolder> { // Adapter para mostrar las facturas con un RecyclerView
    private List<Factura> facturas;
    private OnFacturaClickListener listener;

    // Constructor
    public FacturaAdapter(List<Factura> facturas){
        this.facturas = facturas;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
        notifyDataSetChanged();
    }

    // Crear ViewHolder para mostrar un ítem
    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFacturaBinding binding = ItemFacturaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FacturaViewHolder(binding);
    }

    // Vincular datos al ViewHolder
    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        holder.bind(facturas.get(position), listener);
    }

    public interface OnFacturaClickListener {
        void onFacturaClick(Factura factura);
    }

    public void setOnFacturaClickListener(OnFacturaClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() { // Número de elementos de factura
        return facturas != null ? facturas.size() : 0;
    }
}