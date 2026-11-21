package com.tecnm.merida.market_backend.domain.service;

import com.tecnm.merida.market_backend.domain.dto.LoginRequestDTO;
import com.tecnm.merida.market_backend.web.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequestDTO request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 1. Buscar al cliente por su correo
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(request.getEmail());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 2. Comparar la contrasena plana contra la encriptada (BCrypt)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 3. Si es correcta, generar y regresar el token JWT
        return jwtUtil.generateToken(user.getUsername());
    }
}