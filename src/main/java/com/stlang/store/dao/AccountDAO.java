package com.stlang.store.dao;

import com.stlang.store.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, String> {
    Optional<Boolean> findCountByEmailAndUsernameNot(String email, String username);
    Optional<Account> findByEmail(String email);
}
