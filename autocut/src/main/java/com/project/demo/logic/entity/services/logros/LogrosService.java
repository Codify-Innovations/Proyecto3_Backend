package com.project.demo.logic.entity.services.logros;

import com.project.demo.logic.entity.logro.Logro;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogro;

import java.util.List;

public interface LogrosService {

    void evaluateAchievementsForUser(User usuario);

    List<Logro> getAllActiveAchievements();

    List<UsuarioLogro> getUnlockedAchievements(User usuario);
}
