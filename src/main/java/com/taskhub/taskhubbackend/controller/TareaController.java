package com.taskhub.taskhubbackend.controller;

import com.taskhub.taskhubbackend.dto.FechaFiltroDTO;
import com.taskhub.taskhubbackend.dto.Response;
import com.taskhub.taskhubbackend.dto.TareaDto;
import com.taskhub.taskhubbackend.entity.Tarea;
import com.taskhub.taskhubbackend.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            return ResponseEntity.ok(new Response("Tarea registrada con éxito", tarea));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de Usuario no encontrado");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editarTarea(@PathVariable Integer id, @RequestBody TareaDto tareaDto) {
        Tarea tarea = tareaService.editarTarea(id, tareaDto);
        if (tarea != null) {
            return ResponseEntity.ok(new Response("Tarea actualizada con éxito", tarea));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada con el id proporcionado");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Integer id) {
        Tarea tareaExistente = tareaService.buscarIdTarea(id);
        if (tareaExistente != null) {
            tareaService.eliminarTarea(id);
            return ResponseEntity.ok().body("Tarea eliminada con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada con el id proporcionado");
        }
    }
    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> mostrarTareas(@PathVariable Integer usuarioId) {
        List<Tarea> ListaTareas = tareaService.mostrarTareas(usuarioId);
        if (ListaTareas == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron tareas");
        } else {
            Tarea primeraTarea = ListaTareas.get(0);
            if (primeraTarea.getIdTarea() == -1){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de Usuario no encontrado");
            }
            return ResponseEntity.ok(new Response("Se encontraron las siguientes tareas", ListaTareas));
        }
    }

    @PostMapping("/filtrar/{usuarioId}")
    public ResponseEntity<?> filtrarTareasPorRangoDeFechas(@PathVariable Integer usuarioId, @RequestBody FechaFiltroDTO fechaFiltroDTO) {

        Date fechaInicio = fechaFiltroDTO.getFechaInicio();
        Date fechaFin = fechaFiltroDTO.getFechaFin();

        List<Tarea> tareasFiltradas = tareaService.filtrarTareasPorRangoDeFechas(usuarioId, fechaInicio, fechaFin);

        if (tareasFiltradas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron tareas en el rango de fechas proporcionado.");
        } else {
            return ResponseEntity.ok(new Response("Se encontraron las siguientes tareas", tareasFiltradas));
        }
    }
}