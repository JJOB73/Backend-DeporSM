package com.example.deporsm.repository;

import com.example.deporsm.model.InstalacionDeportiva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstalacionRepository extends JpaRepository<InstalacionDeportiva, Integer> {
}
