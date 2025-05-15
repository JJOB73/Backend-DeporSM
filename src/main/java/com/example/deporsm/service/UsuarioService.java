package com.example.deporsm.service;

import com.example.deporsm.dto.ActualizarPerfilDTO;
import com.example.deporsm.dto.AdministradorDTO;
import com.example.deporsm.dto.CambioPasswordDTO;
import com.example.deporsm.dto.CoordinadorDTO;
import com.example.deporsm.dto.PerfilUsuarioDTO;
import com.example.deporsm.dto.PreferenciasNotificacionDTO;
import com.example.deporsm.dto.VecinoDTO;
import com.example.deporsm.model.PreferenciaNotificacion;
import com.example.deporsm.model.Rol;
import com.example.deporsm.model.Usuario;
import com.example.deporsm.repository.PreferenciaNotificacionRepository;
import com.example.deporsm.repository.UsuarioRepository;
import com.example.deporsm.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de usuarios en la aplicación
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PreferenciaNotificacionRepository preferenciaNotificacionRepository;

    @Autowired
    private VecinoRepository vecinoRepository;

    /**
     * Obtiene todos los usuarios del sistema
     * @return Lista de usuarios
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene todos los coordinadores
     * @return Lista de coordinadores con sus instalaciones asignadas
     */
    public List<CoordinadorDTO> listarCoordinadores() {
        return usuarioRepository.findAllCoordinadores();
    }

    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Usuario encontrado o empty si no existe
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene el perfil del usuario autenticado
     * @param authentication Autenticación del usuario
     * @return Usuario encontrado o empty si no existe
     */
    public Optional<Usuario> obtenerPerfilUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Actualiza el perfil del usuario
     * @param perfilDTO DTO con los datos a actualizar
     * @param authentication Autenticación del usuario
     * @return Usuario actualizado o empty si no existe
     */
    public Optional<Usuario> actualizarPerfil(PerfilUsuarioDTO perfilDTO, Authentication authentication) {
        Optional<Usuario> usuarioOpt = obtenerPerfilUsuario(authentication);
        
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (perfilDTO.getTelefono() != null) {
            usuario.setTelefono(perfilDTO.getTelefono());
        }
        
        if (perfilDTO.getDireccion() != null) {
            usuario.setDireccion(perfilDTO.getDireccion());
        }
        
        return Optional.of(usuarioRepository.save(usuario));
    }

    /**
     * Guarda un usuario en la base de datos
     * @param usuario Usuario a guardar
     * @return Usuario guardado
     */
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos del perfil de un usuario
     * @param email Email del usuario
     * @param perfilDTO Datos a actualizar
     * @return Usuario actualizado
     * @throws RuntimeException si no se encuentra el usuario
     */
    public Usuario actualizarPerfil(String email, ActualizarPerfilDTO perfilDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Actualizar sólo los campos permitidos
        usuario.setTelefono(perfilDTO.getTelefono());
        usuario.setDireccion(perfilDTO.getDireccion());
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Cambia la contraseña de un usuario
     * @param email Email del usuario
     * @param cambioPasswordDTO Datos de cambio de contraseña
     * @throws RuntimeException si la contraseña actual es incorrecta o no se encuentra el usuario
     */
    public void cambiarPassword(String email, CambioPasswordDTO cambioPasswordDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(cambioPasswordDTO.getPasswordActual(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        // Verificar que la nueva contraseña y su confirmación coincidan
        if (!cambioPasswordDTO.getPasswordNueva().equals(cambioPasswordDTO.getConfirmacionPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(cambioPasswordDTO.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }
    
    /**
     * Actualiza las preferencias de notificaciones de un usuario
     * @param email Email del usuario
     * @param preferenciasDTO Preferencias de notificaciones
     * @throws RuntimeException si no se encuentra el usuario
     */    
    public void actualizarPreferenciasNotificaciones(String email, PreferenciasNotificacionDTO preferenciasDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Buscar preferencias existentes o crear nuevas
        PreferenciaNotificacion preferencias = preferenciaNotificacionRepository
                .findByUsuarioId(usuario.getId())
                .orElse(new PreferenciaNotificacion());
        
        preferencias.setUsuario(usuario);
        preferencias.setEmail(preferenciasDTO.isEmail());
        preferencias.setReservas(preferenciasDTO.isReservas());
        preferencias.setPromociones(preferenciasDTO.isPromociones());
        preferencias.setMantenimiento(preferenciasDTO.isMantenimiento());
        
        preferenciaNotificacionRepository.save(preferencias);
    }

    /**
     * Obtiene todos los vecinos con su información básica y número de reservas
     */
    public List<VecinoDTO> listarVecinos() {
        return vecinoRepository.findAllVecinos();
    }

    /**
     * Busca vecinos por nombre, email o DNI
     */
    public List<VecinoDTO> buscarVecinos(String query) {
        return vecinoRepository.buscarVecinos(query);
    }

    /**
     * Registra un nuevo vecino
     * @throws RuntimeException si el email o DNI ya están registrados
     */
    public Usuario registrarVecino(Usuario usuario) {
        // Validar correo duplicado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
          // Asignar rol de vecino (role_id = 4)
        Rol rol = new Rol();
        rol.setId(4); // ID 4 corresponde al rol 'vecino' en la base de datos
        usuario.setRol(rol);
        
        // Activar por defecto
        usuario.setActivo(true);
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos de un vecino
     * @throws RuntimeException si el vecino no existe
     */
    public Usuario actualizarVecino(Long id, Usuario datosActualizados) {
        Usuario vecino = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vecino no encontrado"));
        
        // Actualizar solo campos permitidos
        vecino.setNombre(datosActualizados.getNombre());
        vecino.setApellidos(datosActualizados.getApellidos());
        vecino.setTelefono(datosActualizados.getTelefono());
        vecino.setDireccion(datosActualizados.getDireccion());
        
        return usuarioRepository.save(vecino);
    }

    /**
     * Desactiva un vecino
     * @throws RuntimeException si el vecino no existe
     */
    public void desactivarVecino(Long id) {
        Usuario vecino = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vecino no encontrado"));
        
        vecino.setActivo(false);
        usuarioRepository.save(vecino);
    }

    /**
     * Reactiva un vecino
     * @throws RuntimeException si el vecino no existe
     */
    public void reactivarVecino(Long id) {
        Usuario vecino = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vecino no encontrado"));
        
        vecino.setActivo(true);
        usuarioRepository.save(vecino);
    }

    /**
     * Obtiene todos los administradores
     * @return Lista de administradores
     */
    public List<AdministradorDTO> listarAdministradores() {
        System.out.println("[DEBUG] UsuarioService.listarAdministradores - Iniciando búsqueda");
        return usuarioRepository.findAllAdministradores();
    }
}