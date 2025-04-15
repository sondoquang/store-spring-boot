package com.stlang.store.service;

import com.stlang.store.domain.Role;

import java.util.List;

public interface IRoleService {

    List<Role> findAll();
    Role findById(String id);
    Role findByName(String name);
    Role create(Role role);
    Role update(String id, String role);
    void delete(String id);

}
