package com.project.demo.logic.entity.vehiculo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(length = 100, nullable = false)
    private String marca;

    @Column(length = 100, nullable = false)
    private String modelo;

    @Column(length = 100, nullable = false)
    private String categoria;

    @Column(name = "anio")
    private int anio;

    @Column(name = "confianza", precision = 5, scale = 2)
    private BigDecimal confianza;

    @Column(name = "imagen_url")
    private String imagenURL;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public BigDecimal getConfianza() {
        return confianza;
    }

    public void setConfianza(BigDecimal confianza) {
        this.confianza = confianza;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }
}
