package com.example.deporsm.repository;

import com.example.deporsm.dto.VecinoDTO;
import com.example.deporsm.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VecinoRepository extends JpaRepository<Usuario, Long> {

    // Consulta para obtener vecinos (role_id = 2)
    @Query(value = """
        SELECT 
            u.id AS id,
            CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
            u.email AS email,
            u.telefono AS telefono,
            COUNT(r.id) AS reservas  -- Número de reservas realizadas por el vecino
        FROM 
            deportes_sm.usuarios u
        LEFT JOIN 
            deportes_sm.reservas r ON u.id = r.usuario_id  -- Contar reservas
        WHERE 
            u.role_id = 2  -- Solo vecinos
        GROUP BY 
            u.id, nombre, u.email, u.telefono
    """, nativeQuery = true)
    List<VecinoDTO> findAllVecinos();
}
