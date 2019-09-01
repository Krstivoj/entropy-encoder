package com.encoder.entropy_ecnoder.repository;

import com.encoder.entropy_ecnoder.model.Role;
import com.encoder.entropy_ecnoder.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
