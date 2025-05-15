package com.example.deporsm.repository;

import com.example.deporsm.dto.AdministradorDTO;
import com.example.deporsm.dto.CoordinadorDTO;
import com.example.deporsm.dto.VecinoDTO;
import com.example.deporsm.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para encontrar un usuario por su correo
    Optional<Usuario> findByEmail(String correo);

    // Consulta para obtener coordinadores (admin)
    @Query(value = """
    SELECT 
        u.id AS id,
        CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
        u.email AS email,
        u.telefono AS telefono,
        GROUP_CONCAT(i.nombre ORDER BY i.nombre SEPARATOR ', ') AS instalacionesAsignadas
    FROM 
        deportes_sm.usuarios u
    LEFT JOIN 
        deportes_sm.coordinadores_instalaciones ci ON u.id = ci.usuario_id
    LEFT JOIN 
        deportes_sm.instalaciones i ON ci.instalacion_id = i.id
    WHERE 
        u.role_id = 3  -- Solo coordinadores
    GROUP BY 
        u.id, nombre, u.email, u.telefono
    """, nativeQuery = true)
    List<CoordinadorDTO> findAllCoordinadores();

    @Query(value = """
    SELECT 
        u.id,
        CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
        u.email,
        u.telefono,
        '' as instalacionesAsignadas
    FROM 
        deportes_sm.usuarios u
    WHERE 
        u.role_id = 2
    """, nativeQuery = true)
    List<AdministradorDTO> findAllAdministradores();

    @Query(value = """
    SELECT 
        CAST(u.id AS SIGNED) as id,
        CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
        u.email,
        u.telefono,
        CAST(COUNT(r.id) AS SIGNED) as reservas
    FROM
        deportes_sm.usuarios u     
    LEFT JOIN
        deportes_sm.reservas r 
            ON u.id = r.usuario_id     
    WHERE
        u.role_id = 4     
    GROUP BY
        u.id,
        u.nombre,
        u.apellidos,
        u.email,
        u.telefono 
    """, nativeQuery = true)
    List<VecinoDTO> findAllVecinos();
}
