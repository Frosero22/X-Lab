package com.neu360.x_lab.laboratorio.DTO;

public class EmpresaMovilDTO {

    private String numeroIdentificacion;
    private int codigoEmpresa;
    private String direccionIpWsXHealth;
    private String nombreWarWsXHealth;

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getDireccionIpWsXHealth() {
        return "http://192.168.100.228:3000";
    }

    public void setDireccionIpWsXHealth(String direccionIpWsXHealth) {
        this.direccionIpWsXHealth = direccionIpWsXHealth;
    }

    public String getNombreWarWsXHealth() {
        return nombreWarWsXHealth;
    }

    public void setNombreWarWsXHealth(String nombreWarWsXHealth) {
        this.nombreWarWsXHealth = nombreWarWsXHealth;
    }
}
