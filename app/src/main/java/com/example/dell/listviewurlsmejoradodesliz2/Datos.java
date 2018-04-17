package com.example.dell.listviewurlsmejoradodesliz2;

/**
 * Created by sergioromeroburdalo on 20/3/18.
 */

public class Datos {

    private int id;
    private String nombre;
    private String URL;


    public Datos(int id, String nombre, String URL) {
        this.nombre = nombre;
        this.URL = URL;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
