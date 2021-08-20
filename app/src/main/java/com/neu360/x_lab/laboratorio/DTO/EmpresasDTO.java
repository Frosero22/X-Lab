package com.neu360.x_lab.laboratorio.DTO;

import java.util.ArrayList;
import java.util.List;

public class EmpresasDTO {

    private int codigoEmpresa;
    private String nombreEmpresa;
    private List<SucursalesDTO> lsSucursales = new ArrayList<>();
    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public List<SucursalesDTO> getLsSucursales() {
        return lsSucursales;
    }

    public void setLsSucursales(List<SucursalesDTO> lsSucursales) {
        this.lsSucursales = lsSucursales;
    }


}
