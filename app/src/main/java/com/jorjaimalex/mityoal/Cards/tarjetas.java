package com.jorjaimalex.mityoal.Cards;

public class tarjetas {
    private String uId;
    private String name;
    private String imageUrl;
    private String descripcion;
    private String profesion;

    public tarjetas(String uId, String name, String imageUrl, String descripcion, String profesion) {
        this.uId = uId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.profesion = profesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getuId() {
        return uId;
    }

    public String getName() {
        return name;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}