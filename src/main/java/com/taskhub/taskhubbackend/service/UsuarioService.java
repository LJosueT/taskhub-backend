package com.taskhub.taskhubbackend.service;

import com.taskhub.taskhubbackend.dto.UsuarioDto;
import com.taskhub.taskhubbackend.entity.Usuario;
import com.taskhub.taskhubbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearUsuario(UsuarioDto usuarioDTO) {
        Usuario usuario = new Usuario();
        Date fechaActual = new Date();

        usuario.setUsuarioIngreso(usuarioDTO.getUsuarioIngreso());
        usuario.setContraseña(usuarioDTO.getContraseña());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNombres(usuarioDTO.getNombres());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setEstado("1");
        usuario.setFechaCreacion(fechaActual);
        return usuarioRepository.save(usuario);
    }
}

