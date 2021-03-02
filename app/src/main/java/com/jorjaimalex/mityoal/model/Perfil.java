package com.jorjaimalex.mityoal.model;

public class Perfil {
    private String nombreU;
    private String urlFoto;

    public Perfil() {}

    public Perfil(String nombre, String urlFoto) {
        this.nombreU = nombre;
        this.urlFoto = urlFoto;
    }

    public String getNombre() {
        return nombreU;
    }

    public String getUrlFoto() {
        return urlFoto;
    }
}
