package com.taskhub.taskhubbackend.security.jwt;

import com.taskhub.taskhubbackend.security.CustomerDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    private String username = null;

    Claims claims = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().matches("/taskhub/v1/usuarios/login|taskhub/v1/usuarios/forgotPassword|/taskhub/v1/usuarios/signup")){
            filterChain.doFilter(request, response);
        }else{
            String authorizationHeader = request.getHeader("Authotization");
            String token = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractAllsClaims(token)   ;
            }

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = customerDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    new WebAuthenticationDetailsSource().buildDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);

        }
    }
//
//    public Boolean isAdmin(){
//        return "admin".equalsIgnoreCase((String) claims.get("role"));
//    }
//
//    public Boolean isUser(){
//        return "user".equalsIgnoreCase((String) claims.get("role"));
//    }

    public String getCurrentUser(){
        return username;
    }

}
