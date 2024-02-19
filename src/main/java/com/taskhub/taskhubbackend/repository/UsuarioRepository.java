package com.taskhub.taskhubbackend.repository;

import com.taskhub.taskhubbackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.usuarioIngreso) = LOWER(:usuarioIngreso)")
    Optional<Usuario> findByUsuarioIngreso(String usuarioIngreso);
}
