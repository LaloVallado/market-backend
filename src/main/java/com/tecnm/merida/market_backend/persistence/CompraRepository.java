package com.tecnm.merida.market_backend.persistence;

import com.tecnm.merida.market_backend.domain.Purchase;
import com.tecnm.merida.market_backend.domain.repository.PurchaseRepository;
import com.tecnm.merida.market_backend.persistence.crud.CompraCrudRepository;
import com.tecnm.merida.market_backend.persistence.entity.Compra;
import com.tecnm.merida.market_backend.persistence.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository implements PurchaseRepository {

    @Autowired
    private CompraCrudRepository compraCrudRepository;

    @Autowired
    private PurchaseMapper mapper;

    @Override
    public List<Purchase> getAll() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<List<Purchase>> getByClient(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId)
                .map(compras -> mapper.toPurchases(compras));
    }

    @Override
    public Optional<Purchase> getPurchase(int purchaseId) {
        return compraCrudRepository.findById(purchaseId)
                .map(compra -> mapper.toPurchase(compra));
    }

    @Override
    public Purchase save(Purchase purchase) {
        // Mapper: Purchase (dominio) -> Compra (entidad) con su lista de CompraProducto
        Compra compra = mapper.toCompra(purchase);

        // Cada detalle debe conocer a su compra (Maestro) para el guardado en cascada
        compra.getProductos().forEach(producto -> producto.setCompra(compra));

        // Un solo save: Hibernate inserta la compra y despues todos sus productos (CascadeType.ALL)
        return mapper.toPurchase(compraCrudRepository.save(compra));
    }

    @Override
    public Optional<Purchase> update(int purchaseId, Purchase changes) {
        // Solo se actualiza la cabecera; los productos de la compra no se tocan
        return compraCrudRepository.findById(purchaseId).map(compra -> {
            compra.setMedioPago(changes.getPaymentMethod());
            compra.setComentario(changes.getComment());
            compra.setEstado(changes.getState());
            return mapper.toPurchase(compraCrudRepository.save(compra));
        });
    }

    @Override
    public void delete(int purchaseId) {
        // Gracias a CascadeType.ALL tambien se borran sus registros en compras_productos
        compraCrudRepository.deleteById(purchaseId);
    }
}