package com.example.deporsm.repository;

import com.example.deporsm.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuario_Id(Integer idUsuario);
}
