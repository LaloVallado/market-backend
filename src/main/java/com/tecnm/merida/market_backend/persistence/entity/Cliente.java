package com.tecnm.merida.market_backend.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    // En la BD la columna id es varchar(20), por eso es String
    @Id
    private String id;

    private String nombre;

    private String apellidos;

    // En la BD la columna celular es numeric, por eso es Long
    private Long celular;

    private String direccion;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    // Contrasena encriptada con BCrypt (nunca en texto plano)
    private String password;

    @OneToMany(mappedBy = "cliente")
    private List<Compra> compras;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getCelular() {
        return celular;
    }

    public void setCelular(Long celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
}