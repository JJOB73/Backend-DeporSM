package com.example.deporsm.repository;

import com.example.deporsm.model.Reserva;
import com.example.deporsm.dto.ReservaListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Método para listar reservas para el Admin
    @Query("SELECT new com.example.deporsm.dto.ReservaListDTO(" +
            "r.id, " +
            "u.nombre, " +
            "i.nombre, " +
            "r.fecha, " +
            "r.horaInicio, " +
            "r.horaFin, " +
            "r.estado, " +
            "r.estadoPago" + // Agregado estadoPago
            ") " +
            "FROM Reserva r " +
            "JOIN r.usuario u " +
            "JOIN r.instalacion i")
    List<ReservaListDTO> listarReservasParaAdmin();

    // Método para buscar reservas por el DNI del usuario
    @Query("SELECT r FROM Reserva r JOIN r.usuario u WHERE u.dni = :dni")
    List<Reserva> findByUsuario_Dni(@Param("dni") String dni);
}
