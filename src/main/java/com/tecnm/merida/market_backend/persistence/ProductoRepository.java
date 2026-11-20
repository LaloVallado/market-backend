package com.tecnm.merida.market_backend.persistence;

import com.tecnm.merida.market_backend.domain.Product;
import com.tecnm.merida.market_backend.domain.repository.ProductRepository;
import com.tecnm.merida.market_backend.persistence.crud.ProductoCrudRepository;
import com.tecnm.merida.market_backend.persistence.entity.Producto;
import com.tecnm.merida.market_backend.persistence.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository implements ProductRepository {

    @Autowired
    private ProductoCrudRepository productoCrudRepository;

    @Autowired
    private ProductMapper mapper;

    @Override
    public List<Product> getAll() {
        List<Producto> productos = (List<Producto>) productoCrudRepository.findAll();
        return mapper.toProducts(productos);
    }

    @Override
    public Optional<List<Product>> getByCategory(int categoryId) {
        List<Producto> productos = productoCrudRepository.findByIdCategoriaOrderByNombreAsc(categoryId);
        return Optional.of(mapper.toProducts(productos));
    }

    @Override
    public Optional<List<Product>> getScarceProducts(int quantity) {
        return productoCrudRepository.findByCantidadStockLessThanAndEstado(quantity, true)
                .map(productos -> mapper.toProducts(productos));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.findById(productId)
                .map(producto -> mapper.toProduct(producto));
    }

    @Override
    public Product save(Product product) {
        Producto producto = mapper.toProducto(product);

        // Si es una actualizacion, conservamos el codigo de barras que ya tenia
        // (el dominio Product no maneja codigo de barras y se borraria)
        if (producto.getIdProducto() != null) {
            productoCrudRepository.findById(producto.getIdProducto())
                    .ifPresent(actual -> producto.setCodigoBarras(actual.getCodigoBarras()));
        }

        return mapper.toProduct(productoCrudRepository.save(producto));
    }

    @Override
    public void delete(int productId) {
        productoCrudRepository.deleteById(productId);
    }
}