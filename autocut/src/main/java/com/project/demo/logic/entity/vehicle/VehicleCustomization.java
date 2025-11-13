package com.project.demo.logic.entity.vehicle;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "vehicle_customizations")
public class VehicleCustomization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String modelo;

    @Column(length = 20)
    private String carroceria;

    @Column(name = "vidrios_polarizados")
    private boolean vidriosPolarizados;

    @Column(length = 20)
    private String interior;

    @Column(length = 30)
    private String rines;

    @Column(name = "luces_front", length = 30)
    private String lucesFront;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Date fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private Date fechaActualizacion;

    public VehicleCustomization() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    public boolean isVidriosPolarizados() {
        return vidriosPolarizados;
    }

    public void setVidriosPolarizados(boolean vidriosPolarizados) {
        this.vidriosPolarizados = vidriosPolarizados;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getRines() {
        return rines;
    }

    public void setRines(String rines) {
        this.rines = rines;
    }

    public String getLucesFront() {
        return lucesFront;
    }

    public void setLucesFront(String lucesFront) {
        this.lucesFront = lucesFront;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "VehicleCustomization{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", carroceria='" + carroceria + '\'' +
                ", vidriosPolarizados=" + vidriosPolarizados +
                ", interior='" + interior + '\'' +
                ", rines='" + rines + '\'' +
                ", lucesFront='" + lucesFront + '\'' +
                '}';
    }
}
