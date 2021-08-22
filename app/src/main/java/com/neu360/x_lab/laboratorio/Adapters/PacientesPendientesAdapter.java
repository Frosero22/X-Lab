package com.neu360.x_lab.laboratorio.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.Controllers.ActualizacionDeDatosActivity;
import com.neu360.x_lab.laboratorio.DTO.OrdenesDTO;
import com.neu360.x_lab.laboratorio.Util.Sesiones;

import java.util.List;

public class PacientesPendientesAdapter extends RecyclerView.Adapter<PacientesPendientesAdapter.HolderOrdenes> implements View.OnClickListener {


    private List<OrdenesDTO> lsOrdenes;
    private OrdenesDTO ordenesDTO;
    private static Context context;
    private static String fechaSeleccionada;


    private View.OnClickListener listener;

    public PacientesPendientesAdapter(List<OrdenesDTO> lsOrdenes, Context context, String fechaSeleccionada){
        this.lsOrdenes = lsOrdenes;
        this.context = context;
        this.fechaSeleccionada = fechaSeleccionada;

    }

    @Override
    public PacientesPendientesAdapter.HolderOrdenes onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pacientes_pendientes,parent,false);
        v.setOnClickListener(this);
        return new HolderOrdenes(v);
    }

    public class HolderOrdenes extends RecyclerView.ViewHolder{
        TextView txvNombrePaciente;
        TextView txvNumeroOrden;
        TextView txvIdentificacion;
        TextView txvGenero;
        Button btnTomarAtencion;

        public HolderOrdenes(@NonNull View itemView) {
            super(itemView);

            txvNumeroOrden = itemView.findViewById(R.id.txvNumeroOrden);
            txvNombrePaciente = itemView.findViewById(R.id.txvNombrePaciente);
            txvIdentificacion = itemView.findViewById(R.id.txvIdentificacion);
            txvGenero = itemView.findViewById(R.id.txvGenero);
            btnTomarAtencion = itemView.findViewById(R.id.btnTomarAtencion);
            btnTomarAtencion.setOnClickListener(v -> {

                Sesiones.guardaFecha(fechaSeleccionada,context);

                Bundle extras = new Bundle();
                extras.putInt("numeroOrden",lsOrdenes.get(getAdapterPosition()).getNumeroOrden());
                extras.putInt("idPaciente",lsOrdenes.get(getAdapterPosition()).getIdPaciente());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    Intent detalle = new Intent(context.getApplicationContext(), ActualizacionDeDatosActivity.class);
                    detalle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    detalle.putExtras(extras);
                    context.getApplicationContext().startActivity(detalle);

                });
            });
        }




    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrdenes holder, int position) {

        holder.txvNumeroOrden.setText("#Órden "+lsOrdenes.get(position).getNumeroOrden());
        holder.txvNombrePaciente.setText(lsOrdenes.get(position).getNombreCliente());
        holder.txvIdentificacion.setText("CI: "+lsOrdenes.get(position).getNumeroIdentificacionCliente());

        if(lsOrdenes.get(position).getGenero().equalsIgnoreCase("M")){
            holder.txvGenero.setText("MASCULINO");
        }else if(lsOrdenes.get(position).getGenero().equalsIgnoreCase("F")){
            holder.txvGenero.setText("FEMENINO");
        }else{
            holder.txvGenero.setText("INDIFERENTE");
        }

    }

    @Override
    public int getItemCount() {
        return lsOrdenes.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }



}
