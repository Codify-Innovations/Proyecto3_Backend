package com.project.demo.logic.entity.services.ranking;


public record RankingUserDTO(
        Long userId,
        String name,
        String lastname,
        long totalVehiculos,
        long totalLogros
) {
}
