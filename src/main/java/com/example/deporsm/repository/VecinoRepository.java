package com.example.deporsm.repository;

import com.example.deporsm.dto.VecinoDTO;
import com.example.deporsm.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VecinoRepository extends JpaRepository<Usuario, Long> {

    @Query(value = """
        SELECT 
            u.id,
            CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
            u.email,
            u.telefono,
            COUNT(r.id) AS reservas
        FROM 
            deportes_sm.usuarios u
        LEFT JOIN 
            deportes_sm.reservas r ON u.id = r.usuario_id
        WHERE 
            u.role_id = 4
        GROUP BY 
            u.id, u.nombre, u.apellidos, u.email, u.telefono
    """, nativeQuery = true)
    List<VecinoDTO> findAllVecinos();

    @Query(value = """
        SELECT 
            u.id,
            CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
            u.email,
            u.telefono,
            COUNT(r.id) AS reservas
        FROM 
            deportes_sm.usuarios u
        LEFT JOIN 
            deportes_sm.reservas r ON u.id = r.usuario_id
        WHERE 
            u.role_id = 4 AND
            (LOWER(CONCAT(u.nombre, ' ', u.apellidos)) LIKE LOWER(CONCAT('%', :query, '%')) OR 
             LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR 
             u.dni LIKE CONCAT('%', :query, '%'))
        GROUP BY 
            u.id, u.nombre, u.apellidos, u.email, u.telefono
    """, nativeQuery = true)
    List<VecinoDTO> buscarVecinos(@Param("query") String query);

    @Query(value = """
        SELECT 
            u.id,
            CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
            u.email,
            u.telefono,
            COUNT(r.id) AS reservas
        FROM 
            deportes_sm.usuarios u
        LEFT JOIN 
            deportes_sm.reservas r ON u.id = r.usuario_id
        WHERE
            u.role_id = 4 AND
            u.activo = :activo
        GROUP BY
            u.id, u.nombre, u.apellidos, u.email, u.telefono
    """, nativeQuery = true)
    List<VecinoDTO> findByActivo(@Param("activo") boolean activo);
}
