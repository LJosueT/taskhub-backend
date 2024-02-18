package com.taskhub.taskhubbackend.repository;

import com.taskhub.taskhubbackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuarioIngreso(String usuarioIngreso);
}
