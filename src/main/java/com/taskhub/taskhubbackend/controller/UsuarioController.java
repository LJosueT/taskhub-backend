package com.taskhub.taskhubbackend.controller;

import com.taskhub.taskhubbackend.dto.Response;
import com.taskhub.taskhubbackend.dto.UsuarioDto;
import com.taskhub.taskhubbackend.entity.Usuario;
import com.taskhub.taskhubbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

        if (usuario.getIdUsuario() == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario ya existente");
        } else{
            return ResponseEntity.ok(new Response("Usuario registrado de manera exitosa", usuario));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarUsuario(@PathVariable int id, @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioService.editarUsuario(id, usuarioDto);
        
        if (usuario.getIdUsuario() == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario ya existente");
        } else if (usuario.getIdUsuario() == -2) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de Usuario no encontrado");
        } 
        else {
            return ResponseEntity.ok(new Response("Usuario actualizado exitosamente", usuario));
        }
    }    
}

