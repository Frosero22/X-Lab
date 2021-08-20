package com.neu360.x_lab.laboratorio.Controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.Adapters.DetalleOrdenAdapter;
import com.neu360.x_lab.laboratorio.DTO.DetalleOrdenDTO;
import com.neu360.x_lab.laboratorio.DTO.EmpresaMovilDTO;
import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;
import com.neu360.x_lab.laboratorio.DTO.SucursalesDTO;
import com.neu360.x_lab.laboratorio.DTO.UsuarioDTO;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosGetBO;
import com.neu360.x_lab.laboratorio.Util.MensajesDelSistema;
import com.neu360.x_lab.laboratorio.Util.Sesiones;
import com.neu360.x_lab.laboratorio.Util.Varios;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DetalleDeOrdenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<DetalleOrdenDTO> lsDetalle;
    private ArrayList<DetalleOrdenDTO> lsAuxiliar;

    private DetalleOrdenAdapter detalleOrdenAdapter;
    private ProgressDialog progressDialog;

    private EmpresaMovilDTO empresaMovilDTO;
    private SucursalesDTO sucursalesDTO;
    private UsuarioDTO usuarioDTO;


    private int idPaciente;
    private int numeroOrden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_de_orden);
        initComponentes();
    }

    public void initComponentes(){

        getSupportActionBar().setTitle("Detalle de Ordén");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empresaMovilDTO = Sesiones.obtieneEmpresaMovil(DetalleDeOrdenActivity.this);
        sucursalesDTO = Sesiones.obtieneESucursal(DetalleDeOrdenActivity.this);
        usuarioDTO = Sesiones.obtenerUsuario(DetalleDeOrdenActivity.this);

        recyclerView = findViewById(R.id.lista_detalle_orden);

        Bundle bundle = this.getIntent().getExtras();
        idPaciente = bundle.getInt("idPaciente",0);
        numeroOrden = bundle.getInt("numeroOrden",0);

        lsDetalle = new ArrayList<>();
        lsAuxiliar = new ArrayList<>();

        registerForContextMenu(recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        detalleOrdenAdapter = new DetalleOrdenAdapter(lsDetalle,DetalleDeOrdenActivity.this);
        recyclerView.setAdapter(detalleOrdenAdapter);

        listaDetalleOrdenes();

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.item_detalle_ordenes,menu);
        MenuItem seleccionarTodos = menu.findItem(R.id.seleccionarTodos);
        MenuItem imprimir = menu.findItem(R.id.imprimir);

        seleccionarTodos.setOnMenuItemClickListener(item -> {
            seleccionarTodos();
            return false;
        });

        imprimir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });



        return true;

    }


    public void armaArregloImpresion(){

        List<DetalleOrdenDTO> lsDetalleOrden = new ArrayList<>();

        for(DetalleOrdenDTO detalleOrdenDTO : lsDetalleOrden){

            if(detalleOrdenDTO.getEsSeleccionado().equalsIgnoreCase("S")){


                lsDetalleOrden.add(detalleOrdenDTO);

            }

        }

    }

    public void seleccionarTodos(){

        runOnUiThread(() -> {
            int cantSeleccionados = 0;

            for(DetalleOrdenDTO detalleOrdenDTO : lsDetalle){

                if(detalleOrdenDTO.getEsSeleccionado().equalsIgnoreCase("S")){
                    cantSeleccionados++;
                }

            }

            if(cantSeleccionados == lsDetalle.size()){

                for(DetalleOrdenDTO detalleOrdenDTO : lsDetalle){

                    detalleOrdenDTO.setEsSeleccionado("N");

                }

                MensajesDelSistema.mensajeFlotanteCorto(DetalleDeOrdenActivity.this,"Todos los elementos deselecciónados");

            }else{

                for(DetalleOrdenDTO detalleOrdenDTO : lsDetalle){

                    detalleOrdenDTO.setEsSeleccionado("S");

                }

                MensajesDelSistema.mensajeFlotanteCorto(DetalleDeOrdenActivity.this,"Todos los elementos selecciónados");

            }

            detalleOrdenAdapter = new DetalleOrdenAdapter(lsDetalle,DetalleDeOrdenActivity.this);
            recyclerView.setAdapter(detalleOrdenAdapter);
        });



    }

    public void listaDetalleOrdenes(){

        progressDialog = Varios.progressDialog(DetalleDeOrdenActivity.this,"Cargando...");

        RequestGenDTO requestGenDTO = ServiciosGetBO.obtenerListaDetalleOrdenes(empresaMovilDTO.getCodigoEmpresa(),
                                                                                numeroOrden,
                                                                                empresaMovilDTO.getDireccionIpWsXHealth(),
                                                                                empresaMovilDTO.getNombreWarWsXHealth(),
                                                                                usuarioDTO.getIdToken(),
                                                                                usuarioDTO.getTokenType());

        requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                MensajesDelSistema.mensajeError(DetalleDeOrdenActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());

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
                            armaDetalleOrden(jsonObject.getInt("codigoEmpresa"),
                                            jsonObject.getInt("codigoPrestacion"),
                                            jsonObject.getInt("codigoServicio"),
                                            jsonObject.getInt("lineaDetalleOrden"),
                                            jsonObject.getString("nombrePrestacion"),
                                            jsonObject.getString("nombreServicio"),
                                            jsonObject.getInt("numeroOrden"));


                        }

                    }else{

                        progressDialog.dismiss();
                        MensajesDelSistema.mensajeErrorLooper(DetalleDeOrdenActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+json.getString("message"));

                    }


                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeErrorLooper(DetalleDeOrdenActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
                }

            }
        });



    }

    public void armaDetalleOrden(int codigoEmpresa, int codigoPrestacion, int codigoServicio, int lineaDetalleOrden, String nombrePrestacion, String nombreServicio, int numeroOrden){

        runOnUiThread(() -> {

            DetalleOrdenDTO detalleOrdenDTO = new DetalleOrdenDTO();
            detalleOrdenDTO.setCodigoEmpresa(codigoEmpresa);
            detalleOrdenDTO.setCodigoPrestacion(codigoPrestacion);
            detalleOrdenDTO.setCodigoServicio(codigoServicio);
            detalleOrdenDTO.setLineaDetalleOrden(lineaDetalleOrden);
            detalleOrdenDTO.setNombrePrestacion(nombrePrestacion);
            detalleOrdenDTO.setNombreServicio(nombreServicio);
            detalleOrdenDTO.setNumeroOrden(numeroOrden);
            detalleOrdenDTO.setEsSeleccionado("N");

            lsDetalle.add(detalleOrdenDTO);
            lsAuxiliar.add(detalleOrdenDTO);
            detalleOrdenAdapter.notifyDataSetChanged();

        });

    }

    public void goToActualizacion(){

        Intent intent = new Intent(DetalleDeOrdenActivity.this,ActualizacionDeDatosActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("numeroOrden",numeroOrden);
        extras.putInt("idPaciente",idPaciente);

        intent.putExtras(extras);
        startActivity(intent);
        finish();

    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        goToActualizacion();
        return super.getParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        goToActualizacion();
    }
}