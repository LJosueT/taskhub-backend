package com.taskhub.taskhubbackend.service;

import com.taskhub.taskhubbackend.dto.LoginResponse;
import com.taskhub.taskhubbackend.dto.UsuarioDetalleDto;
import com.taskhub.taskhubbackend.dto.UsuarioDto;
import com.taskhub.taskhubbackend.dto.UsuarioEditarDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private boolean validarNombreUsuario(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByUsuarioIngreso(nombreUsuario).orElse(null);
        return usuario == null;
    }

    public boolean validarNombreUsuario(UsuarioDto usuarioDTO) {
        return validarNombreUsuario(usuarioDTO.getUsuarioIngreso());
    }

    public boolean validarNombreUsuario(UsuarioEditarDto usuarioEditarDTO) {
        return validarNombreUsuario(usuarioEditarDTO.getUsuarioIngreso());
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

    public Usuario editarUsuario(Integer usuario_id, UsuarioEditarDto usuarioEditarDTO) {
        Usuario usuario = buscarIdUsuario(usuario_id);
        if (usuario != null) {
            if(!usuario.getUsuarioIngreso().equals(usuarioEditarDTO.getUsuarioIngreso())){
                if(!validarNombreUsuario(usuarioEditarDTO)){
                    return new Usuario(-1, null, null, null, null, null, null, null);
                }
            }
            usuario.setUsuarioIngreso(usuarioEditarDTO.getUsuarioIngreso());
            usuarioEditarDTO.getContraseña().ifPresent(usuario::setContraseña);
            usuario.setNombres(usuarioEditarDTO.getNombres());
            usuario.setApellidos(usuarioEditarDTO.getApellidos());
            // Guardar el usuario actualizado en la base de datos
            return usuarioRepository.save(usuario);
        }
        return new Usuario(-2, null, null, null, null, null, null, null);
    }

    public ResponseEntity<LoginResponse> login(Map<String, String> requestMap) {
        try {
            String correo = requestMap.get("correo");
            String contraseña = requestMap.get("contraseña");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, contraseña)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario userDetail = usuarioRepository.findByCorreo(correo).orElseThrow(
                    () -> new UsernameNotFoundException("Usuario no encontrado")
            );

            if (userDetail.getEstado().equals("1")) {
                String token = jwtUtil.generateToken(userDetail.getCorreo());

                UsuarioDetalleDto usuarioDetalleDto = new UsuarioDetalleDto();
                usuarioDetalleDto.setIdUsuario(userDetail.getIdUsuario());
                usuarioDetalleDto.setUsuarioIngreso(userDetail.getUsuarioIngreso());
                usuarioDetalleDto.setCorreo(userDetail.getCorreo());
                usuarioDetalleDto.setNombres(userDetail.getNombres());
                usuarioDetalleDto.setApellidos(userDetail.getApellidos());

                LoginResponse loginResponse = new LoginResponse(token, usuarioDetalleDto);
                return ResponseEntity.ok(loginResponse);
            } else {
                return ResponseEntity.badRequest().body(new LoginResponse("El usuario no está activo", null));
            }
        } catch (Exception exception) {
            log.error("Error en el login: {}", exception.getMessage());
            return ResponseEntity.badRequest().body(new LoginResponse("Credenciales incorrectas", null));
        }
    }
}

