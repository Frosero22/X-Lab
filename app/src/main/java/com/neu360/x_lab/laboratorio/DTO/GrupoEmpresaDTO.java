package com.neu360.x_lab.laboratorio.DTO;

import java.util.ArrayList;
import java.util.List;

public class GrupoEmpresaDTO {

    private int codigoGrupoEmpresa;
    private String nombreGrupoEmpresa;

    private List<EmpresasDTO> lsEmpresas = new ArrayList<>();



    public int getCodigoGrupoEmpresa() {
        return codigoGrupoEmpresa;
    }

    public void setCodigoGrupoEmpresa(int codigoGrupoEmpresa) {
        this.codigoGrupoEmpresa = codigoGrupoEmpresa;
    }

    public String getNombreGrupoEmpresa() {
        return nombreGrupoEmpresa;
    }

    public void setNombreGrupoEmpresa(String nombreGrupoEmpresa) {
        this.nombreGrupoEmpresa = nombreGrupoEmpresa;
    }

    public List<EmpresasDTO> getLsEmpresas() {
        return lsEmpresas;
    }

    public void setLsEmpresas(List<EmpresasDTO> lsEmpresas) {
        this.lsEmpresas = lsEmpresas;
    }

}
