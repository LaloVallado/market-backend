package com.tecnm.merida.market_backend.domain.dto;

// Datos de la cabecera que se pueden modificar con PUT
public class PurchaseUpdateDTO {

    private String medioPago;
    private String comentario;
    private String estado;

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}