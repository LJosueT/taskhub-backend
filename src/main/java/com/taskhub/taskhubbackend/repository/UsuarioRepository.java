package com.taskhub.taskhubbackend.repository;

import com.taskhub.taskhubbackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}
