package com.project.demo.rest.ranking;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.services.ranking.RankingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking")
public class RankingRestController {

    @Autowired
    private RankingServiceImpl rankingService;

    @GetMapping("/top")
    public ResponseEntity<?> getTopUsers(HttpServletRequest request) {

        var topUsers = rankingService.getTopUsers();

        return new GlobalResponseHandler().handleResponse(
                "Ranking obtenido correctamente.",
                topUsers,
                HttpStatus.OK,
                request
        );
    }
}