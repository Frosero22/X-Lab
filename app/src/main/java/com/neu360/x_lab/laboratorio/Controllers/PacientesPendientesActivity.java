package com.neu360.x_lab.laboratorio.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;

import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.Adapters.PacientesPendientesAdapter;
import com.neu360.x_lab.laboratorio.DTO.EmpresaMovilDTO;
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

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            listaOrdenesPendientes(date);

        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = simpleDateFormat.format(new Date());

        lsOrdenes = new ArrayList<>();
        lsAuxiliar = new ArrayList<>();

        registerForContextMenu(recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        pacientesPendientesAdapter = new PacientesPendientesAdapter(lsOrdenes,PacientesPendientesActivity.this);
        recyclerView.setAdapter(pacientesPendientesAdapter);

        recyclerView.setAdapter(pacientesPendientesAdapter);

        listaOrdenesPendientes(fechaActual);

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
                MensajesDelSistema.mensajeError(PacientesPendientesActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{

                    ResponseBody responseBody = response.body();
                    JSONObject json = new JSONObject(responseBody.string());

                    if(response.code() == 200){

                        progressDialog.dismiss();
                        JSONArray jsonArray = json.getJSONArray("data");

                        for(int a = 0; a < jsonArray.length(); a++){

                            JSONObject jsonObject = jsonArray.getJSONObject(a);

                            armaOrdenes(jsonObject.getInt("codigoEmpresa"),
                                        jsonObject.getInt("numeroOrden"),
                                        jsonObject.getInt("idPaciente"),
                                        jsonObject.getString("nombreCliente"),
                                        jsonObject.getString("numeroIdentificacionCliente"),
                                        jsonObject.getString("genero"));

                        }

                    }else{

                        progressDialog.dismiss();
                        MensajesDelSistema.mensajeError(PacientesPendientesActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+json.getString("message"));


                    }


                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeError(PacientesPendientesActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
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






}