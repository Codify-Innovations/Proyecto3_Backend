package com.project.demo.logic.entity.usuarioLogro;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.demo.logic.entity.logro.Logro;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.serializers.UsuarioIdSerializer;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios_logros")
public class UsuarioLogro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    // ===== Uso de serialize para evitar que las respuestas contengan ===== //
    // ===== el objeto completo de usuario y retorne solamente el id   ===== //
    @JsonSerialize(using = UsuarioIdSerializer.class)
    private User usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "logro_id", nullable = false)
    private Logro logro;

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

    public Logro getLogro() {
        return logro;
    }

    public void setLogro(Logro logro) {
        this.logro = logro;
    }

    public LocalDateTime getFechaDesbloqueo() {
        return fechaDesbloqueo;
    }

    public void setFechaDesbloqueo(LocalDateTime fechaDesbloqueo) {
        this.fechaDesbloqueo = fechaDesbloqueo;
    }

    @Column(name = "fecha_desbloqueo")
    private LocalDateTime fechaDesbloqueo = LocalDateTime.now();
}