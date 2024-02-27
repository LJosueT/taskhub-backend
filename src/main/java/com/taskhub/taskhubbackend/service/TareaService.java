package com.taskhub.taskhubbackend.service;

import com.taskhub.taskhubbackend.dto.TareaDto;
import com.taskhub.taskhubbackend.entity.Tarea;
import com.taskhub.taskhubbackend.entity.Usuario;
import com.taskhub.taskhubbackend.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public TareaService(TareaRepository tareaRepository, UsuarioService usuarioService) {
        this.tareaRepository = tareaRepository;
        this.usuarioService = usuarioService;
    }

    public boolean verificarIdUsuarioTarea(TareaDto tareaDTO){
        Integer id = tareaDTO.getId_usuario();
        Usuario usuario = usuarioService.buscarIdUsuario(id);
        if (usuario != null) {
            return true;
        } else {
            return false;
        }
    }

    public Tarea registrarTarea(TareaDto TareaDTO) {
        Tarea tarea = new Tarea();
        if(verificarIdUsuarioTarea(TareaDTO)){
            tarea.setNombre(TareaDTO.getNombre());
            tarea.setDescripcion(TareaDTO.getDescripcion());
            tarea.setFechaFin(TareaDTO.getFecha_fin());
            tarea.setPrioridad(TareaDTO.getPrioridad());
            tarea.setEstado(TareaDTO.getEstado());
            tarea.setIdUsuario(TareaDTO.getId_usuario());
            return tareaRepository.save(tarea);
        }
        return null;
    }
    //Leoncio
    public Tarea buscarIdTarea(Integer idTarea) {
        return tareaRepository.findById(idTarea).orElse(null);
    }
    //Leoncio
    public Tarea editarTarea(Integer idTarea, TareaDto TareaDTO) {
        Tarea tareaExistente = buscarIdTarea(idTarea);

        if (tareaExistente != null) {
            return tareaRepository.save(tareaExistente);
        }

        return null;
    }
    //Leoncio
    public void eliminarTarea(Integer idTarea) {
        tareaRepository.deleteById(idTarea);
    }

    public List<Tarea> filtrarTareasPorRangoDeFechas(Integer usuarioId, Date fechaInicio, Date fechaFin) {
        return tareaRepository.findByUsuarioIdAndFechaFinBetween(usuarioId, fechaInicio, fechaFin);
    }

}