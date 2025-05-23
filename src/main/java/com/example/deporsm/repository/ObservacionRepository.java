// src/main/java/com/example/deporsm/repository/ObservacionRepository.java
package com.example.deporsm.repository;

import com.example.deporsm.dto.ObservacionRecienteDTO;
import com.example.deporsm.model.Observacion;
import com.example.deporsm.dto.ObservacionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservacionRepository extends JpaRepository<Observacion, Integer> {

    @Query(value = """
        SELECT
            o.id as idObservacion,
            i.nombre AS instalacion,
            o.titulo AS descripcion,
            CONCAT(u.nombre, ' ', u.apellidos) AS coordinador,
            DATE_FORMAT(o.created_at, '%d/%m/%Y') AS fecha,
            o.estado AS estado,
            o.prioridad AS prioridad
        FROM 
            deportes_sm.observaciones o
        INNER JOIN 
            deportes_sm.instalaciones i ON o.instalacion_id = i.id
        INNER JOIN 
            deportes_sm.usuarios u ON o.usuario_id = u.id
        ORDER BY 
            o.created_at DESC
        """, nativeQuery = true)
    List<Object[]> findAllObservacionesRaw();

    default List<ObservacionDTO> findAllObservacionesDTO() {
        return findAllObservacionesRaw().stream()
                .map(row -> new ObservacionDTO(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        (String) row[5],
                        (String) row[6]
                ))
                .toList();
    }

    @Query(value = """
        SELECT 
            o.id AS idObservacion,
            i.nombre AS nombreInstalacion,
            o.descripcion,
            o.prioridad,
            DATE(o.created_at) AS fecha
        FROM observaciones o
        JOIN instalaciones i ON o.instalacion_id = i.id
        ORDER BY o.created_at DESC
        LIMIT 4
    """, nativeQuery = true)
    List<ObservacionRecienteDTO> findObservacionesRecientes();

}
