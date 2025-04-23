package com.example.deporsm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "instalaciones_deportivas")
public class InstalacionDeportiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private String descripcion;
    private Double lat;
    private Double lon;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Integer capacidad;
    private Double precio;

    @Column(name = "foto_url")
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "dni_coordinador", referencedColumnName = "dni")
    private Usuario coordinador;

    public enum Tipo {
        voley, futbol, gimnasio, piscina, atletismo
    }

    public enum Estado {
        disponible, mantenimiento, bloqueado
    }
}
