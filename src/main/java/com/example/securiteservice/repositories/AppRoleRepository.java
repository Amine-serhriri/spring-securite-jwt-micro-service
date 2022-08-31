package com.example.securiteservice.repositories;

import com.example.securiteservice.entities.AppRole;
import com.example.securiteservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findByRoleName(String roleName);
}
