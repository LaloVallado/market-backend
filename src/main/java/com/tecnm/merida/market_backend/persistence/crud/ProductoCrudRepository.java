package com.tecnm.merida.market_backend.persistence.crud;

import com.tecnm.merida.market_backend.persistence.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoCrudRepository extends CrudRepository<Producto, Integer> {

    // La anotación @Query evita que Spring se confunda con los nombres
    @Query(value = "SELECT * FROM productos WHERE id_categoria = ?1 ORDER BY nombre ASC", nativeQuery = true)
    List<Producto> findByIdCategoriaOrderByNombreAsc(int idCategoria);

    Optional<List<Producto>> findByCantidadStockLessThanAndEstado(int cantidadStock, boolean estado);
}