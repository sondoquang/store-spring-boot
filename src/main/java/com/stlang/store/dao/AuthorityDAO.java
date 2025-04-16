package com.stlang.store.dao;

import com.stlang.store.domain.Account;
import com.stlang.store.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityDAO extends JpaRepository<Authority,Integer> {

    List<Authority> findByAccount(Account account);

}
