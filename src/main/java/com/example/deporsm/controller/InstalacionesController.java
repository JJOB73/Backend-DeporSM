package com.example.deporsm.controller;

import com.example.deporsm.model.InstalacionDeportiva;
import com.example.deporsm.repository.InstalacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instalaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class InstalacionesController {

    private final InstalacionRepository repository;

    public InstalacionesController(InstalacionRepository repository) {
        this.repository = repository;
    }

    // Listar todas
    @GetMapping
    public List<InstalacionDeportiva> listar() {
        return repository.findAll();
    }

    // Crear nueva instalación
    @PostMapping
    public InstalacionDeportiva crear(@RequestBody InstalacionDeportiva nuevaInstalacion) {
        return repository.save(nuevaInstalacion);
    }

    // Actualizar una instalación existente
    @PutMapping("/{id}")
    public ResponseEntity<InstalacionDeportiva> actualizar(@PathVariable int id, @RequestBody InstalacionDeportiva actualizada) {
        Optional<InstalacionDeportiva> existente = repository.findById(id);

        if (existente.isPresent()) {
            InstalacionDeportiva inst = existente.get();
            inst.setNombre(actualizada.getNombre());
            inst.setDescripcion(actualizada.getDescripcion());
            inst.setFotoUrl(actualizada.getFotoUrl());
            return ResponseEntity.ok(repository.save(inst));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // (Opcional) Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
