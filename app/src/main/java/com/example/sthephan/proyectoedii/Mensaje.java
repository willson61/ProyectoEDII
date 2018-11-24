package com.example.sthephan.proyectoedii;

public class Mensaje {
    public String remitente;
    public String receptor;
    public String mensaje;
    public String tipo;
    public String fecha;


    public Mensaje(String remitente, String receptor, String mensaje, String tipo, String fecha){
        this.remitente = remitente;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente){
        this.remitente = remitente;
    }

    public String getReceptor(){
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
