package com.neu360.x_lab.laboratorio.DTO;

import androidx.annotation.NonNull;

public class SucursalesDTO {

    private int codigoSucursal;
    private String nombreSucursal;

    public int getCodigoSucursal() {
        return codigoSucursal;
    }

    public void setCodigoSucursal(int codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    @NonNull
    @Override
    public String toString() {
        return nombreSucursal;
    }
}
