package com.example.deporsm.controller;

import com.example.deporsm.model.Reserva;
import com.example.deporsm.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

   // @GetMapping("/usuario/{dni}")
   // public List<Reserva> obtenerReservasPorUsuario(@PathVariable String dni) {
       // return reservaRepository.findByDniUsuario(dni);
   // }
}
