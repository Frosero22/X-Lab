package com.neu360.x_lab.laboratorio.Servicios;

import android.content.Context;

import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServiciosPostBO {

    public static RequestGenDTO genRequestAutenticacion(Context context, String strIp, String strWar, String strUsuario, String strClave){

        RequestGenDTO requestGenDTO = new RequestGenDTO();
        OkHttpClient cliente = HttpRequest.buildHttpRequest(15, 15, 15);
        Request request = ServiciosPostDAO.requestAutenticacion(context,"POST",
                strIp+"/"+strWar+"/v1/autenticacion/login",
                strUsuario,
                strClave);

        requestGenDTO.setOkHttpClient(cliente);
        requestGenDTO.setRequest(request);

        return requestGenDTO;

    }

}
