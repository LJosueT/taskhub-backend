package com.taskhub.taskhubbackend.controller;

import com.taskhub.taskhubbackend.dto.Response;
import com.taskhub.taskhubbackend.dto.UsuarioDto;
import com.taskhub.taskhubbackend.entity.Usuario;
import com.taskhub.taskhubbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taskhub/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> agregarUsuario(@RequestBody UsuarioDto usuarioDTO) {
        Usuario usuario = usuarioService.crearUsuario(usuarioDTO);

        Response response = new Response("Usuario registrado de manera exitosa", usuario);

        return ResponseEntity.ok(response);
    }
}

