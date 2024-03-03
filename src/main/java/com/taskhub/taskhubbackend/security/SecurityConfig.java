package com.taskhub.taskhubbackend.security;

import com.taskhub.taskhubbackend.security.jwt.JwtFilter;
import com.taskhub.taskhubbackend.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;
import java.io.InputStream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        

        // httpSecurity.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
        //         .and()
        //         .csrf().disable()
        //         .authorizeHttpRequests()
        //         .requestMatchers("/taskhub/v1/usuarios/login", "/taskhub/v1/usuarios/signup", "/taskhub/v1/usuarios/forgotPassword").permitAll()
        //         .anyRequest()
        //         .authenticated()
        //         .and().exceptionHandling()
        //         .and().sessionManagement()
        //         .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200/"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(false);

        httpSecurity.cors().configurationSource(request -> corsConfiguration)
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/taskhub/v1/usuarios/login", "/taskhub/v1/usuarios/signup", "/taskhub/v1/usuarios/forgotPassword").permitAll()
                .anyRequest()
                .authenticated()
                .and().exceptionHandling()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        

        // httpSecurity.cors().configurationSource(request -> corsConfiguration);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    

    @Bean
    public EmailService emailService(){
        return new EmailService();
    }

    @Bean
    public JavaMailSender javaMailSender(){
        return new JavaMailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                JavaMailSender.super.send(simpleMessage);
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {

            }

            @Override
            public MimeMessage createMimeMessage() {
                return null;
            }

            @Override
            public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
                return null;
            }

            @Override
            public void send(MimeMessage mimeMessage) throws MailException {
                JavaMailSender.super.send(mimeMessage);
            }

            @Override
            public void send(MimeMessage... mimeMessages) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
                JavaMailSender.super.send(mimeMessagePreparator);
            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
                JavaMailSender.super.send(mimeMessagePreparators);
            }
        };
    }

}
