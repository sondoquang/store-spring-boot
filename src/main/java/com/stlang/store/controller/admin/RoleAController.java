package com.stlang.store.controller.admin;

import com.stlang.store.domain.Role;
import com.stlang.store.service.serviceimpl.RoleService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.path}")
public class RoleAController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> roles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.status(OK).body(roles);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRole(@PathVariable("id") String name) {
        Role role = roleService.findById(name);
        return ResponseEntity.status(OK).body(role);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        Role saveRole = roleService.create(role);
        return ResponseEntity.status(OK).body(saveRole);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") String roleId, @RequestParam("name") String roleName) {
        Role saveRole = roleService.update(roleId, roleName);
        return ResponseEntity.status(OK).body(saveRole);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") String roleId) {
        roleService.delete(roleId);
        return ResponseEntity.status(NO_CONTENT).body(null);
    }


}
