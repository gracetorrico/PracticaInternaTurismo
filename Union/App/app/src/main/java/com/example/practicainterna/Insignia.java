package com.example.practicainterna;

public class Insignia {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean usuarioTieneInsignia;
    private int cantidadInsignias;

    public Insignia(int id, String nombre, String descripcion, boolean usuarioTieneInsignia, int cantidadInsignias) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuarioTieneInsignia = usuarioTieneInsignia;
        this.cantidadInsignias = cantidadInsignias;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isUsuarioTieneInsignia() {
        return usuarioTieneInsignia;
    }

    public int getCantidadInsignias() {
        return cantidadInsignias;
    }
}
