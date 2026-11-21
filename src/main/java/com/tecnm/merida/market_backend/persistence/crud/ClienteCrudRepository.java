package com.tecnm.merida.market_backend.persistence.crud;

import com.tecnm.merida.market_backend.persistence.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClienteCrudRepository extends CrudRepository<Cliente, String> {

    // Para el login: buscar al cliente por su correo
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
}