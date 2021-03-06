package com.jorjaimalex.mityoal.contactos;

public class Contacto {

    private String userId;
    private String name;
    private String profileImageUrl;
    public Contacto (String userId, String name, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }

}
