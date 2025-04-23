package com.example.deporsm.controller;

import com.example.deporsm.model.LoginRequest;
import com.example.deporsm.model.Usuario;
import com.example.deporsm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    //@PostMapping("/login")
    //public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    //System.out.println("Intentando login con: " + request.getCorreo());

    //Usuario user = usuarioRepo.findByCorreo(request.getCorreo());

    //if (user == null || !user.getPasswordHash().equals(request.getPassword())) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    //}

    // return ResponseEntity.ok(user);
//    }

}
