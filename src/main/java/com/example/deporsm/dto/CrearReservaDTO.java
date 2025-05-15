package com.example.deporsm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaDTO {
    private Integer instalacionId;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private Integer numeroAsistentes;
    private String comentarios;
    private String estado;          // Estado de la reserva (pendiente, confirmada, etc)
    private String estadoPago;      // Estado del pago (pendiente, pagado, etc)
    private String metodoPago;      // Método de pago (online, deposito)
}
