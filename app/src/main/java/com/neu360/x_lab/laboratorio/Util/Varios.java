package com.neu360.x_lab.laboratorio.Util;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.Spinner;

import com.neu360.x_lab.laboratorio.Controllers.PacientesPendientesActivity;
import com.neu360.x_lab.laboratorio.DTO.EmpresasDTO;
import com.neu360.x_lab.laboratorio.DTO.GrupoEmpresaDTO;
import com.neu360.x_lab.laboratorio.DTO.SucursalesDTO;
import com.neu360.x_lab.laboratorio.Globales.VariablesGlobales;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Varios {

    private static DatePickerDialog.OnDateSetListener setListener;
    private static  VariablesGlobales variablesGlobales = new VariablesGlobales();

    public static ProgressDialog progressDialog(Context context, String mensaje){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(mensaje);
        progressDialog.show();

        return progressDialog;
    }


    public static String generarHeader(Context context, String usuario, String clave) {
        HashMap<String, String> params = new HashMap<String, String>();
        String auth = "";
        if(usuario != null && clave != null){
            String credentials = usuario + ":" + clave;
            auth =  "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.URL_SAFE|Base64.NO_WRAP);
            Log.d(TAG, auth);
        }

        params.put("Authorization", auth);

        return auth;
    }


    public static GrupoEmpresaDTO armaArbolEmpresas(JSONArray jsonArray) throws JSONException {



        GrupoEmpresaDTO grupoEmpresa = new GrupoEmpresaDTO();

        for(int a = 0; a < jsonArray.length(); a++){

            //OBTIENE GRUPOS DE EMPRESAS

            GrupoEmpresaDTO grupoEmpresaDTO = new GrupoEmpresaDTO();
            grupoEmpresaDTO.setCodigoGrupoEmpresa(jsonArray.getJSONObject(a).getInt("codigoGrupoEmpresa"));
            grupoEmpresaDTO.setNombreGrupoEmpresa(jsonArray.getJSONObject(a).getString("nombreGrupoEmpresa"));

            JSONArray jsonEmpresas = jsonArray.getJSONObject(a).getJSONArray("empresas");

            for(int b = 0; b < jsonEmpresas.length(); b++){

                EmpresasDTO empresasDTO = new EmpresasDTO();
                empresasDTO.setCodigoEmpresa(jsonEmpresas.getJSONObject(b).getInt("codigoEmpresa"));
                empresasDTO.setNombreEmpresa(jsonEmpresas.getJSONObject(b).getString("nombreEmpresa"));

                JSONArray jsonSucursales = jsonEmpresas.getJSONObject(b).getJSONArray("sucursales");

                for(int c = 0; c < jsonSucursales.length(); c++){

                    SucursalesDTO sucursalesDTO = new SucursalesDTO();
                    sucursalesDTO.setCodigoSucursal(jsonSucursales.getJSONObject(c).getInt("codigoSucursal"));
                    sucursalesDTO.setNombreSucursal(jsonSucursales.getJSONObject(c).getString("nombreSucursal"));



                    empresasDTO.getLsSucursales().add(sucursalesDTO);


                }


                grupoEmpresa.getLsEmpresas().add(empresasDTO);

            }


        }


        return grupoEmpresa;

    }

    public static List<SucursalesDTO> filtraSucursalesXEmpresa(GrupoEmpresaDTO grupoEmpresaDTO, Integer intCodigoEmpresa){

        List<SucursalesDTO> lsSucursales = new ArrayList<>();


        for(EmpresasDTO empresasDTO : grupoEmpresaDTO.getLsEmpresas()){

            if(empresasDTO.getCodigoEmpresa() == intCodigoEmpresa){

                for(SucursalesDTO sucursalesDTO : empresasDTO.getLsSucursales()){

                    SucursalesDTO sucursales = new SucursalesDTO();
                    sucursales.setNombreSucursal(sucursalesDTO.getNombreSucursal());
                    sucursales.setCodigoSucursal(sucursalesDTO.getCodigoSucursal());

                    lsSucursales.add(sucursales);



                }

            }

        }



        return lsSucursales;
    }

    public static Object armaSpinner(List<SucursalesDTO> lsSucursales) {
        SucursalesDTO[] spinnerArraySucursales;
        spinnerArraySucursales = new SucursalesDTO[lsSucursales.size()];
        for (int i = 0; i < lsSucursales.size(); i++) {
            spinnerArraySucursales[i] = lsSucursales.get(i);
        }

        return spinnerArraySucursales;

    }








}
