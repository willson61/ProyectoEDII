package com.example.sthephan.proyectoedii;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Mensaje {
    public String remitente;
    public String receptor;
    public String mensaje;
    public String tipo;
    public String fecha;
    public String type;



    public Mensaje(String remitente, String receptor, String mensaje, String tipo, Date fecha) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        this.remitente = remitente;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fecha = date.format(fecha);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Date getFecha() throws ParseException {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        return date.parse(fecha);
    }

    public void setFecha(Date fecha) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        this.fecha = date.format(fecha);
    }
}

class DateComparator  implements Comparator<Mensaje> {

    @Override
    public int compare(Mensaje obj1, Mensaje obj2) {
        Date date1 = null;
        Date date2 = null;
        try{
            date1 = obj1.getFecha();
            date2 = obj2.getFecha();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (date1.equals(date2)) {
            return 0;
        }
        if (String.valueOf(date1).equals("")) {
            return -1;
        }
        if (String.valueOf(date2).equals("")) {
            return 1;
        }
        return date1.compareTo(date2);
    }
}
