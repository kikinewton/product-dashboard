package com.bsupply.productdashboard.repository;

import com.bsupply.productdashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("UPDATE User u SET u.lastLogin= CURRENT_TIMESTAMP WHERE u.email =:email")
    @Modifying
    @Transactional
    void updateLastLogin(@Param("email") String email);
}
