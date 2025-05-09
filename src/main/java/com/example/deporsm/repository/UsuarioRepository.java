package com.example.deporsm.repository;

import com.example.deporsm.dto.AdministradorDTO;
import com.example.deporsm.dto.UsuarioDTO;
import com.example.deporsm.dto.CoordinadorDTO;
import com.example.deporsm.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String correo);

    // Consulta para obtener los Coordinadores
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
            u.role_id = 3  -- Rol de Coordinador
        GROUP BY 
            u.id, nombre, u.email, u.telefono
        """, nativeQuery = true)
    List<CoordinadorDTO> findAllCoordinadores();

    // Consulta modificada para obtener los Vecinos
    @Query(value = """
        SELECT 
            u.id AS id,
            CONCAT(u.nombre, ' ', u.apellidos) AS nombre,
            u.email AS email,
            u.telefono AS telefono,  
            u.dni AS dni,
            CASE             
                WHEN u.activo = 1 THEN 'Activo'             
                ELSE 'Inactivo'         
            END AS estado,
            COUNT(r.id) AS reservas
        FROM 
            usuarios u
        LEFT JOIN 
            reservas r 
                ON u.id = r.usuario_id
        WHERE 
            u.role_id = 4  -- Solo los "vecinos"
        GROUP BY 
            u.id, nombre, u.email, u.telefono, u.dni, u.activo
    """, nativeQuery = true)
    List<UsuarioDTO> findAllVecinos();

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
            u.role_id = 2  -- Rol de Administrador
        GROUP BY 
            u.id, nombre, u.email, u.telefono
        """, nativeQuery = true)
    List<AdministradorDTO> findAllAdmins();


}
