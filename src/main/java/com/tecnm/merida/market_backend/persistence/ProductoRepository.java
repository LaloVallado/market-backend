package com.tecnm.merida.market_backend.persistence;

import com.tecnm.merida.market_backend.persistence.crud.ProductoCrudRepository;
import com.tecnm.merida.market_backend.persistence.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductoRepository {

    @Autowired
    private ProductoCrudRepository productoCrudRepository;

    // SELECT * FROM productos
    public List<Producto> getAll() {
        // Se realiza el casteo de Iterable a List
        return (List<Producto>) productoCrudRepository.findAll();
    }
}