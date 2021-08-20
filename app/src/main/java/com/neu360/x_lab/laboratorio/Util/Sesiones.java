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

    public static EmpresaMovilDTO obtieneEmpresaMovil(Context context){

        EmpresaMovilDTO empresaMovilDTO = new EmpresaMovilDTO();
        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_EMPRESA_MOVIL", Context.MODE_PRIVATE);

        empresaMovilDTO.setCodigoEmpresa(sharedPreferences.getInt("codigoEmpresa",0));
        empresaMovilDTO.setNumeroIdentificacion(sharedPreferences.getString("numeroIdentificacion",""));
        empresaMovilDTO.setNombreWarWsXHealth(sharedPreferences.getString("nombreWarWsXHealth",""));
        empresaMovilDTO.setDireccionIpWsXHealth(sharedPreferences.getString("direccionIpWsXHealth",""));


        return empresaMovilDTO;


    }

    public  static void guardaUsuario(UsuarioDTO usuarioDTO, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_USUARIO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("codigoUsuario",usuarioDTO.getCodigoUsuario());
        editor.putString("tokenType",usuarioDTO.getTokenType());
        editor.putString("idToken",usuarioDTO.getIdToken());
        editor.apply();

    }

    public static UsuarioDTO obtenerUsuario(Context context){

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_USUARIO", Context.MODE_PRIVATE);
        usuarioDTO.setTokenType(sharedPreferences.getString("tokenType",""));
        usuarioDTO.setIdToken(sharedPreferences.getString("idToken",""));
        usuarioDTO.setCodigoUsuario(sharedPreferences.getString("codigoUsuario",""));

        return usuarioDTO;


    }

    public  static void guardaSucursal(SucursalesDTO sucursalesDTO, Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_SUCURSAL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombreSucursal",sucursalesDTO.getNombreSucursal());
        editor.putInt("codigoSucursal",sucursalesDTO.getCodigoSucursal());
        editor.apply();

    }

    public static SucursalesDTO obtieneESucursal(Context context){

        SucursalesDTO sucursalesDTO = new SucursalesDTO();
        SharedPreferences sharedPreferences = context.getSharedPreferences("CRENDECIALES_SUCURSAL", Context.MODE_PRIVATE);
        sucursalesDTO.setCodigoSucursal(sharedPreferences.getInt("codigoSucursal",0));
        sucursalesDTO.setNombreSucursal(sharedPreferences.getString("nombreSucursal",""));


        return sucursalesDTO;


    }

}
