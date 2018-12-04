package com.example.sthephan.proyectoedii;

import java.util.ArrayList;

public class ResponseChat {
    public ArrayList<Mensaje> docs2;
    public void setdocs2(ArrayList<Mensaje> docs2) {
        this.docs2 = docs2;
    }
    public ArrayList<Mensaje> getdocs2() {
        return docs2;
    }
    public String token;
    public ArrayList<Mensaje> docs;
    public ArrayList<Mensaje> getdocs() {
        return docs;
    }
    public void setdocs(ArrayList<Mensaje> docs) {
        this.docs = docs;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    /*class jsonUsuario2{
        public ArrayList<Mensaje> docs2;
        public void setdocs2(ArrayList<Mensaje> docs2) {
            this.docs2 = docs2;
        }
        public ArrayList<Mensaje> getdocs2() {
            return docs2;
        }
    }

    class jsonUsuario{
        public ArrayList<Mensaje> docs;
        public ArrayList<Mensaje> getdocs() {
            return docs;
        }
        public void setdocs(ArrayList<Mensaje> docs) {
            this.docs = docs;
        }
    }*/
}

