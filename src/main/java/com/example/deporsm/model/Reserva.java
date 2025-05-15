package com.example.deporsm.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;    @ManyToOne
    @JoinColumn(name = "instalacion_id")
    @JsonIgnore
    private Instalacion instalacion;

    @Column(name="fecha")
    private Date fecha;

    @Column(name = "hora_inicio")
    private Time horaInicio;

    @Column(name = "hora_fin")
    private Time horaFin;    @Column(name="estado")
    private String estado;
    
    // Estos campos no existen en la base de datos pero los mantenemos en el modelo
    // Como transientes para no afectar el código existente
    @Transient
    private String motivo;

    @Transient
    private Integer numeroAsistentes;

    @Column(name="comentarios")
    private String comentarios;    @Column(name = "estado_pago") // Corregido a estado_pago en la base de datos
    private String estadoPago;
    
    @Column(name = "metodo_pago") // Nuevo campo para el método de pago
    private String metodoPago;    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }
        if (this.updatedAt == null) {
            this.updatedAt = new Timestamp(System.currentTimeMillis());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
