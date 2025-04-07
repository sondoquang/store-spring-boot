package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.RoleDAO;
import com.stlang.store.domain.Role;
import com.stlang.store.exception.DataExistingException;
import com.stlang.store.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements com.stlang.store.service.IRoleService {

    private final RoleDAO roleDAO;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    @Override
    public Role findById(Integer id) {
        return roleDAO.findById(id).orElseThrow(() -> new DataNotFoundException("Role not found"));
    }

    @Override
    public Role findByName(String name) {
        return roleDAO.findByNameLike(name).orElseThrow(() -> new DataNotFoundException("Role not found"));
    }

    @Override
    public Role create(String role) {
        Role existingRole = findByName(role);
        if (existingRole == null) {
            Role saveRole = Role.builder().name(role).build();
            return roleDAO.save(saveRole);
        }
        throw new DataExistingException("Role already exists");
    }

    @Override
    public Role update(Integer id, String role) {
        Role existingRole = findById(id);
        existingRole.setName(role);
        return roleDAO.save(existingRole);
    }

    @Override
    public void delete(Integer id) {
        roleDAO.findById(id).orElseThrow(() -> new DataNotFoundException("Role not found"));
        roleDAO.deleteById(id);
    }
}
