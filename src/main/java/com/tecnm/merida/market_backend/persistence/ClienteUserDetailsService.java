package com.tecnm.merida.market_backend.persistence;

import com.tecnm.merida.market_backend.persistence.crud.ClienteCrudRepository;
import com.tecnm.merida.market_backend.persistence.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Le dice a Spring Security como buscar un usuario: es un cliente buscado por su correo
@Service
public class ClienteUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteCrudRepository clienteCrudRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteCrudRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found: " + email));

        if (cliente.getPassword() == null) {
            throw new UsernameNotFoundException("Client has no password: " + email);
        }

        return User.withUsername(cliente.getCorreoElectronico())
                .password(cliente.getPassword())
                .roles("CLIENT")
                .build();
    }
}