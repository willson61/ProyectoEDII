package com.example.sthephan.proyectoedii;

import java.util.ArrayList;

public class ResponseUsers {
    public String token;
    public ArrayList<Usuario> docs;

    public String getStatus() {
        return token;
    }

    public void setStatus(String status) {
        this.token = status;
    }

    public ArrayList<Usuario> getUsuarios() {
        return docs;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.docs = usuarios;
    }
}
