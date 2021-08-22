package com.neu360.x_lab.laboratorio.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.Adapters.PacientesPendientesAdapter;
import com.neu360.x_lab.laboratorio.DTO.EmpresaMovilDTO;
import com.neu360.x_lab.laboratorio.DTO.GrupoEmpresaDTO;
import com.neu360.x_lab.laboratorio.DTO.OrdenesDTO;
import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;
import com.neu360.x_lab.laboratorio.DTO.SucursalesDTO;
import com.neu360.x_lab.laboratorio.DTO.UsuarioDTO;
import com.neu360.x_lab.laboratorio.Globales.VariablesGlobales;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosGetBO;
import com.neu360.x_lab.laboratorio.Util.MensajesDelSistema;
import com.neu360.x_lab.laboratorio.Util.Sesiones;
import com.neu360.x_lab.laboratorio.Util.Varios;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PacientesPendientesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<OrdenesDTO> lsOrdenes;
    private ArrayList<OrdenesDTO> lsAuxiliar;

    private PacientesPendientesAdapter pacientesPendientesAdapter;
    private ProgressDialog progressDialog;

    private EmpresaMovilDTO empresaMovilDTO;
    private SucursalesDTO sucursalesDTO;
    private UsuarioDTO usuarioDTO;


    private DatePickerDialog datePickerDialog;

    private SucursalesDTO[] spinnerArraySucursales;

    private String fechaSeleccionada;

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            fechaSeleccionada = makeDateString(day, month, year);
            listaOrdenesPendientes(fechaSeleccionada);

        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);


    }

    private String makeDateString(int day, int month, int year)
    {
        return day+"/"+month+"/"+year;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes_pendientes);
        initDatePicker();
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.item_ordenes_pendientes,menu);
        MenuItem filtrarFecha = menu.findItem(R.id.calendario);
        MenuItem cerrarSesion = menu.findItem(R.id.cerrarSesion);
        MenuItem cambiarSucursal = menu.findItem(R.id.cambiarSucursal);
        MenuItem reiniciarAplicacion = menu.findItem(R.id.reiniciarAplicacion);

        filtrarFecha.setOnMenuItemClickListener(item -> {

            openDatePicker();
            return false;

        });

        cerrarSesion.setOnMenuItemClickListener(item -> {
            Sesiones.borrarUsuario(PacientesPendientesActivity.this);
            goToLogin();
            return false;
        });

        cambiarSucursal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                obtieneGrupoEmpresas(usuarioDTO);
                return false;
            }
        });

        reiniciarAplicacion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Sesiones.borrarSesion(PacientesPendientesActivity.this);
                goToLogin();
                return false;
            }
        });

        return true;

    }

    public void openDatePicker()
    {
        datePickerDialog.show();
    }

    public void initComponents(){

        empresaMovilDTO = Sesiones.obtieneEmpresaMovil(PacientesPendientesActivity.this);
        sucursalesDTO = Sesiones.obtieneESucursal(PacientesPendientesActivity.this);
        usuarioDTO = Sesiones.obtenerUsuario(PacientesPendientesActivity.this);
        recyclerView = findViewById(R.id.lista_pacientes_pendientes);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


        lsOrdenes = new ArrayList<>();
        lsAuxiliar = new ArrayList<>();

        registerForContextMenu(recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        pacientesPendientesAdapter = new PacientesPendientesAdapter(lsOrdenes,PacientesPendientesActivity.this,fechaSeleccionada);
        recyclerView.setAdapter(pacientesPendientesAdapter);

        recyclerView.setAdapter(pacientesPendientesAdapter);

        getSupportActionBar().setTitle("Pacientes Pendientes");

        fechaSeleccionada = Sesiones.obtieneFecha(PacientesPendientesActivity.this);

        if(fechaSeleccionada == null || fechaSeleccionada.equalsIgnoreCase("")){
            fechaSeleccionada = simpleDateFormat.format(new Date());
        }




        listaOrdenesPendientes(fechaSeleccionada);


    }


    public void listaOrdenesPendientes(String strFecha){

        progressDialog = Varios.progressDialog(PacientesPendientesActivity.this,"Cargando...");

        RequestGenDTO requestGenDTO = ServiciosGetBO.obtenerOrdenesPendientes(empresaMovilDTO.getCodigoEmpresa(),
                                                                              sucursalesDTO.getCodigoSucursal(),
                                                                              strFecha,
                                                                              empresaMovilDTO.getDireccionIpWsXHealth(),
                                                                              empresaMovilDTO.getNombreWarWsXHealth(),
                                                                              usuarioDTO.getIdToken(),
                                                                              usuarioDTO.getTokenType());

        requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                MensajesDelSistema.mensajeErrorLooper(PacientesPendientesActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{

                    ResponseBody responseBody = response.body();
                    JSONObject json = new JSONObject(responseBody.string());

                    if(response.code() == 200){

                        progressDialog.dismiss();
                        JSONArray jsonArray = json.getJSONArray("data");

                        if(jsonArray.length() < 1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(null);
                                }
                            });

                            MensajesDelSistema.mensajeFlotanteCortoLooper(PacientesPendientesActivity.this,"No hay pacientes en espera actualmente");

                        }else{

                            for(int a = 0; a < jsonArray.length(); a++){

                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                armaOrdenes(jsonObject.getInt("codigoEmpresa"),
                                            jsonObject.getInt("numeroOrden"),
                                            jsonObject.getInt("idPaciente"),
                                            jsonObject.getString("nombreCliente"),
                                            jsonObject.getString("numeroIdentificacionCliente"),
                                            jsonObject.getString("genero"));

                            }

                            runOnUiThread(() -> {
                                pacientesPendientesAdapter = new PacientesPendientesAdapter(lsOrdenes,PacientesPendientesActivity.this,fechaSeleccionada);
                                recyclerView.setAdapter(pacientesPendientesAdapter);
                                pacientesPendientesAdapter.notifyDataSetChanged();
                            });

                        }



                    }else{

                        progressDialog.dismiss();
                        MensajesDelSistema.mensajeErrorLooper(PacientesPendientesActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+json.getString("message"));


                    }


                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeErrorLooper(PacientesPendientesActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
                }

            }
        });


    }


    public void armaOrdenes(int codigoEmpresa, int numeroOrden, int idPaciente, String strNombreCliente, String strIdentificacion, String strGenero){

        runOnUiThread(() -> {

            OrdenesDTO ordenesDTO = new OrdenesDTO();
            ordenesDTO.setCodigoEmpresa(codigoEmpresa);
            ordenesDTO.setNumeroOrden(numeroOrden);
            ordenesDTO.setIdPaciente(idPaciente);
            ordenesDTO.setNombreCliente(strNombreCliente);
            ordenesDTO.setNumeroIdentificacionCliente(strIdentificacion);
            ordenesDTO.setGenero(strGenero);


            lsOrdenes.add(ordenesDTO);
            lsAuxiliar.add(ordenesDTO);
            pacientesPendientesAdapter.notifyDataSetChanged();

        });

    }

    public void obtieneGrupoEmpresas(UsuarioDTO usuarioDTO){
        runOnUiThread(() -> {

            progressDialog = Varios.progressDialog(PacientesPendientesActivity.this,"Cargando...");

            RequestGenDTO requestGenDTO = ServiciosGetBO.obtenerGruposEmpresas(usuarioDTO.getCodigoUsuario(),
                    empresaMovilDTO.getDireccionIpWsXHealth(),
                    empresaMovilDTO.getNombreWarWsXHealth(),
                    usuarioDTO.getIdToken(),
                    usuarioDTO.getTokenType());

            requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeErrorLooper(PacientesPendientesActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {

                        ResponseBody responseBody = response.body();
                        JSONObject json = new JSONObject(responseBody.string());

                        if(response.code() == 200){

                            JSONArray data = json.getJSONArray("data");
                            GrupoEmpresaDTO grupoEmpresaDTO = Varios.armaArbolEmpresas(data);
                            List<SucursalesDTO> lsSucursales = Varios.filtraSucursalesXEmpresa(grupoEmpresaDTO,empresaMovilDTO.getCodigoEmpresa());
                            spinnerArraySucursales = (SucursalesDTO[]) Varios.armaSpinner(lsSucursales);

                            progressDialog.dismiss();

                            preparaSelector();



                        }else{

                            progressDialog.dismiss();
                            MensajesDelSistema.mensajeErrorLooper(PacientesPendientesActivity.this,"Error en la Transacción",json.getString("message"));


                        }










                    }catch (Exception e){
                        progressDialog.dismiss();
                        e.printStackTrace();
                        MensajesDelSistema.mensajeErrorLooper(PacientesPendientesActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
                    }

                }
            });
        });



    }

    private void preparaSelector() {
        runOnUiThread((Runnable) () -> {
            final View view = getLayoutInflater().inflate(R.layout.sucursales, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(PacientesPendientesActivity.this).create();
            alertDialog.setTitle("Selecciona la sucursal");
            alertDialog.setCancelable(false);

            final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
            ArrayAdapter adapter = new ArrayAdapter(PacientesPendientesActivity.this, android.R.layout.simple_spinner_item, spinnerArraySucursales);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SucursalesDTO sub = (SucursalesDTO)spinner.getSelectedItem();

                    if (sub == null ){
                        Toast.makeText(getApplicationContext(), "Selecciona la sucursal", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Sesiones.guardaSucursal(sub,PacientesPendientesActivity.this);
                        reload();
                    }


                }
            });


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });


            alertDialog.setView(view);
            alertDialog.show();
        });

    }

    public void goToLogin(){
        Intent intent = new Intent(PacientesPendientesActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void reload(){
        Intent intent = new Intent(PacientesPendientesActivity.this,PacientesPendientesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }



}