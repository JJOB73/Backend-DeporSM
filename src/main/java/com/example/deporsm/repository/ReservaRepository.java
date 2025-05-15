package com.example.deporsm.repository;

import com.example.deporsm.dto.DashboardStatsDTO;
import com.example.deporsm.dto.ReservaDetalleDTO;
import com.example.deporsm.dto.ReservaRecienteDTO;
import com.example.deporsm.model.Reserva;
import com.example.deporsm.dto.ReservaListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {    // Método para listar reservas para el Admin
    @Query("SELECT new com.example.deporsm.dto.ReservaListDTO(" +
            "r.id, " +
            "u.nombre, " +
            "i.nombre, " +
            "i.ubicacion, " +
            "r.metodoPago, " +
            "i.imagenUrl, " +
            "r.fecha, " +
            "r.horaInicio, " +
            "r.horaFin, " +
            "r.estado, " +
            "r.estadoPago" +
            ") " +
            "FROM Reserva r " +
            "JOIN r.usuario u " +
            "JOIN r.instalacion i")
    List<ReservaListDTO> listarReservasParaAdmin();    // Método para buscar reservas por el DNI del usuario
    @Query("SELECT r FROM Reserva r JOIN r.usuario u WHERE u.dni = :dni")
    List<Reserva> findByUsuario_Dni(@Param("dni") String dni);
    
    @Query(value = """
    SELECT 
      COALESCE(COUNT(*), 0) AS totalReservas,
      COALESCE(SUM(CASE WHEN estado IN ('pendiente', 'confirmada') THEN 1 ELSE 0 END), 0) AS reservasActivas,
      (SELECT COALESCE(COUNT(*), 0) FROM instalaciones WHERE activo = true) AS totalInstalaciones,
      (SELECT COALESCE(COUNT(*), 0) FROM observaciones) AS totalObservaciones
    FROM reservas
    """, nativeQuery = true)
    DashboardStatsDTO getDashboardStats(); // ← preferido si estás usando una proyección basada en constructor
    
    @Query(value = """
    SELECT 
        r.id AS idReserva,
        u.nombre AS nombreUsuario,
        i.nombre AS nombreInstalacion,
        r.fecha AS fecha,
        r.hora_inicio AS horaInicio,
        r.hora_fin AS horaFin,
        r.estado AS estado,
        r.estado_pago AS estadoPago
    FROM reservas r
    JOIN usuarios u ON r.usuario_id = u.id
    JOIN instalaciones i ON r.instalacion_id = i.id
    ORDER BY r.fecha DESC, r.hora_inicio DESC
    LIMIT 5
    """, nativeQuery = true)
    List<ReservaRecienteDTO> obtenerReservasRecientes();
    
    // Consulta para obtener los detalles de una reserva específica
    @Query("SELECT new com.example.deporsm.dto.ReservaDetalleDTO(" +
            "r.id, " +
            "u.id, " +
            "u.nombre, " +
            "i.id, " +
            "i.nombre, " +
            "i.ubicacion, " +
            "i.imagenUrl, " +
            "r.fecha, " +
            "r.horaInicio, " +
            "r.horaFin, " +
            "r.estado, " +
            "r.estadoPago, " +
            "r.metodoPago, " +
            "r.comentarios, " +
            "r.createdAt, " +
            "r.updatedAt) " +
            "FROM Reserva r " +
            "JOIN r.usuario u " +
            "JOIN r.instalacion i " +
        "WHERE r.id = :id")
    Optional<ReservaDetalleDTO> obtenerDetalleReserva(@Param("id") Integer id);



}
