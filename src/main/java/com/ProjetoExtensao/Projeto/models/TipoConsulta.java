package com.ProjetoExtensao.Projeto.models;

public enum TipoConsulta {
    ROTINA, EMERGENCIAL, ESPECIALIZADA;

    public static TipoConsulta getType(String type) {
        for (TipoConsulta consulta : TipoConsulta.values()) {
            if (consulta.toString().equalsIgnoreCase(type)) {
                return consulta;
            }
        }
        return null;
    }
}