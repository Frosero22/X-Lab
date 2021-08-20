package com.neu360.x_lab.laboratorio.DTO;

public class DetalleOrdenDTO {

    private int codigoEmpresa;
    private int numeroOrden;
    private int lineaDetalleOrden;
    private int codigoPrestacion;
    private int codigoServicio;
    private String nombrePrestacion;
    private String nombreServicio;

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

    public int getLineaDetalleOrden() {
        return lineaDetalleOrden;
    }

    public void setLineaDetalleOrden(int lineaDetalleOrden) {
        this.lineaDetalleOrden = lineaDetalleOrden;
    }

    public int getCodigoPrestacion() {
        return codigoPrestacion;
    }

    public void setCodigoPrestacion(int codigoPrestacion) {
        this.codigoPrestacion = codigoPrestacion;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public String getNombrePrestacion() {
        return nombrePrestacion;
    }

    public void setNombrePrestacion(String nombrePrestacion) {
        this.nombrePrestacion = nombrePrestacion;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
}
