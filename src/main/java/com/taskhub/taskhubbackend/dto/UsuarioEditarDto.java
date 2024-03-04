package com.taskhub.taskhubbackend.dto;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioEditarDto {

    private String usuarioIngreso;
    private Optional<String> contraseña;
    private String nombres;
    private String apellidos;

    public UsuarioEditarDto() {
        this.contraseña = Optional.empty();
    }

}