package com.example.sthephan.proyectoedii;

public class UsuarioItem {
    public String nombreUsuario;
    public String ultimoMensaje;

    public UsuarioItem(String nombreUsuario, String ultimoMensaje) {
        this.nombreUsuario = nombreUsuario;
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
}
