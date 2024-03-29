package com.neu360.x_lab.laboratorio.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.DTO.DetalleOrdenDTO;

import java.util.List;

public class DetalleOrdenAdapter extends RecyclerView.Adapter<DetalleOrdenAdapter.HolderDetalleOrdenes> implements View.OnClickListener{

    private final List<DetalleOrdenDTO> lsDetalleOrden;
    private DetalleOrdenDTO detalleOrdenDTO;
    private static Context context;

    private View.OnClickListener listener;

    public DetalleOrdenAdapter(List<DetalleOrdenDTO> lsDetalleOrden, Context context){
        this.lsDetalleOrden = lsDetalleOrden;
        this.context = context;
    }

    @Override
    public DetalleOrdenAdapter.HolderDetalleOrdenes onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_orden,parent,false);
        v.setOnClickListener(this);
        return new HolderDetalleOrdenes(v);
    }

    public static class HolderDetalleOrdenes extends RecyclerView.ViewHolder{

        TextView txtNombrePrestacion;
        CheckBox chkSeleccionado;


        public HolderDetalleOrdenes(@NonNull View itemView) {
            super(itemView);
            txtNombrePrestacion = itemView.findViewById(R.id.txtNombrePrestacion);
            chkSeleccionado = itemView.findViewById(R.id.chkSeleccionado);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull DetalleOrdenAdapter.HolderDetalleOrdenes holder, int position) {

        holder.txtNombrePrestacion.setText(String.valueOf(lsDetalleOrden.get(position).getCodigoPrestacion())+" - "+lsDetalleOrden.get(position).getNombrePrestacion());

        holder.chkSeleccionado.setOnClickListener(v -> {
            if(holder.chkSeleccionado.isChecked()){

                lsDetalleOrden.get(position).setEsSeleccionado("S");
            }else{

                lsDetalleOrden.get(position).setEsSeleccionado("N");
            }
        });

        if(lsDetalleOrden.get(position).getEsSeleccionado().equalsIgnoreCase("S")){
            holder.chkSeleccionado.setChecked(true);
        }else if(lsDetalleOrden.get(position).getEsSeleccionado().equalsIgnoreCase("N")){
            holder.chkSeleccionado.setChecked(false);
        }else{
            holder.chkSeleccionado.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return lsDetalleOrden.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }


}
