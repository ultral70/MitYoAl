package com.jorjaimalex.mityoal.model;

public class Persona {
    private String nombre;
    private String email;

    public Persona() {}

    public Persona(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
