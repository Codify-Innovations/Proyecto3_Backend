package com.project.demo.logic.entity.vehiculo;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;


    @Column(length = 100, nullable = false)
    private String marca;

    @Column(length = 100, nullable = false)
    private String modelo;

    @Column(length = 100, nullable = false)
    private String categoria;

    @Column(name = "anio")
    private int anio;

    @Column(name = "imagen_url")
    private String imagenURL;

    @Column(name = "imagen_hot_wheels")
    private String imagenHotWheels;

    @Column(length = 50, nullable = false)
    private String color;

    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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


    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
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


    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getImagenHotWheels() {
        return imagenHotWheels;
    }

    public void setImagenHotWheels(String imagenHotWheels) {
        this.imagenHotWheels = imagenHotWheels;
    }
}
