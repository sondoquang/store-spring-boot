package com.stlang.store.service;

import com.stlang.store.domain.Role;

import java.util.List;

public interface IRoleService {

    List<Role> findAll();
    Role findById(Integer id);
    Role findByName(String name);
    Role create(String role);
    Role update(Integer id, String role);
    void delete(Integer id);

}
