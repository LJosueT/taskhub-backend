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

    public boolean validarNombreUsuario(UsuarioDto usuarioDTO) {
        String nombre_usuario = usuarioDTO.getUsuarioIngreso();
        Usuario usuario = usuarioRepository.findByUsuarioIngreso(nombre_usuario).orElse(null);
        return (usuario == null);
    }

    public Usuario crearUsuario(UsuarioDto usuarioDTO) {
        Usuario usuario = new Usuario();
        Date fechaActual = new Date();
        if(validarNombreUsuario(usuarioDTO)){
            usuario.setUsuarioIngreso(usuarioDTO.getUsuarioIngreso());
            usuario.setContraseña(usuarioDTO.getContraseña());
            usuario.setCorreo(usuarioDTO.getCorreo());
            usuario.setNombres(usuarioDTO.getNombres());
            usuario.setApellidos(usuarioDTO.getApellidos());
            usuario.setEstado("1");
            usuario.setFechaCreacion(fechaActual);
            return usuarioRepository.save(usuario);
        }
        return new Usuario(-1, null, null, null, null, null, null, null);
    }

    public Usuario buscarIdUsuario(Integer usuario_id) {
        return usuarioRepository.findById(usuario_id).orElse(null);
    }

    public Usuario editarUsuario(Integer usuario_id, UsuarioDto usuarioDTO) {
        Usuario usuario = buscarIdUsuario(usuario_id);
        if (usuario != null) {
            if(validarNombreUsuario(usuarioDTO)){
                usuario.setUsuarioIngreso(usuarioDTO.getUsuarioIngreso());
                usuario.setContraseña(usuarioDTO.getContraseña());
                usuario.setCorreo(usuarioDTO.getCorreo());
                usuario.setNombres(usuarioDTO.getNombres());
                usuario.setApellidos(usuarioDTO.getApellidos());
                // Guardar el usuario actualizado en la base de datos
                return usuarioRepository.save(usuario);
            }
            return new Usuario(-1, null, null, null, null, null, null, null);
        }
        return new Usuario(-2, null, null, null, null, null, null, null);
    }
}

