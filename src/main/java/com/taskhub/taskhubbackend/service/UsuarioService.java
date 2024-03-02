package com.taskhub.taskhubbackend.service;

import com.taskhub.taskhubbackend.dto.UsuarioDto;
import com.taskhub.taskhubbackend.entity.Usuario;
import com.taskhub.taskhubbackend.repository.UsuarioRepository;
import com.taskhub.taskhubbackend.security.CustomerDetailsService;
import com.taskhub.taskhubbackend.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

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
            if(!usuario.getUsuarioIngreso().equals(usuarioDTO.getUsuarioIngreso())){
                if(!validarNombreUsuario(usuarioDTO)){
                    return new Usuario(-1, null, null, null, null, null, null, null);
                }
            }
            usuario.setUsuarioIngreso(usuarioDTO.getUsuarioIngreso());
                usuario.setContraseña(usuarioDTO.getContraseña());
                usuario.setCorreo(usuarioDTO.getCorreo());
                usuario.setNombres(usuarioDTO.getNombres());
                usuario.setApellidos(usuarioDTO.getApellidos());
                // Guardar el usuario actualizado en la base de datos
                return usuarioRepository.save(usuario); 
        }
        return new Usuario(-2, null, null, null, null, null, null, null);
    }

    public ResponseEntity<String> login(Map<String, String> requestMap){
//        log.info("Dentro del login");
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("correo"), requestMap.get("contraseña"))
            );

            if(authentication.isAuthenticated()){
                if(customerDetailsService.getUserDetail().getEstado().equals("1")){
                    return new ResponseEntity<String>("{\"token\": \"" +
                            jwtUtil.generateToken(customerDetailsService.getUserDetail().getCorreo()) + "\")}",
                            HttpStatus.OK);
                }
            }else{
                return new ResponseEntity<String>("{\"mensaje\": \"" + "Algo salió mal" + "\")}", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception exception){
            log.error("{}", exception);
        }
        return new ResponseEntity<String>("{\"mensaje\":\""+"Credenciales incorrectas"+"\"}", HttpStatus.BAD_REQUEST);
    }
}

