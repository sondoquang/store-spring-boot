package com.stlang.store.dao;

import com.stlang.store.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);

    Account findByUsernameAndRefreshToken(String username, String email);

    Page<Account> findByEmailContaining(String email, Pageable pageable);

    Page<Account> findByFullnameContaining(String fullname, Pageable pageable);
    Page<Account> findByFullnameContainingAndEmailContaining(String fullname,String email, Pageable pageable);
}
