package com.taskhub.taskhubbackend.dto;

import com.taskhub.taskhubbackend.entity.Usuario;
import lombok.Data;

@Data
public class Response {

    private String mensaje;
    private Usuario usuario;

    public Response(String mensaje, Usuario usuario) {
        this.mensaje = mensaje;
        this.usuario = usuario;
    }
}
