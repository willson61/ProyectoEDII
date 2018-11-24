package com.example.sthephan.proyectoedii;

import java.util.ArrayList;

public class UltimosMensajes {
    public ArrayList<Mensaje> jsonUsuario;
    public ArrayList<Mensaje> jsonUsuario2;
    public String token;

    public UltimosMensajes(ArrayList<Mensaje> jsonUsuario, ArrayList<Mensaje> jsonUsuario2, String token) {
        this.jsonUsuario = jsonUsuario;
        this.jsonUsuario2 = jsonUsuario2;
        this.token = token;
    }

    public ArrayList<Mensaje> getJsonUsuario() {
        return jsonUsuario;
    }

    public void setJsonUsuario(ArrayList<Mensaje> jsonUsuario) {
        this.jsonUsuario = jsonUsuario;
    }

    public ArrayList<Mensaje> getJsonUsuario2() {
        return jsonUsuario2;
    }

    public void setJsonUsuario2(ArrayList<Mensaje> jsonUsuario2) {
        this.jsonUsuario2 = jsonUsuario2;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
