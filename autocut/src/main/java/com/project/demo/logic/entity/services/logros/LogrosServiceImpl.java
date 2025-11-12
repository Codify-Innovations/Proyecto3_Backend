package com.project.demo.logic.entity.services.logros;

import com.project.demo.logic.entity.logro.Logro;
import com.project.demo.logic.entity.logro.LogroRepository;
import com.project.demo.logic.entity.notificacion.Notificacion;
import com.project.demo.logic.entity.notificacion.NotificacionRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogro;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogroRepository;
import com.project.demo.logic.entity.vehiculo.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@Transactional
public class LogrosServiceImpl implements LogrosService{

    @Autowired
    private LogroRepository logroRepository;
    @Autowired
    private UsuarioLogroRepository usuarioLogroRepository;
    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public void evaluateAchievementsForUser(User usuario) {
        List<Logro> logrosActivos = logroRepository.findByActivoTrue();

        for (Logro logro : logrosActivos) {

            boolean yaDesbloqueado = usuarioLogroRepository.existsByUsuarioAndLogro(usuario, logro);
            if (yaDesbloqueado) continue;

            switch (logro.getCriterio()) {
                case "categoria_count":
                    long cantidadCategoria = vehiculoRepository.countByUsuarioIdAndCategoria(usuario.getId(), logro.getCategoria());
                    if (cantidadCategoria >= logro.getCantidadRequerida()) {
                        desbloquearLogro(usuario, logro);
                    }
                    break;

                case "marca_count":
                    long cantidadMarca = vehiculoRepository.countByUsuarioIdAndMarca(usuario.getId(), logro.getCategoria());
                    if (cantidadMarca >= logro.getCantidadRequerida()) {
                        desbloquearLogro(usuario, logro);
                    }
                    break;

                case "total_detectados":
                    long total = vehiculoRepository.countByUsuarioId(usuario.getId());
                    if (total >= logro.getCantidadRequerida()) {
                        desbloquearLogro(usuario, logro);
                    }
                    break;

                default:
                    break;
            }
        }
    }
    private void desbloquearLogro(User usuario, Logro logro) {
        UsuarioLogro nuevo = new UsuarioLogro();
        nuevo.setUsuario(usuario);
        nuevo.setLogro(logro);
        nuevo.setFechaDesbloqueo(LocalDateTime.now());
        usuarioLogroRepository.save(nuevo);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje("¡Has desbloqueado el logro " + logro.getNombre() + "!");
        notificacion.setTipo("logro");
        notificacion.setFecha(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
    @Override
    public List<Logro> getAllActiveAchievements() {
        return logroRepository.findByActivoTrue();
    }

    @Override
    public List<UsuarioLogro> getUnlockedAchievements(User usuario) {
        return usuarioLogroRepository.findByUsuario(usuario);
    }
}
