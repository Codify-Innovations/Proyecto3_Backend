package com.project.demo.logic.entity.services.ranking;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankingRepository extends JpaRepository<User, Long> {
    @Query(value = """
        SELECT 
            u.id AS userId,
            u.name AS name,
            u.lastname AS lastname,
            COALESCE(v.totalVehiculos, 0) AS totalVehiculos,
            COALESCE(l.totalLogros, 0) AS totalLogros
        FROM user u
        LEFT JOIN (
            SELECT usuario_id, COUNT(*) AS totalVehiculos
            FROM vehiculos
            GROUP BY usuario_id
        ) v ON v.usuario_id = u.id
        LEFT JOIN (
            SELECT usuario_id, COUNT(*) AS totalLogros
            FROM usuarios_logros
            GROUP BY usuario_id
        ) l ON l.usuario_id = u.id
        WHERE u.email <> 'super.admin@gmail.com'
        ORDER BY totalVehiculos DESC, totalLogros DESC
        LIMIT 5
        """, nativeQuery = true)
    List<RankingProjection> getTopUsers();
}
