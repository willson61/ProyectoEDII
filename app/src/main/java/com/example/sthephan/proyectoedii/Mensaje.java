package com.example.sthephan.proyectoedii;

import java.util.Comparator;
import java.util.Date;

public class Mensaje {
    public String remitente;
    public String receptor;
    public String mensaje;
    public String tipo;
    public Date fecha;
    public String token;
    public String secreto;

    public Mensaje(String remitente, String receptor, String mensaje, String tipo, Date fecha, String token, String secreto) {
        this.remitente = remitente;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fecha = fecha;
        this.token = token;
        this.secreto = secreto;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getReceptor() {
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

    public Date getFecha() {
        return fecha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecreto(){
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

class DateComparator  implements Comparator<Mensaje> {

    @Override
    public int compare(Mensaje obj1, Mensaje obj2) {
        if (obj1.getFecha().equals(obj2.getFecha())) {
            return 0;
        }
        if (String.valueOf(obj1.getFecha()).equals("")) {
            return -1;
        }
        if (String.valueOf(obj2.getFecha()).equals("")) {
            return 1;
        }
        return obj1.getFecha().compareTo(obj2.getFecha());
    }
}
