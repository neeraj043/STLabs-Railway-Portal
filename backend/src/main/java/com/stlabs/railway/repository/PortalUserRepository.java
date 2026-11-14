package com.stlabs.railway.repository;

import com.stlabs.railway.model.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortalUserRepository extends JpaRepository<PortalUser, Long> {
    Optional<PortalUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
