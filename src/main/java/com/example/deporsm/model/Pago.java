package com.example.deporsm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reserva_id")
    private Integer reservaId;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "metodo")
    private String metodo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "referencia_transaccion")
    private String referenciaTransaccion;

    @Column(name = "url_comprobante")
    private String urlComprobante;

    @Column(name = "ultimos_digitos")
    private String ultimosDigitos;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
