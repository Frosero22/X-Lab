package com.neu360.x_lab.laboratorio.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.neu360.x_lab.laboratorio.DTO.EmpresaMovilDTO;
import com.neu360.x_lab.laboratorio.DTO.EmpresasDTO;
import com.neu360.x_lab.laboratorio.DTO.SucursalesDTO;
import com.neu360.x_lab.laboratorio.DTO.UsuarioDTO;

public class Sesiones {

    public  static void guardaEmpresaMovil(EmpresaMovilDTO objEmpresaMovilDTO, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_EMPRESA_MOVIL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numeroIdentificacion",objEmpresaMovilDTO.getNumeroIdentificacion());
        editor.putInt("codigoEmpresa",objEmpresaMovilDTO.getCodigoEmpresa());
        editor.putString("direccionIpWsXHealth",objEmpresaMovilDTO.getDireccionIpWsXHealth());
        editor.putString("nombreWarWsXHealth",objEmpresaMovilDTO.getNombreWarWsXHealth());
        editor.apply();

    }

    public  static void guardaUsuario(UsuarioDTO usuarioDTO, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_USUARIO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("codigoUsuario",usuarioDTO.getCodigoUsuario());
        editor.putString("tokenType",usuarioDTO.getTokenType());
        editor.putString("idToken",usuarioDTO.getIdToken());
        editor.apply();

    }

    public  static void guardaSucursal(SucursalesDTO sucursalesDTO, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_SUCURSAL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombreSucursal",sucursalesDTO.getNombreSucursal());
        editor.putInt("codigoSucursal",sucursalesDTO.getCodigoSucursal());
        editor.apply();

    }

}
