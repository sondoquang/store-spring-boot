package com.stlang.store.service;

import com.stlang.store.domain.Authority;
import com.stlang.store.dto.AuthorityDTO;

import java.util.List;

public interface IAuthorityService {

    List<AuthorityDTO> findAll();
    Authority findById(Integer id);
    Authority create(String userId, String roleId);
    Authority update(Integer id, String userId, String roleId);
    void delete(Integer id);
    List<AuthorityDTO> findByAccount(String accountId);

}
