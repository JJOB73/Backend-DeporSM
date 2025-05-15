package com.example.deporsm.controller;

import com.example.deporsm.model.Pago;
import com.example.deporsm.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:3000")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<?> obtenerPagoPorReserva(@PathVariable Integer reservaId) {
        return pagoRepository.findByReservaId(reservaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
