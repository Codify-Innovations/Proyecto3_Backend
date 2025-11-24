package com.project.demo.logic.entity.services.ranking;

import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.usuarioLogro.UsuarioLogroRepository;
import com.project.demo.logic.entity.vehiculo.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    public List<RankingUserDTO> getTopUsers() {

        return rankingRepository.getTopUsers()
                .stream()
                .map(r -> new RankingUserDTO(
                        r.getUserId(),
                        r.getName(),
                        r.getLastname(),
                        r.getTotalVehiculos(),
                        r.getTotalLogros()
                ))
                .toList();
    }
}
