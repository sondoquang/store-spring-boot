package com.stlang.store.service;

import com.stlang.store.domain.Authority;

import java.util.List;

public interface IAuthorityService {

    List<Authority> findAll();
    Authority findById(Integer id);
    Authority create(Authority authority);
    Authority update(Authority authority);
    void delete(Integer id);

}
