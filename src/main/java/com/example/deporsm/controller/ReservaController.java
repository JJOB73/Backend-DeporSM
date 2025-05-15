package com.example.deporsm.controller;

import com.example.deporsm.dto.CrearReservaDTO;
import com.example.deporsm.dto.DashboardStatsDTO;
import com.example.deporsm.dto.ReservaListDTO;
import com.example.deporsm.dto.ReservaRecienteDTO;
import com.example.deporsm.model.Reserva;
import com.example.deporsm.repository.ReservaRepository;
import com.example.deporsm.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private ReservaService reservaService;    @GetMapping("/usuario/{dni}")
    public List<ReservaListDTO> obtenerReservasPorUsuario(@PathVariable String dni) {
        List<Reserva> reservas = reservaRepository.findByUsuario_Dni(dni);

        return reservas.stream()
                .map(reserva -> {
                    ReservaListDTO dto = new ReservaListDTO(
                            reserva.getId(),
                            reserva.getUsuario().getNombre(),
                            reserva.getInstalacion().getNombre(),
                            reserva.getInstalacion().getUbicacion(), // Incluir ubicación
                            reserva.getMetodoPago(),                // Incluir método de pago
                            reserva.getInstalacion().getImagenUrl(), // Incluir URL de imagen
                            reserva.getFecha(),
                            reserva.getHoraInicio(),
                            reserva.getHoraFin(),
                            reserva.getEstado(),
                            reserva.getEstadoPago()
                    );
                    return dto;
                })
                .collect(Collectors.toList()); // Usamos collect para compatibilidad con Java 8+
    }

    @GetMapping("/stats")
    public DashboardStatsDTO obtenerEstadisticasDashboard() {
        return reservaRepository.getDashboardStats();
    }


    /**
     * Crea una nueva reserva para el usuario autenticado
     */
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody CrearReservaDTO reservaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        
        String email = authentication.getName();
        try {
            Reserva reservaCreada = reservaService.crearReserva(email, reservaDTO);
            return ResponseEntity.ok(reservaCreada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear reserva: " + e.getMessage());
        }
    }
    
    /**
     * Cancela una reserva
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Integer id, @RequestParam(required = false) String motivo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        
        String email = authentication.getName();
        try {
            reservaService.cancelarReserva(id, email, motivo);
            return ResponseEntity.ok("Reserva cancelada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cancelar reserva: " + e.getMessage());
        }
    }
      /**
     * Obtiene el historial de reservas del usuario autenticado
     */
    @GetMapping("/historial")
    public ResponseEntity<?> historialReservas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        
        String email = authentication.getName();
        try {
            return ResponseEntity.ok(reservaService.obtenerHistorialReservas(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener historial: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene los detalles de una reserva específica por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleReserva(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
          try {
            return reservaRepository.obtenerDetalleReserva(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener detalle de reserva: " + e.getMessage());
        }
    }
}
