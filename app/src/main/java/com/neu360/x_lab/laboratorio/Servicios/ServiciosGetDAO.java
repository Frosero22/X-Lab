package com.neu360.x_lab.laboratorio.Servicios;

import okhttp3.Request;

public class ServiciosGetDAO {


    public static Request requestEmpresasMovil(String strTipoRequest, String strUrl){

        Request request;
        request = new Request.Builder().url(strUrl)
                .method(strTipoRequest, null)
                .build();

        return request;

    }

    public static Request requestGruposEmpresas(String strTipoRequest, String strUrl, String strToken, String strTokenType){

        Request request;
        request = new Request.Builder().url(strUrl)
                .method(strTipoRequest, null)
                .addHeader("Authorization",strTokenType+" "+strToken)
                .build();

        return request;

    }

    public static Request requestOrdenesPendientes(String strTipoRequest, String strUrl, String strToken, String strTokenType){

        Request request;
        request = new Request.Builder().url(strUrl)
                .method(strTipoRequest, null)
                .addHeader("Authorization",strTokenType+" "+strToken)
                .build();

        return request;

    }

    public static Request requesInformacionBasica(String strTipoRequest, String strUrl, String strToken, String strTokenType){

        Request request;
        request = new Request.Builder().url(strUrl)
                .method(strTipoRequest, null)
                .addHeader("Authorization",strTokenType+" "+strToken)
                .build();

        return request;

    }


}
