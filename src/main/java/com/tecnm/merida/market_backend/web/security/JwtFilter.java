package com.tecnm.merida.market_backend.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Intercepta TODAS las peticiones, busca el header Authorization y valida el token
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Buscar el encabezado Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay token, seguimos sin usuario (Spring Security respondera 401 si la ruta es privada)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Remover la palabra "Bearer " para quedarnos solo con el token
        String token = authHeader.substring(7);

        // 4. Validar el token y registrar al usuario en el contexto de Spring
        if (jwtUtil.isTokenValid(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String email = jwtUtil.extractEmail(token);
                UserDetails user = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                // El cliente del token ya no existe: seguimos sin usuario
            }
        }

        filterChain.doFilter(request, response);
    }
}