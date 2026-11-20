package com.tecnm.merida.market_backend.domain.dto;

import java.util.List;

// DTO complejo: cabecera (Maestro) + lista de productos (Detalle)
public class PurchaseRequestDTO {

    private String idCliente;
    private String medioPago;
    private String comentario;
    private List<PurchaseItemRequestDTO> productosAdquiridos;

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<PurchaseItemRequestDTO> getProductosAdquiridos() {
        return productosAdquiridos;
    }

    public void setProductosAdquiridos(List<PurchaseItemRequestDTO> productosAdquiridos) {
        this.productosAdquiridos = productosAdquiridos;
    }
}