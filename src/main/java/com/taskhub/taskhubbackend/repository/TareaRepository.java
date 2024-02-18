package com.taskhub.taskhubbackend.repository;

import com.taskhub.taskhubbackend.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByFechaFinBetween(Date fechaInicio, Date fechaFin);
}