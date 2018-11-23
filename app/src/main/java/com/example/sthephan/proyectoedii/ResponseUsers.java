package com.example.sthephan.proyectoedii;

import java.util.ArrayList;

public class ResponseUsers {
    public String status;
    public ArrayList<Usuario> usuarios;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
