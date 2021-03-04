package com.jorjaimalex.mityoal.Cards;

public class tarjetas {
    private String uId;
    private String name;
    private String imageUrl;
    private String descripcion;

    public tarjetas(String uId, String name, String imageUrl, String descripcion) {
        this.uId = uId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
    }

    public String getuId() {
        return uId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}