package com.neu360.x_lab.laboratorio.Servicios;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpRequest {

    private static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient buildHttpRequest(Integer intConnectTimeOuT, Integer intReadTimeOut, Integer intWriteTimeOut){

        OkHttpClient cliente;

        cliente = client.newBuilder().connectTimeout(intConnectTimeOuT, TimeUnit.SECONDS)
                .readTimeout(intReadTimeOut, TimeUnit.SECONDS)
                .writeTimeout(intWriteTimeOut, TimeUnit.SECONDS)
                .build();

        return cliente;

    }

}
