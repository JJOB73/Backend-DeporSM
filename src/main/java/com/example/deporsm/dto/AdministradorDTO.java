package com.example.deporsm.dto;

public class AdministradorDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private String instalacionesAsignadas; // Texto separado por comas, si es necesario

    // Constructor
    public AdministradorDTO(Integer id, String nombre, String email, String telefono, String instalacionesAsignadas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.instalacionesAsignadas = instalacionesAsignadas;
    }

    // Getters
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getInstalacionesAsignadas() { return instalacionesAsignadas; }
}
