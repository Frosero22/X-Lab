package com.neu360.x_lab.laboratorio.DTO;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestGenDTO {

    private OkHttpClient okHttpClient;
    private Request request;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
