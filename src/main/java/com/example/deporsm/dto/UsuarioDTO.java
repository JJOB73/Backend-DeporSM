package com.example.deporsm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private String dni;
    private String estado;
    private Integer reservas;

    // Constructor
    public UsuarioDTO(Integer id, String nombre, String email, String telefono, String dni, String estado, Integer reservas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
        this.estado = estado;
        this.reservas = reservas;
    }
}
