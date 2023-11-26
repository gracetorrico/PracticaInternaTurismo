package com.example.practicainterna;

public class Coordenadas {
    private String Nombre;
    private double Longitud, Latitud;

    public Coordenadas(String nombre, double longitud, double latitud){
        Nombre = nombre;
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

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }
}
