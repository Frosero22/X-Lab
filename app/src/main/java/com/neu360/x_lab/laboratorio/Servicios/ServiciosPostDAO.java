package com.neu360.x_lab.laboratorio.Servicios;

import android.content.Context;

import com.neu360.x_lab.laboratorio.Util.Varios;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServiciosPostDAO {

    public static Request requestAutenticacion(Context context, String strTipoRequest, String strUrl, String strUsuario, String strClave){

        Request request;
        String strAuth = Varios.generarHeader(context,strUsuario,strClave);
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        request = new Request.Builder().url(strUrl)
                .method(strTipoRequest, body)
                .addHeader("Authorization", strAuth)
                .build();

        return request;

    }

}
