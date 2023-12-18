package com.example.practicainterna;

public class Coordenadas {
    private String Nombre, Tipo;
    private double Longitud, Latitud;
    private int ID;

    public Coordenadas(int id, String nombre, double longitud, double latitud, String tipo){
        ID = id;
        Nombre = nombre;
        Tipo = tipo;
        Longitud = longitud;
        Latitud = latitud;
    }

    public String getNombre() {
        return Nombre;
    }

    public double getLongitud() {
        return Longitud;
    }

    public double getLatitud() {
        return Latitud;
    }

    public String getTipo(){ return Tipo; }

    public int getID() { return ID; }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }
    public void setTipo(String tipo) { Tipo = tipo; }
}
