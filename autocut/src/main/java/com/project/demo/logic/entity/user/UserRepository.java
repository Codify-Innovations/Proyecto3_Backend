package com.project.demo.logic.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    @Query("SELECT u.visibility FROM User u WHERE u.id = ?1")
    Optional<String> findVisibilityByUserId(Long userId);

    @Query("""
        SELECT u FROM User u
        WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
        AND (:active IS NULL OR u.active = :active)
    """)
    Page<User> searchUsers(String name, String email, Boolean active, Pageable pageable);
}
