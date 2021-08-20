package com.neu360.x_lab.laboratorio.DTO;

public class OrdenesDTO {

    private int codigoEmpresa;
    private int numeroOrden;
    private int idPaciente;
    private int codigoTipoIdentificacion;
    private String numeroIdentificacionCliente;
    private String nombreCliente;
    private String genero;

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getCodigoTipoIdentificacion() {
        return codigoTipoIdentificacion;
    }

    public void setCodigoTipoIdentificacion(int codigoTipoIdentificacion) {
        this.codigoTipoIdentificacion = codigoTipoIdentificacion;
    }

    public String getNumeroIdentificacionCliente() {
        return numeroIdentificacionCliente;
    }

    public void setNumeroIdentificacionCliente(String numeroIdentificacionCliente) {
        this.numeroIdentificacionCliente = numeroIdentificacionCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
