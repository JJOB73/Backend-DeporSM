package com.example.deporsm.service;

import com.example.deporsm.dto.CrearReservaDTO;
import com.example.deporsm.dto.ReservaListDTO;
import com.example.deporsm.model.Instalacion;
import com.example.deporsm.model.Reserva;
import com.example.deporsm.model.Usuario;
import com.example.deporsm.repository.InstalacionRepository;
import com.example.deporsm.repository.ReservaRepository;
import com.example.deporsm.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private InstalacionRepository instalacionRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Crea una nueva reserva para un usuario
     * @param email Email del usuario
     * @param reservaDTO Datos de la reserva
     * @return La reserva creada
     * @throws RuntimeException si no se encuentra el usuario o la instalación,
     *         o si la instalación no está disponible
     */
    public Reserva crearReserva(String email, CrearReservaDTO reservaDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Instalacion instalacion = instalacionRepository.findById(reservaDTO.getInstalacionId())
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada"));
        
        if (!instalacion.getActivo()) {
            throw new RuntimeException("La instalación no está disponible para reservas");
        }
        
        // Verificar disponibilidad de horario (implementación simplificada)
        boolean horarioDisponible = verificarDisponibilidadHorario(
                instalacion.getId(), 
                reservaDTO.getFecha(),
                reservaDTO.getHoraInicio(),
                reservaDTO.getHoraFin());
        
        if (!horarioDisponible) {
            throw new RuntimeException("El horario seleccionado no está disponible");
        }        // Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setInstalacion(instalacion);
        reserva.setFecha(reservaDTO.getFecha());
        reserva.setHoraInicio(reservaDTO.getHoraInicio());
        reserva.setHoraFin(reservaDTO.getHoraFin());
        
        // Manejar el campo numeroAsistentes de forma segura
        try {
            if (reservaDTO.getNumeroAsistentes() != null) {
                reserva.setNumeroAsistentes(reservaDTO.getNumeroAsistentes());
            }
        } catch (Exception e) {
            // Si el campo no existe en la entidad, solo log y continuar
            System.out.println("Advertencia: No se pudo establecer el número de asistentes: " + e.getMessage());
        }
        
        reserva.setComentarios(reservaDTO.getComentarios());
        
        // Usar el estado proporcionado o establecer el predeterminado
        if (reservaDTO.getEstado() != null && !reservaDTO.getEstado().isEmpty()) {
            reserva.setEstado(reservaDTO.getEstado()); // Estado personalizado desde el frontend
        } else {
            reserva.setEstado("pendiente"); // La reserva comienza en estado pendiente por defecto
        }
        
        // Usar el estado de pago proporcionado o establecerlo como pendiente
        if (reservaDTO.getEstadoPago() != null && !reservaDTO.getEstadoPago().isEmpty()) {
            reserva.setEstadoPago(reservaDTO.getEstadoPago()); // Estado de pago personalizado
        } else {
            reserva.setEstadoPago("pendiente"); // El pago comienza como pendiente por defecto
        }
          // Guardar método de pago si viene proporcionado
        if (reservaDTO.getMetodoPago() != null && !reservaDTO.getMetodoPago().isEmpty()) {
            // Asumiendo que has agregado este getter y setter a la entidad Reserva
            reserva.setMetodoPago(reservaDTO.getMetodoPago());
        }
        
        // Asegurar que las marcas de tiempo se establezcan correctamente
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        if (reserva.getCreatedAt() == null) {
            reserva.setCreatedAt(currentTimestamp);
        }
        if (reserva.getUpdatedAt() == null) {
            reserva.setUpdatedAt(currentTimestamp);
        }
        
        // Guardar y devolver
        return reservaRepository.save(reserva);
    }
    
    /**
     * Cancela una reserva de un usuario
     * @param reservaId ID de la reserva
     * @param email Email del usuario
     * @param motivo Motivo de la cancelación
     * @throws RuntimeException si no se encuentra la reserva o si no pertenece al usuario
     */
    public void cancelarReserva(Integer reservaId, String email, String motivo) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que la reserva existe
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        // Verificar que la reserva pertenece al usuario
        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("La reserva no pertenece a este usuario");
        }
        
        // Verificar que la reserva no esté ya cancelada
        if ("cancelada".equals(reserva.getEstado())) {
            throw new RuntimeException("La reserva ya está cancelada");
        }
          // Actualizar el estado de la reserva
        reserva.setEstado("cancelada");
        // Solo setear el motivo si el campo existe en la entidad
        try {
            if (motivo != null && !motivo.isEmpty()) {
                reserva.setMotivo(motivo);
            }
        } catch (Exception e) {
            // Si el campo no existe o hay otro problema, lo manejamos silenciosamente
            // y continuamos con la cancelación
            System.out.println("Advertencia: No se pudo establecer el motivo de cancelación: " + e.getMessage());
        }
        
        // Guardar la reserva actualizada
        reservaRepository.save(reserva);
    }
    /**
     * Obtiene el historial de reservas de un usuario
     * @param email Email del usuario
     * @return Lista de reservas del usuario
     */
    public List<ReservaListDTO> obtenerHistorialReservas(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
          // Usamos JPQL con proyección para evitar los campos problemáticos e incluir ubicación, método de pago e imagen
        String jpql = "SELECT new com.example.deporsm.dto.ReservaListDTO(" +
                "r.id, " +
                "u.nombre, " +
                "i.nombre, " +
                "i.ubicacion, " +
                "r.metodoPago, " +
                "i.imagenUrl, " +
                "r.fecha, " +
                "r.horaInicio, " +
                "r.horaFin, " +
                "r.estado, " +
                "r.estadoPago) " +
                "FROM Reserva r " +
                "JOIN r.usuario u " +
                "JOIN r.instalacion i " +
                "WHERE u.dni = :dni";
                
        return entityManager.createQuery(jpql, ReservaListDTO.class)
                .setParameter("dni", usuario.getDni())
                .getResultList();
    }
    
    /**
     * Verifica si un horario está disponible para una instalación
     * @param instalacionId ID de la instalación
     * @param fecha Fecha de la reserva
     * @param horaInicio Hora de inicio
     * @param horaFin Hora de fin
     * @return true si el horario está disponible, false en caso contrario
     */
    private boolean verificarDisponibilidadHorario(Integer instalacionId, java.sql.Date fecha, 
                                                java.sql.Time horaInicio, java.sql.Time horaFin) {
        // Lógica simplificada - en un caso real, verificaríamos en la BD las reservas existentes
        // que se solapen con el horario solicitado
        
        // Para esta demostración, suponemos que está disponible
        return true;
    }
      /**
     * Actualiza el estado de pago de una reserva
     * @param reservaId ID de la reserva
     * @param email Email del usuario
     * @param estadoPago Nuevo estado de pago
     * @param estado Estado opcional de la reserva
     * @return La reserva actualizada
     * @throws RuntimeException si no se encuentra la reserva o si no pertenece al usuario
     */
    public Reserva actualizarEstadoPago(Integer reservaId, String email, String estadoPago, String estado) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que la reserva existe
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        // Verificar que la reserva pertenece al usuario
        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("La reserva no pertenece a este usuario");
        }
        
        // Actualizar el estado de pago
        reserva.setEstadoPago(estadoPago);
        
        // Si se proporciona un estado explícito, usarlo
        if (estado != null && !estado.isEmpty()) {
            reserva.setEstado(estado);
        } 
        // Si no, y el pago está confirmado, también actualizar el estado de la reserva a confirmado
        else if ("pagado".equals(estadoPago)) {
            reserva.setEstado("confirmada");
        }
        
        // Guardar la reserva actualizada
        return reservaRepository.save(reserva);
    }
}
