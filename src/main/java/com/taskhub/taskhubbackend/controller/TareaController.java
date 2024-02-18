package com.taskhub.taskhubbackend.controller;

import com.taskhub.taskhubbackend.dto.FechaFiltroDTO;
import com.taskhub.taskhubbackend.dto.Response;
import com.taskhub.taskhubbackend.dto.TareaDto;
import com.taskhub.taskhubbackend.entity.Tarea;
import com.taskhub.taskhubbackend.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/taskhub/v1/tareas")
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    public ResponseEntity<?> registrarTarea(@RequestBody TareaDto tareasDto) {
        Tarea tarea = tareaService.registrarTarea(tareasDto);
        if(tarea != null){
            return ResponseEntity.ok(new Response("Tarea registrada de manera exitosa", tarea));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de Usuario no encontrado");
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarTareasPorRangoDeFechas(@RequestBody FechaFiltroDTO fechaFiltroDTO) {

        Date fechaInicio = fechaFiltroDTO.getFechaInicio();
        Date fechaFin = fechaFiltroDTO.getFechaFin();

        List<Tarea> tareasFiltradas = tareaService.filtrarTareasPorRangoDeFechas(fechaInicio, fechaFin);

        if (tareasFiltradas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tareas en el rango de fechas proporcionado.");
        } else {
            return ResponseEntity.ok(new Response("Se encontraron las siguientes tareas", tareasFiltradas));
        }
    }
}