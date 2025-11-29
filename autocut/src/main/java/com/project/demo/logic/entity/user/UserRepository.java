package com.project.demo.logic.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Override
    void deleteById(Long id);

    @Query("SELECT u.visibility FROM User u WHERE u.id = :userId")
    Optional<String> findVisibilityByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u " + "WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) " + "AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " + "AND (:active IS NULL OR u.active = :active)")
    Page<User> searchUsers(@Param("name") String name, @Param("email") String email, @Param("active") Boolean active, Pageable pageable);

    long countByCreatedAtBetween(Date start, Date end);

    long countByActiveTrue();

    long countByActiveFalse();
}
