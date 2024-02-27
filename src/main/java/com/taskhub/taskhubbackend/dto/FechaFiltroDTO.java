package com.taskhub.taskhubbackend.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FechaFiltroDTO {
    private Integer usuarioId;
    private Date fechaInicio;
    private Date fechaFin;
}