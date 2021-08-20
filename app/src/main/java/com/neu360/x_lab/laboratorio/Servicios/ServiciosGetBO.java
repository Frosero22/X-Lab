package com.neu360.x_lab.laboratorio.Servicios;

import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServiciosGetBO {

    public static RequestGenDTO obtenerEmpresaMovil(String strRuc){

        RequestGenDTO requestGenDTO = new RequestGenDTO();

        OkHttpClient cliente = HttpRequest.buildHttpRequest(15,15,15);
        Request request = ServiciosGetDAO.requestEmpresasMovil("GET",
                "http://magkaz.neu360.com:8080/X-uitWSRest/AccesosPolux/obtenerEmpresaMovil?arg0="+strRuc);

        requestGenDTO.setOkHttpClient(cliente);
        requestGenDTO.setRequest(request);

        return requestGenDTO;

    }

    public static RequestGenDTO obtenerGruposEmpresas(String strCodigoUsuario, String strIp, String strWar, String strToken, String strTokenType){

        RequestGenDTO requestGenDTO = new RequestGenDTO();

        OkHttpClient cliente = HttpRequest.buildHttpRequest(15,15,15);
        Request request = ServiciosGetDAO.requestGruposEmpresas("GET",
                strIp+"/"+strWar+"/v1/usuarios/"+strCodigoUsuario+"/grupos_empresa",
                strToken,
                strTokenType);

        requestGenDTO.setOkHttpClient(cliente);
        requestGenDTO.setRequest(request);

        return requestGenDTO;

    }

}
