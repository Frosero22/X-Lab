package com.neu360.x_lab.laboratorio.Controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputLayout;
import com.neu360.x_lab.R;
import com.neu360.x_lab.laboratorio.DTO.EmpresaMovilDTO;
import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;
import com.neu360.x_lab.laboratorio.DTO.SucursalesDTO;
import com.neu360.x_lab.laboratorio.DTO.UsuarioDTO;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosGetBO;
import com.neu360.x_lab.laboratorio.Servicios.ServiciosPutBO;
import com.neu360.x_lab.laboratorio.Util.MensajesDelSistema;
import com.neu360.x_lab.laboratorio.Util.Sesiones;
import com.neu360.x_lab.laboratorio.Util.Varios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ActualizacionDeDatosActivity extends AppCompatActivity {

    private RadioButton radMasculino;
    private RadioButton radFemenino;

    private TextInputLayout inptPrimerNombre;
    private TextInputLayout inptSegundoNombre;
    private TextInputLayout inptPrimerApellido;
    private TextInputLayout inptSegundoApellido;
    private TextInputLayout inptFechaNacimiento;
    private TextInputLayout inptCorreo;
    private TextInputLayout inptTelefono;

    private EditText txtPrimerNombre;
    private EditText txtSegundoNombre;
    private EditText txtPrimerApellido;
    private EditText txtSegundoApellido;
    private EditText txtFechaNacimiento;
    private EditText txtCorreo;
    private EditText txtTelefono;

    private Button btnActualizar;
    private Button btnFechaNacimiento;

    private EmpresaMovilDTO empresaMovilDTO;
    private UsuarioDTO usuarioDTO;

    private ProgressDialog progressDialog;

    private int idPaciente;
    private int numeroOrden;
    private int codigoCliente;

    private DatePickerDialog datePickerDialog;

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;

            String mes = null;
            if(month < 10){
                 mes = "0"+month;
            }

            String date;
            if(mes != null){

                date = day+"/"+mes+"/"+year;

            }else{

                date = makeDateString(day, month, year);

            }
            txtFechaNacimiento.setText(date);


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
        setContentView(R.layout.activity_actualizacion_de_datos);
        initDatePicker();
        initComponents();
    }

    public void initComponents(){

        getSupportActionBar().setTitle("Actualización de Datos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getIntent().getExtras();
        idPaciente = bundle.getInt("idPaciente",0);
        numeroOrden = bundle.getInt("numeroOrden",0);

        empresaMovilDTO = Sesiones.obtieneEmpresaMovil(ActualizacionDeDatosActivity.this);
        usuarioDTO = Sesiones.obtenerUsuario(ActualizacionDeDatosActivity.this);

        radMasculino = findViewById(R.id.radMasculino);
        radFemenino = findViewById(R.id.radFemenino);

        inptPrimerNombre = findViewById(R.id.txtInpPrimerNombre);
        inptSegundoNombre = findViewById(R.id.txtInpSegundoNombre);
        inptPrimerApellido = findViewById(R.id.txtInpPrimerApellido);
        inptSegundoApellido = findViewById(R.id.txtInpSegundoApellido);
        inptFechaNacimiento = findViewById(R.id.txtInpFechaNacimiento);
        inptCorreo = findViewById(R.id.txtInpCorreo);
        inptTelefono = findViewById(R.id.txtInptTelefono);

        txtPrimerNombre = findViewById(R.id.txtPrimerNombre);
        txtSegundoNombre = findViewById(R.id.txtSegundoNombre);
        txtPrimerApellido = findViewById(R.id.txtPrimerApellido);
        txtSegundoApellido = findViewById(R.id.txtSegundoApellido);
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtTelefono = findViewById(R.id.txtTelefono);

        radMasculino.setOnClickListener(v -> {

            if(radFemenino.isChecked()){
                radFemenino.setChecked(false);
                radMasculino.setChecked(true);
            }

        });

        radFemenino.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(radMasculino.isChecked()){
                radMasculino.setChecked(false);
            }

        });

        btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(v -> {
            try {
                armaEstructuaJSON();
            } catch (JSONException e) {
                e.printStackTrace();
                MensajesDelSistema.mensajeError(ActualizacionDeDatosActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
            }
        });

        btnFechaNacimiento = findViewById(R.id.btnTomarFechaNacimiento);
        btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        obtieneInformacionBasica();

    }

    public void openDatePicker()
    {
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.item_actualizacion_paciente,menu);
        MenuItem noActualizar = menu.findItem(R.id.noActualizar);

        noActualizar.setOnMenuItemClickListener(item -> {

            new AlertDialog.Builder(ActualizacionDeDatosActivity.this)
                    .setMessage("¿Está seguro/a de continuar sin actualizar?, El correó electroníco actual sera usado para enviar sus resultados")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        dialog.dismiss();
                        goToDetalleOrden();
                    }).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss()).show();

            return false;

        });

        return true;

    }




    public void obtieneInformacionBasica(){

        progressDialog = Varios.progressDialog(ActualizacionDeDatosActivity.this,"Cargando...");

        RequestGenDTO requestGenDTO = ServiciosGetBO.obtenerInformacionBasica(empresaMovilDTO.getCodigoEmpresa(),
                                                                              idPaciente,
                                                                              empresaMovilDTO.getDireccionIpWsXHealth(),
                                                                              empresaMovilDTO.getNombreWarWsXHealth(),
                                                                              usuarioDTO.getIdToken(),
                                                                              usuarioDTO.getTokenType());

        requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                MensajesDelSistema.mensajeError(ActualizacionDeDatosActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {

                try{


                    ResponseBody responseBody = response.body();
                    JSONObject json = new JSONObject(responseBody.string());
                    progressDialog.dismiss();
                    JSONArray jsonArray = json.getJSONArray("data");
                    armaData(jsonArray);


                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeError(ActualizacionDeDatosActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
                }




            }
        });

    }

    public void armaData(JSONArray jsonArray){

        runOnUiThread(() -> {

            try {
                txtPrimerNombre.setText(jsonArray.getJSONObject(0).getString("primerNombre"));

                if(jsonArray.getJSONObject(0).getString("segundoNombre") != null && !jsonArray.getJSONObject(0).getString("segundoNombre").equalsIgnoreCase("null")){
                    txtSegundoNombre.setText(jsonArray.getJSONObject(0).getString("segundoNombre"));
                }else{
                    txtSegundoNombre.setText("");
                }

                txtPrimerApellido.setText(jsonArray.getJSONObject(0).getString("primerApellido"));

                if(jsonArray.getJSONObject(0).getString("segundoApellido") != null && !jsonArray.getJSONObject(0).getString("segundoApellido").equalsIgnoreCase("null")){
                    txtSegundoApellido.setText(jsonArray.getJSONObject(0).getString("segundoApellido"));
                }else{
                    txtSegundoApellido.setText("");
                }


                if(jsonArray.getJSONObject(0).getString("genero").equalsIgnoreCase("M")){
                    radMasculino.setChecked(true);
                }else if(jsonArray.getJSONObject(0).getString("genero").equalsIgnoreCase("F")){
                    radFemenino.setChecked(true);
                }

                if(jsonArray.getJSONObject(0).getString("fechaNacimiento") != null || !jsonArray.getJSONObject(0).getString("fechaNacimiento").equalsIgnoreCase("null")){
                    txtFechaNacimiento.setText(jsonArray.getJSONObject(0).getString("fechaNacimiento"));
                }else{
                    txtFechaNacimiento.setText("");
                }

                if(jsonArray.getJSONObject(0).getString("email") != null && !jsonArray.getJSONObject(0).getString("email").equalsIgnoreCase("null")){
                    txtCorreo.setText(jsonArray.getJSONObject(0).getString("email"));
                }else{
                    txtCorreo.setText("");
                }

                if(jsonArray.getJSONObject(0).getString("celular") != null && !jsonArray.getJSONObject(0).getString("celular").equalsIgnoreCase("null")){
                    txtTelefono.setText(jsonArray.getJSONObject(0).getString("celular"));
                }else{
                    txtTelefono.setText("");
                }

                codigoCliente = jsonArray.getJSONObject(0).getInt("codigoCliente");


            } catch (JSONException e) {

                e.printStackTrace();
                MensajesDelSistema.mensajeError(ActualizacionDeDatosActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());

            }

        });

    }

    public boolean validaComponentes(){

        if(txtPrimerNombre.getText().toString().equals("")){
            inptPrimerNombre.setErrorEnabled(true);
            inptPrimerNombre.setError("Campo Obligatorio: Primer Nombre");
            return false;
        }

        if(txtPrimerApellido.getText().toString().equals("")){
            inptPrimerApellido.setErrorEnabled(true);
            inptPrimerApellido.setError("Campo Obligatorio: Primer Apellido");
            return false;
        }

        if(txtPrimerApellido.getText().toString().equals("")){
            inptPrimerApellido.setErrorEnabled(true);
            inptPrimerApellido.setError("Campo Obligatorio: Primer Apellido");
            return false;
        }

        if(!radFemenino.isChecked() && !radMasculino.isChecked()){
            MensajesDelSistema.mensajeFlotanteCorto(ActualizacionDeDatosActivity.this,"Seleccióne un genero para continuar");
            return false;
        }

        if(txtFechaNacimiento.getText().toString().equalsIgnoreCase("")){
            inptFechaNacimiento.setErrorEnabled(true);
            inptFechaNacimiento.setError("Campo Obligatorio: Fecha de Nacimiento");
            return false;
        }

        if(txtCorreo.getText().toString().equalsIgnoreCase("")){
            inptCorreo.setErrorEnabled(true);
            inptCorreo.setError("Campo Obligatorio: Correo");
            return false;
        }

        if(txtTelefono.getText().toString().equalsIgnoreCase("")){
            inptTelefono.setErrorEnabled(true);
            inptTelefono.setError("Campo Obligatorio: Telefóno");
            return false;
        }

        return true;

    }


    public void armaEstructuaJSON() throws JSONException {

        if(validaComponentes()){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("codigoEmpresa",empresaMovilDTO.getCodigoEmpresa());
            jsonObject.put("codigoCliente",codigoCliente);
            jsonObject.put("primerNombre",txtPrimerNombre.getText().toString().toUpperCase());
            jsonObject.put("segundoNombre", txtSegundoNombre.getText() != null ? txtSegundoNombre.getText() : JSONObject.NULL);
            jsonObject.put("primerApellido",txtPrimerApellido.getText().toString().toUpperCase());
            jsonObject.put("segundoApellido", txtSegundoApellido.getText() != null ? txtSegundoApellido.getText() : JSONObject.NULL);

            if(radMasculino.isChecked()){
                jsonObject.put("genero","M");
            }else if(radFemenino.isChecked()){
                jsonObject.put("genero","F");
            }

            jsonObject.put("fechaNacimiento",txtFechaNacimiento.getText().toString());
            jsonObject.put("correoElectronico",txtCorreo.getText().toString());
            jsonObject.put("telefonoMovil",txtTelefono.getText().toString());

            Log.e("---->","JSON GENERADO --> "+jsonObject.toString());

            actualizaDatos(jsonObject.toString());

        }

    }

    public void actualizaDatos(String json){

        progressDialog = Varios.progressDialog(ActualizacionDeDatosActivity.this,"Cargando...");

        RequestGenDTO requestGenDTO = ServiciosPutBO.genRequestActualizaDatos(ActualizacionDeDatosActivity.this,
                                                                              empresaMovilDTO.getDireccionIpWsXHealth(),
                                                                              empresaMovilDTO.getNombreWarWsXHealth(),
                                                                               json,
                                                                               usuarioDTO.getIdToken(),
                                                                               usuarioDTO.getTokenType(),idPaciente);

        requestGenDTO.getOkHttpClient().newCall(requestGenDTO.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                MensajesDelSistema.mensajeErrorLooper(ActualizacionDeDatosActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{

                    ResponseBody responseBody = response.body();
                    JSONObject json = new JSONObject(responseBody.string());

                    progressDialog.dismiss();

                    if (response.code() == 200){

                        goToDetalleOrden();
                        MensajesDelSistema.mensajeFlotanteCortoLooper(ActualizacionDeDatosActivity.this,"Actualización realizada con éxito");

                    }else{

                        MensajesDelSistema.mensajeErrorLooper(ActualizacionDeDatosActivity.this,"Excepción Desconocida",json.getString("message"));

                    }





                }catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                    MensajesDelSistema.mensajeErrorLooper(ActualizacionDeDatosActivity.this,"Excepción No Controlada","Mensaje Obtenido: "+e.toString());
                }

            }
        });

    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = new Intent(ActualizacionDeDatosActivity.this,PacientesPendientesActivity.class);
        startActivity(intent);
        finish();
        return super.getParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActualizacionDeDatosActivity.this,PacientesPendientesActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToDetalleOrden(){

        Intent intent = new Intent(ActualizacionDeDatosActivity.this,DetalleDeOrdenActivity.class);

        Bundle extras = new Bundle();
        extras.putInt("numeroOrden",numeroOrden);
        extras.putInt("idPaciente",idPaciente);
        intent.putExtras(extras);

        startActivity(intent);

        finish();

    }

}