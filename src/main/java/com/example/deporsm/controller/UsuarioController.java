package com.example.deporsm.controller;
import com.example.deporsm.dto.UsuarioDTO;
import com.example.deporsm.dto.CoordinadorDTO;
import com.example.deporsm.dto.AdministradorDTO;

import com.example.deporsm.model.Usuario;
import com.example.deporsm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/allCoordinadores")
    public List<CoordinadorDTO> listarCoordinadores() {
        return usuarioRepository.findAllCoordinadores();
    }

    @GetMapping("/allVecinos")
    public List<UsuarioDTO> listarVecinos() { return usuarioRepository.findAllVecinos();
    }

    @GetMapping("/allAdmins")
    public List<AdministradorDTO> listarAdmins() {
        return usuarioRepository.findAllAdmins();
    }
}

