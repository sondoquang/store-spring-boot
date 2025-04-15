package com.stlang.store.dao;

import com.stlang.store.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDAO extends JpaRepository<Role, String> {
    Optional<Role> findByNameLike(String name);
}
