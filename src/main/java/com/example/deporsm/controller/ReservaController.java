package com.example.deporsm.controller;

import com.example.deporsm.dto.DashboardStatsDTO;
import com.example.deporsm.dto.ReservaListDTO;
import com.example.deporsm.dto.ReservaRecienteDTO;
import com.example.deporsm.model.Reserva;
import com.example.deporsm.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/usuario/{dni}")
    public List<ReservaListDTO> obtenerReservasPorUsuario(@PathVariable String dni) {
        List<Reserva> reservas = reservaRepository.findByUsuario_Dni(dni);

        return reservas.stream()
                .map(reserva -> {
                    ReservaListDTO dto = new ReservaListDTO(
                            reserva.getId(),
                            reserva.getUsuario().getNombre(),
                            reserva.getInstalacion().getNombre(),
                            reserva.getFecha(),
                            reserva.getHoraInicio(),
                            reserva.getHoraFin(),
                            reserva.getEstado(),
                            reserva.getEstadoPago() // Asignar estadoPago
                    );
                    return dto;
                })
                .collect(Collectors.toList()); // Usamos collect para compatibilidad con Java 8+
    }

    @GetMapping("/stats")
    public DashboardStatsDTO obtenerEstadisticasDashboard() {
        return reservaRepository.getDashboardStats();
    }




}
