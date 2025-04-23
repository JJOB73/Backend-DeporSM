// Archivo: Observacion.java
package com.example.deporsm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Observaciones")
public class Observacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descripcion;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @ManyToOne
    @JoinColumn(name = "idInstalacion")
    private InstalacionDeportiva instalacion;

    @ManyToOne
    @JoinColumn(name = "idCoordinador", referencedColumnName = "DNI")
    private Usuario coordinador;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "fecha_observacion")
    private LocalDate fechaObservacion;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    public enum Prioridad {
        baja, media, alta
    }

    public enum Estado {
        pendiente, aprobada, completada
    }
}
