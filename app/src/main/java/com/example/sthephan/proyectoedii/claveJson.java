package com.example.sthephan.proyectoedii;

public class claveJson {
    public String clave;
    public String token;

    public claveJson(String clave, String token) {
        this.clave = clave;
        this.token = token;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
