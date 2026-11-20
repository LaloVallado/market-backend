package com.tecnm.merida.market_backend.domain.service;

import com.tecnm.merida.market_backend.domain.Purchase;
import com.tecnm.merida.market_backend.domain.dto.PurchaseItemRequestDTO;
import com.tecnm.merida.market_backend.domain.dto.PurchaseRequestDTO;
import com.tecnm.merida.market_backend.domain.dto.PurchaseUpdateDTO;
import com.tecnm.merida.market_backend.domain.repository.ClientRepository;
import com.tecnm.merida.market_backend.domain.repository.ProductRepository;
import com.tecnm.merida.market_backend.domain.repository.PurchaseRepository;
import com.tecnm.merida.market_backend.persistence.mapper.PurchaseRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;

    public List<Purchase> getAll() {
        return purchaseRepository.getAll();
    }

    public Optional<List<Purchase>> getByClient(String clientId) {
        return purchaseRepository.getByClient(clientId);
    }

    public Optional<Purchase> getPurchase(int purchaseId) {
        return purchaseRepository.getPurchase(purchaseId);
    }

    // Maestro-Detalle: todo o nada. Si falla cualquier producto se hace rollback de la compra
    @Transactional
    public Purchase save(PurchaseRequestDTO request) {
        validate(request);

        // 1. Mapper: DTO complejo -> Purchase (cabecera + lista de items)
        Purchase purchase = purchaseRequestMapper.toPurchase(request);

        // 2. Datos que pone el sistema, no el cliente
        purchase.setDate(LocalDateTime.now());
        purchase.setPaymentMethod(toOneLetter(request.getMedioPago())); // columna char(1): TARJETA -> T
        purchase.setState("P"); // P = pendiente

        // 3. Un solo save; la cascada guarda los productos
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Optional<Purchase> update(int purchaseId, PurchaseUpdateDTO request) {
        if (request.getMedioPago() == null || request.getMedioPago().isBlank()) {
            throw new IllegalArgumentException("medioPago is required");
        }
        if (request.getEstado() == null || request.getEstado().isBlank()) {
            throw new IllegalArgumentException("estado is required");
        }

        Purchase changes = new Purchase();
        changes.setPaymentMethod(toOneLetter(request.getMedioPago()));
        changes.setComment(request.getComentario());
        changes.setState(toOneLetter(request.getEstado()));

        return purchaseRepository.update(purchaseId, changes);
    }

    @Transactional
    public boolean delete(int purchaseId) {
        if (getPurchase(purchaseId).isPresent()) {
            purchaseRepository.delete(purchaseId);
            return true;
        }
        return false;
    }

    private void validate(PurchaseRequestDTO request) {
        if (request.getIdCliente() == null || clientRepository.getClient(request.getIdCliente()).isEmpty()) {
            throw new IllegalArgumentException("Client " + request.getIdCliente() + " does not exist");
        }
        if (request.getMedioPago() == null || request.getMedioPago().isBlank()) {
            throw new IllegalArgumentException("medioPago is required");
        }
        if (request.getProductosAdquiridos() == null || request.getProductosAdquiridos().isEmpty()) {
            throw new IllegalArgumentException("The purchase must have at least one product");
        }

        Set<Integer> productIds = new HashSet<>();
        for (PurchaseItemRequestDTO item : request.getProductosAdquiridos()) {
            if (item.getIdProducto() == null || productRepository.getProduct(item.getIdProducto()).isEmpty()) {
                throw new IllegalArgumentException("Product " + item.getIdProducto() + " does not exist");
            }
            if (!productIds.add(item.getIdProducto())) {
                throw new IllegalArgumentException("Product " + item.getIdProducto() + " is repeated in the purchase");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("cantidad must be greater than 0");
            }
            if (item.getTotal() == null || item.getTotal() < 0) {
                throw new IllegalArgumentException("total must be 0 or greater");
            }
        }
    }

    private String toOneLetter(String value) {
        return value.trim().substring(0, 1).toUpperCase();
    }
}