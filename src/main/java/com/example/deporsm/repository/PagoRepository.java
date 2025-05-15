package com.example.deporsm.repository;

import com.example.deporsm.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
    @Query("SELECT p FROM Pago p WHERE p.reservaId = :reservaId")
    Optional<Pago> findByReservaId(@Param("reservaId") Integer reservaId);
}
