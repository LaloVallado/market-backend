package com.tecnm.merida.market_backend.domain.dto;

// Cada renglon del ticket (Detalle)
public class PurchaseItemRequestDTO {

    private Integer idProducto;
    private Integer cantidad;
    private Double total;

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}