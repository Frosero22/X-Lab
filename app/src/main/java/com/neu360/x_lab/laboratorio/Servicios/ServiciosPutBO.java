package com.neu360.x_lab.laboratorio.Servicios;

import android.content.Context;

import com.neu360.x_lab.laboratorio.DTO.RequestGenDTO;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServiciosPutBO {

    public static RequestGenDTO genRequestActualizaDatos(Context context, String strIp, String strWar, String strJson, String strToken, String strTokenType, int codigoCliente){

        RequestGenDTO requestGenDTO = new RequestGenDTO();
        OkHttpClient cliente = HttpRequest.buildHttpRequest(15, 15, 15);
        Request request = ServiciosPutDAO.genRequestActualizaDatos(context,"PUT",
                                                            strIp+"/"+strWar+"/v1/pacientes/"+codigoCliente,
                                                                   strJson,
                                                                   strToken,
                                                                   strTokenType);

        requestGenDTO.setOkHttpClient(cliente);
        requestGenDTO.setRequest(request);

        return requestGenDTO;

    }

}
