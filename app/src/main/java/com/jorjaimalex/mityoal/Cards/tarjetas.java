package com.jorjaimalex.mityoal.Cards;

public class tarjetas {
    private String uId;
    private String name;
    private String imageUrl;
    public tarjetas(String uId, String name, String imageUrl){
        this.uId = uId;
        this.name = name;
        this.imageUrl = imageUrl;

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
