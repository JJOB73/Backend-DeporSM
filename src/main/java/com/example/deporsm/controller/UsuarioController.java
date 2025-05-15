package com.example.deporsm.controller;

import com.example.deporsm.dto.ActualizarPerfilDTO;
import com.example.deporsm.dto.AdministradorDTO;
import com.example.deporsm.dto.CambioPasswordDTO;
import com.example.deporsm.dto.CoordinadorDTO;
import com.example.deporsm.dto.PerfilUsuarioDTO;
import com.example.deporsm.dto.PreferenciasNotificacionDTO;
import com.example.deporsm.dto.VecinoDTO;
import com.example.deporsm.model.Rol;
import com.example.deporsm.model.Usuario;
import com.example.deporsm.repository.UsuarioRepository;
import com.example.deporsm.repository.VecinoRepository;
import com.example.deporsm.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private VecinoRepository vecinoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @GetMapping("/allCoordinadores")
    public List<CoordinadorDTO> listarCoordinadores() {
        return usuarioService.listarCoordinadores();
    }
    
    @GetMapping("/perfil")
    public ResponseEntity<Usuario> getPerfil() {
        System.out.println("[DEBUG] Iniciando getPerfil");
        try {
            // Obtener autenticación directamente del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() 
                || authentication.getPrincipal().equals("anonymousUser")) {
                System.out.println("[DEBUG] getPerfil - Usuario no autenticado o anónimo");
                return ResponseEntity.status(401).build();
            }
            
            String email = authentication.getName();
            System.out.println("[DEBUG] getPerfil - Email encontrado: " + email);
            
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
            
            if (usuario.isPresent()) {
                System.out.println("[DEBUG] getPerfil - Usuario encontrado: " + usuario.get().getEmail() 
                    + ", rol: " + usuario.get().getRol().getNombre());
                return ResponseEntity.ok(usuario.get());
            } else {
                System.out.println("[DEBUG] getPerfil - No se encontró usuario con email: " + email);
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Error en getPerfil: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @PutMapping("/perfil")
    public ResponseEntity<Usuario> actualizarPerfil(@RequestBody PerfilUsuarioDTO perfilDTO) {
        System.out.println("[DEBUG] Iniciando actualizarPerfil");
        try {
            // Obtener autenticación directamente del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() 
                || authentication.getPrincipal().equals("anonymousUser")) {
                System.out.println("[DEBUG] actualizarPerfil - Usuario no autenticado o anónimo");
                return ResponseEntity.status(401).build();
            }
            
            String email = authentication.getName();
            System.out.println("[DEBUG] actualizarPerfil - Email encontrado: " + email);
            
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            
            if (usuarioOpt.isEmpty()) {
                System.out.println("[DEBUG] actualizarPerfil - No se encontró usuario con email: " + email);
                return ResponseEntity.status(401).build();
            }
            
            Usuario usuario = usuarioOpt.get();
            
            if (perfilDTO.getTelefono() != null) {
                usuario.setTelefono(perfilDTO.getTelefono());
            }
            
            if (perfilDTO.getDireccion() != null) {
                usuario.setDireccion(perfilDTO.getDireccion());
            }
            
            usuario = usuarioRepository.save(usuario);
            
            System.out.println("[DEBUG] actualizarPerfil - Usuario actualizado: " + usuario.getEmail());
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            System.out.println("[DEBUG] Error en actualizarPerfil: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/actualizar-perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestBody ActualizarPerfilDTO perfilDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        
        String email = authentication.getName();
        try {
            usuarioService.actualizarPerfil(email, perfilDTO);
            return ResponseEntity.ok().body("Perfil actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el perfil: " + e.getMessage());
        }
    }
    
    @PutMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody CambioPasswordDTO cambioPasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        
        String email = authentication.getName();
        try {
            if (!cambioPasswordDTO.getPasswordNueva().equals(cambioPasswordDTO.getConfirmacionPassword())) {
                return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
            }
            
            usuarioService.cambiarPassword(email, cambioPasswordDTO);
            return ResponseEntity.ok().body("Contraseña actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cambiar la contraseña: " + e.getMessage());
        }
    }
    
    @PutMapping("/preferencias-notificaciones")
    public ResponseEntity<?> actualizarPreferenciasNotificaciones(
            @RequestBody PreferenciasNotificacionDTO preferenciasDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        
        String email = authentication.getName();
        try {
            usuarioService.actualizarPreferenciasNotificaciones(email, preferenciasDTO);
            return ResponseEntity.ok().body("Preferencias de notificaciones actualizadas correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error al actualizar las preferencias de notificaciones: " + e.getMessage());
        }    }
    
    @GetMapping("/allVecinos")
    public ResponseEntity<List<VecinoDTO>> listarVecinos() {
        System.out.println("[DEBUG] Iniciando listarVecinos");
        try {
            System.out.println("[DEBUG] Intentando obtener vecinos del servicio");
            List<VecinoDTO> vecinos = usuarioService.listarVecinos();
            
            System.out.println("[DEBUG] ¿Lista de vecinos es nula? " + (vecinos == null));
            
            if (vecinos == null || vecinos.isEmpty()) {
                System.out.println("[DEBUG] listarVecinos - No se encontraron vecinos");
                return ResponseEntity.noContent().build();
            }
            
            System.out.println("[DEBUG] listarVecinos - Vecinos encontrados: " + vecinos.size());
            return ResponseEntity.ok(vecinos);
        } catch (Exception e) {
            System.out.println("[DEBUG] Error en listarVecinos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarVecino(@RequestBody Usuario usuario) {
        try {
            // Validar correo duplicado
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("El correo ya está registrado");
            }
              // Asignar rol de vecino (role_id = 4)
            Rol rol = new Rol();
            rol.setId(4); // ID del rol vecino
            usuario.setRol(rol);
            
            // Activar usuario por defecto
            usuario.setActivo(true);
            
            Usuario nuevoVecino = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(nuevoVecino);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar vecino: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVecino(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return usuarioRepository.findById(id)
                .map(vecino -> {
                    // Solo actualizar campos permitidos
                    vecino.setNombre(usuario.getNombre());
                    vecino.setApellidos(usuario.getApellidos());
                    vecino.setTelefono(usuario.getTelefono());
                    vecino.setDireccion(usuario.getDireccion());
                    
                    // No actualizar email ni DNI para evitar duplicados
                    Usuario vecinoActualizado = usuarioRepository.save(vecino);
                    return ResponseEntity.ok(vecinoActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar vecino: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarVecino(@PathVariable Long id) {
        try {
            return usuarioRepository.findById(id)
                .map(vecino -> {
                    vecino.setActivo(false);
                    usuarioRepository.save(vecino);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al desactivar vecino: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<?> reactivarVecino(@PathVariable Long id) {
        try {
            return usuarioRepository.findById(id)
                .map(vecino -> {
                    vecino.setActivo(true);
                    usuarioRepository.save(vecino);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al reactivar vecino: " + e.getMessage());
        }
    }

    @GetMapping("/allAdministradores")
    public ResponseEntity<List<AdministradorDTO>> listarAdministradores() {
        System.out.println("[DEBUG] Iniciando listarAdministradores");
        try {
            List<AdministradorDTO> admins = usuarioService.listarAdministradores();
            
            if (admins == null || admins.isEmpty()) {
                System.out.println("[DEBUG] listarAdministradores - No se encontraron administradores");
                return ResponseEntity.noContent().build();
            }
            
            System.out.println("[DEBUG] listarAdministradores - Administradores encontrados: " + admins.size());
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            System.out.println("[DEBUG] Error en listarAdministradores: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/administradores")
    public ResponseEntity<?> crearAdministrador(@RequestBody Usuario usuario) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para crear administradores");
            }

            // Validar correo duplicado
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("El correo ya está registrado");
            }            // Asignar rol de administrador (role_id = 2)
            Rol rol = new Rol();
            rol.setId(2); // ID del rol administrador
            usuario.setRol(rol);
            
            // Activar usuario por defecto
            usuario.setActivo(true);
            
            // Encriptar contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            
            Usuario nuevoAdmin = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(nuevoAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear administrador: " + e.getMessage());
        }
    }

    @PutMapping("/administradores/{id}/desactivar")
    public ResponseEntity<?> desactivarAdministrador(@PathVariable Long id) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para desactivar administradores");
            }

            return usuarioRepository.findById(id)
                .map(admin -> {
                    if (admin.getRol().getId() != 2) {
                        return ResponseEntity.badRequest().body("El usuario no es un administrador");
                    }
                    admin.setActivo(false);
                    usuarioRepository.save(admin);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al desactivar administrador: " + e.getMessage());
        }
    }

    @PutMapping("/administradores/{id}/activar")
    public ResponseEntity<?> activarAdministrador(@PathVariable Long id) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para activar administradores");
            }

            return usuarioRepository.findById(id)
                .map(admin -> {
                    if (admin.getRol().getId() != 2) {
                        return ResponseEntity.badRequest().body("El usuario no es un administrador");
                    }
                    admin.setActivo(true);
                    usuarioRepository.save(admin);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al activar administrador: " + e.getMessage());
        }
    }

    @PutMapping("/administradores/{id}")
    public ResponseEntity<?> actualizarAdministrador(@PathVariable Long id, @RequestBody Usuario datosActualizados) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para actualizar administradores");
            }

            return usuarioRepository.findById(id)
                .map(admin -> {
                    if (admin.getRol().getId() != 2) {
                        return ResponseEntity.badRequest().body("El usuario no es un administrador");
                    }
                    
                    // Actualizar campos permitidos
                    admin.setNombre(datosActualizados.getNombre());
                    admin.setApellidos(datosActualizados.getApellidos());
                    admin.setTelefono(datosActualizados.getTelefono());
                    admin.setDireccion(datosActualizados.getDireccion());
                    
                    // Actualizar contraseña si se proporcionó una nueva
                    if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
                        admin.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
                    }
                    
                    Usuario adminActualizado = usuarioRepository.save(admin);
                    return ResponseEntity.ok(adminActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar administrador: " + e.getMessage());
        }
    }
    
    @GetMapping("/administradores/{id}")
    public ResponseEntity<?> obtenerAdministrador(@PathVariable Long id) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para ver administradores");
            }

            return usuarioRepository.findById(id)
                .map(admin -> {
                    if (admin.getRol().getId() != 2) {
                        return ResponseEntity.badRequest().body("El usuario no es un administrador");
                    }
                    return ResponseEntity.ok(admin);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener administrador: " + e.getMessage());
        }
    }
    
    @PostMapping("/coordinadores")
    public ResponseEntity<?> crearCoordinador(@RequestBody Usuario usuario) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para crear coordinadores");
            }

            // Validar correo duplicado
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("El correo ya está registrado");
            }

            // Asignar rol de coordinador (role_id = 3)
            Rol rol = new Rol();
            rol.setId(3); // ID del rol coordinador
            usuario.setRol(rol);
            
            // Activar usuario por defecto
            usuario.setActivo(true);
            
            // Encriptar contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            
            Usuario nuevoCoord = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(nuevoCoord);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear coordinador: " + e.getMessage());
        }
    }

    @PutMapping("/coordinadores/{id}")
    public ResponseEntity<?> actualizarCoordinador(@PathVariable Long id, @RequestBody Usuario datosActualizados) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para actualizar coordinadores");
            }

            return usuarioRepository.findById(id)
                .map(coord -> {
                    if (coord.getRol().getId() != 3) {
                        return ResponseEntity.badRequest().body("El usuario no es un coordinador");
                    }
                    
                    // Actualizar campos permitidos
                    coord.setNombre(datosActualizados.getNombre());
                    coord.setApellidos(datosActualizados.getApellidos());
                    coord.setTelefono(datosActualizados.getTelefono());
                    coord.setDireccion(datosActualizados.getDireccion());
                    
                    // Actualizar contraseña si se proporcionó una nueva
                    if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
                        coord.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
                    }
                    
                    Usuario coordinadorActualizado = usuarioRepository.save(coord);
                    return ResponseEntity.ok(coordinadorActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar coordinador: " + e.getMessage());
        }
    }

    @GetMapping("/coordinadores/{id}")
    public ResponseEntity<?> obtenerCoordinador(@PathVariable Long id) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para ver los detalles de coordinadores");
            }

            return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (usuario.getRol().getId() != 3) {
                        return ResponseEntity.badRequest().body("El usuario no es un coordinador");
                    }
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener coordinador: " + e.getMessage());
        }
    }

    @PutMapping("/coordinadores/{id}/desactivar")
    public ResponseEntity<?> desactivarCoordinador(@PathVariable Long id) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para desactivar coordinadores");
            }

            return usuarioRepository.findById(id)
                .map(coordinador -> {
                    if (coordinador.getRol().getId() != 3) {
                        return ResponseEntity.badRequest().body("El usuario no es un coordinador");
                    }
                    coordinador.setActivo(false);
                    usuarioRepository.save(coordinador);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al desactivar coordinador: " + e.getMessage());
        }
    }

    @PutMapping("/coordinadores/{id}/activar")
    public ResponseEntity<?> activarCoordinador(@PathVariable Long id) {
        try {
            // Verificar si el usuario actual es superadmin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                return ResponseEntity.status(403).body("No tienes permisos para activar coordinadores");
            }

            return usuarioRepository.findById(id)
                .map(coordinador -> {
                    if (coordinador.getRol().getId() != 3) {
                        return ResponseEntity.badRequest().body("El usuario no es un coordinador");
                    }
                    coordinador.setActivo(true);
                    usuarioRepository.save(coordinador);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al activar coordinador: " + e.getMessage());
        }
    }
}

