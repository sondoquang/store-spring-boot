package com.stlang.store.service;

import com.stlang.store.domain.Account;
import com.stlang.store.dto.AccountDTO;
import com.stlang.store.dto.LoginDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IAccountService {

    Page<Account> findAll(int pageNumber, int pageSize, Sort... sort);
    List<Account> findAll();
    Account findById(String username);
    Account createAccount(Account account);
    Account updateAccount(String username, Account account);
    void deleteAccount(String username);
    Account findByEmail(String email);

    void updateToken(String username, String token);
    AccountDTO accountToDTO(Account account);
    LoginDTO.UserLogin accountToLoginDTO(Account account);
    Account FindAccountByIdAndRefreshToken(String username, String refreshToken);
}
