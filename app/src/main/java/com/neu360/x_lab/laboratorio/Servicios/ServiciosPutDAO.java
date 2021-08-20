package com.neu360.x_lab.laboratorio.Servicios;

import android.content.Context;

import com.neu360.x_lab.laboratorio.Util.Varios;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServiciosPutDAO {

    public static Request genRequestActualizaDatos(Context context, String strTipoRequest, String strUrl, String strJson, String strToken, String strTokenType){

        Request request;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, strJson);
        request = new Request.Builder().url(strUrl)
                .method(strTipoRequest, body)
                .addHeader("Authorization",strTokenType+" "+strToken)
                .build();

        return request;

    }

}
