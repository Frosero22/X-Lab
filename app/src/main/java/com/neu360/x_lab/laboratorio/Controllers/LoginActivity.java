package com.neu360.x_lab.laboratorio.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.DTO.EmpresaMovilDTO;
import com.neu360.x_lab.laboratorio.DTO.EmpresasDTO;
import com.neu360.x_lab.laboratorio.DTO.GrupoEmpresaDTO;
import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;
import com.neu360.x_lab.laboratorio.DTO.SucursalesDTO;
import com.neu360.x_lab.laboratorio.DTO.UsuarioDTO;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosGetBO;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosGetDAO;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosPostBO;
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
import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity {

    private TextView txvTexto;

    private TextInputLayout inptRuc;
    private TextInputLayout inptUsuario;
    private TextInputLayout inptPassword;

    private EditText editRuc;
    private EditText editUsuario;
    private EditText editPassword;

    private Button btnAutenticar;

    private EmpresaMovilDTO empresaMovilDTO;

    private ProgressDialog progressDialog;

    private SucursalesDTO[] spinnerArraySucursales;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initComponentes();
    }

    private void initComponentes(){



        txvTexto = findViewById(R.id.text_view_texto);

        inptRuc = findViewById(R.id.input_ruc);
        inptUsuario = findViewById(R.id.input_user);
        inptPassword = findViewById(R.id.input_password);

        editRuc = findViewById(R.id.edit_ruc);
        editUsuario = findViewById(R.id.edit_user);
        editPassword = findViewById(R.id.edit_password);

        btnAutenticar = findViewById(R.id.btn_iniciar_sesion);
        btnAutenticar.setOnClickListener(v -> {

            if(empresaMovilDTO != null){

                autenticarUsuario();

            }else{

                validaEmpresa();

            }


        });

        if(empresaMovilDTO != null){

            habilitaComponentes();

        }

    }


    public void habilitaComponentes(){
        runOnUiThread(() -> {

            inptRuc.setVisibility(View.GONE);
            editRuc.setVisibility(View.GONE);

            inptUsuario.setVisibility(View.VISIBLE);
            inptPassword.setVisibility(View.VISIBLE);

            editUsuario.setVisibility(View.VISIBLE);
            editPassword.setVisibility(View.VISIBLE);

            txvTexto.setText("Ingrese sus datos para continuar");

            btnAutenticar.setText("Iniciar Sesión");


        });
    }


    public void validaEmpresa(){

        progressDialog = Varios.progressDialog(LoginActivity.this,"Cargando...");

        RequestGenDTO requestGenDTO = ServiciosGetBO.obtenerEmpresaMovil(editRuc.getText().toString());

        requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                MensajesDelSistema.mensajeError(LoginActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{

                    ResponseBody responseBody = response.body();
                    JSONObject json = new JSONObject(responseBody.string());

                    if(response.code() == 200){

                        progressDialog.dismiss();

                        String dafEmpresasMovil = json.getString("dafEmpresasMovil");
                        if(dafEmpresasMovil != null || !dafEmpresasMovil.equalsIgnoreCase("null")){

                            JSONObject jsonEmpresa = new JSONObject(dafEmpresasMovil);
                            empresaMovilDTO = new EmpresaMovilDTO();

                            empresaMovilDTO.setCodigoEmpresa(jsonEmpresa.getInt("codigoEmpresa"));
                            empresaMovilDTO.setDireccionIpWsXHealth(jsonEmpresa.getString("direccionIpWsXHealth"));
                            empresaMovilDTO.setNombreWarWsXHealth(jsonEmpresa.getString("nombreWarWsXHealth"));
                            empresaMovilDTO.setNumeroIdentificacion(jsonEmpresa.getString("numeroIdentificacion"));

                            Sesiones.guardaEmpresaMovil(empresaMovilDTO,LoginActivity.this);
                            habilitaComponentes();

                        }else{

                            MensajesDelSistema.mensajeError(LoginActivity.this,"Error en la Transacción","El numero de ruc ingresado es incorrecto");

                        }





                    }else{

                        progressDialog.dismiss();
                        MensajesDelSistema.mensajeError(LoginActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+json.getString("message"));


                    }


                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeError(LoginActivity.this,"Error en la Transacción","El numero de ruc ingresado no es valido.");
                }




            }
        });


    }

    public boolean validaComponentes(){

        if(editUsuario.getText().toString().equals("")){
            inptUsuario.setErrorEnabled(true);
            inptUsuario.setError("Campo Obligatorio");
            return false;
        }

        if(editPassword.getText().toString().equals("")){
            inptPassword.setErrorEnabled(true);
            inptPassword.setError("Campo Obligatorio");
            return false;
        }


        return true;
    }

    public void autenticarUsuario(){

        try{

            if(validaComponentes()){

                progressDialog = Varios.progressDialog(LoginActivity.this,"Cargando...");

                RequestGenDTO requestGenDTO = ServiciosPostBO.genRequestAutenticacion(LoginActivity.this,
                                                                                     empresaMovilDTO.getDireccionIpWsXHealth(),
                                                                                     empresaMovilDTO.getNombreWarWsXHealth(),
                                                                                     editUsuario.getText().toString().toUpperCase(),
                                                                                     editPassword.getText().toString());

                requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        MensajesDelSistema.mensajeErrorLooper(LoginActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        try{

                            ResponseBody responseBody = response.body();
                            JSONObject json = new JSONObject(responseBody.string());

                            if(response.code() == 200){



                                String data = json.getString("data");
                                JSONObject jsonUsuario = new JSONObject(data);

                                UsuarioDTO usuarioDTO = new UsuarioDTO();
                                usuarioDTO.setCodigoUsuario(jsonUsuario.getString("codigoUsuario"));
                                usuarioDTO.setIdToken(jsonUsuario.getString("idToken"));
                                usuarioDTO.setTokenType(jsonUsuario.getString("tokenType"));

                                Sesiones.guardaUsuario(usuarioDTO,LoginActivity.this);

                                obtieneGrupoEmpresas(usuarioDTO);








                            }else{

                                progressDialog.dismiss();
                                MensajesDelSistema.mensajeErrorLooper(LoginActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+json.getString("message"));

                            }


                        }catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                            MensajesDelSistema.mensajeErrorLooper(LoginActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
                        }


                    }
                });

            }




        }catch (Exception e){
            progressDialog.dismiss();
            e.printStackTrace();
            MensajesDelSistema.mensajeError(LoginActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
        }

    }

    public void obtieneGrupoEmpresas(UsuarioDTO usuarioDTO){
        runOnUiThread(() -> {


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
                    MensajesDelSistema.mensajeErrorLooper(LoginActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
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
                            MensajesDelSistema.mensajeErrorLooper(LoginActivity.this,"Error en la Transacción",json.getString("message"));


                        }










                    }catch (Exception e){
                        progressDialog.dismiss();
                        e.printStackTrace();
                        MensajesDelSistema.mensajeErrorLooper(LoginActivity.this,"Error en la Transacción","Excepción no Controlada, Mensaje obtenido: "+e.toString());
                    }

                }
            });
        });



    }



    private void preparaSelector() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final View view = getLayoutInflater().inflate(R.layout.sucursales, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Selecciona la sucursal");
                alertDialog.setCancelable(false);

                final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                ArrayAdapter adapter = new ArrayAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, spinnerArraySucursales);
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
                            Sesiones.guardaSucursal(sub,LoginActivity.this);
                            goToPacientes();
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
            }
        });

    }

    public void goToPacientes(){

        Intent intent = new Intent(LoginActivity.this,PacientesPendientesActivity.class);
        startActivity(intent);
        finish();

    }

}