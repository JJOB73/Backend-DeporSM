package com.example.deporsm.dto;

public class CoordinadorDTOSuperadmin {
    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private Integer cantidadInstalaciones;  // Total de instalaciones asignadas (para superadmin)

    // Constructor
    public CoordinadorDTOSuperadmin(Integer id, String nombre, String email, String telefono, Integer cantidadInstalaciones) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.cantidadInstalaciones = cantidadInstalaciones;
    }

    // Getters
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public Integer getCantidadInstalaciones() { return cantidadInstalaciones; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCantidadInstalaciones(Integer cantidadInstalaciones) { this.cantidadInstalaciones = cantidadInstalaciones; }
}
