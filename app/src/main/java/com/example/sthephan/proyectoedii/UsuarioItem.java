package com.example.sthephan.proyectoedii;

public class UsuarioItem {
    public String nombreUsuarioEmisor;
    public String nombreUsuarioReceptor;
    public String ultimoMensaje;
    public String clave;

    @Override
    public String toString() {
        return "UsuarioItem{" +
                "nombreUsuarioEmisor='" + nombreUsuarioEmisor + '\'' +
                ", nombreUsuarioReceptor='" + nombreUsuarioReceptor + '\'' +
                ", ultimoMensaje='" + ultimoMensaje + '\'' +
                ", clave='" + clave + '\'' +
                '}';
    }

    public UsuarioItem(String nombreUsuarioEmisor, String nombreUsuarioReceptor, String ultimoMensaje, String clave) {
        this.nombreUsuarioEmisor = nombreUsuarioEmisor;
        this.nombreUsuarioReceptor = nombreUsuarioReceptor;
        this.ultimoMensaje = ultimoMensaje;
        this.clave = clave;
    }

    public String getNombreUsuarioReceptor() {
        return nombreUsuarioReceptor;
    }

    public void setNombreUsuarioReceptor(String nombreUsuarioReceptor) {
        this.nombreUsuarioReceptor = nombreUsuarioReceptor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreUsuarioEmisor() {
        return nombreUsuarioEmisor;
    }

    public void setNombreUsuarioEmisor(String nombreUsuarioEmisor) {
        this.nombreUsuarioEmisor = nombreUsuarioEmisor;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
}
