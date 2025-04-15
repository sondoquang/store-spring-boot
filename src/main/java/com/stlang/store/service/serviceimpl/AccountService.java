package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.AccountDAO;
import com.stlang.store.domain.Account;
import com.stlang.store.dto.AccountDTO;
import com.stlang.store.dto.LoginDTO;
import com.stlang.store.exception.DataExistingException;
import com.stlang.store.exception.DataNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.stlang.store.service.IAccountService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    private final AccountDAO accountDAO;
    private final ModelMapper modelMapper;

    public AccountService(AccountDAO accountDAO, ModelMapper modelMapper) {
        this.accountDAO = accountDAO;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<Account> findAll(int pageNumber, int pageSize, Sort... sort) {
        Pageable pageable;
        if(sort.length > 0){
            pageable = PageRequest.of(pageNumber, pageSize, sort[0]);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return accountDAO.findAll(pageable);
    }

    @Override
    public List<Account> findAll() {
        return accountDAO.findAll();
    }

    @Override
    public Account findById(String username) {
        return accountDAO.findById(username)
                .orElseThrow(() -> { throw new DataNotFoundException("Account not found");});
    }

    @Override
    public Account createAccount(Account account) {
        Account existingAccount = accountDAO.findById(account.getUsername()).orElse(null);
        if(existingAccount != null){
            throw new DataExistingException("Username's Account already exists");
        }
        Account emailExisting  = accountDAO.findByEmail(account.getEmail()).orElse(null);
        if (emailExisting != null){
            throw new DataExistingException("Email's Account already exists");
        }
        return accountDAO.save(account);
    }

    @Override
    public Account updateAccount(String username, Account account) {
        Account existingAccount = accountDAO.findById(username)
                .orElseThrow(() ->{throw new DataNotFoundException("Account not found");});
        account.setPassword(existingAccount.getPassword());
        return accountDAO.save(account);
    }

    @Override
    public void deleteAccount(String username) {
        accountDAO.findById(username)
                .orElseThrow(() -> new DataNotFoundException("Account Not Found with username: " + username));
        accountDAO.deleteById(username);
    }

    @Override
    public Account findByEmail(String email) {
        return accountDAO.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Account Not Found with email: " + email));
    }

    @Override
    public void updateToken(String username, String token) {
        Account account = findById(username);
        account.setRefreshToken(token);
        accountDAO.save(account);
    }

    @Override
    public AccountDTO accountToDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public LoginDTO.UserLogin accountToLoginDTO(Account account) {
        LoginDTO.UserLogin loginDTO = new LoginDTO.UserLogin();
        loginDTO.setUsername(account.getUsername());
        loginDTO.setEmail(account.getEmail());
        loginDTO.setFullName(account.getFullname());
        loginDTO.setGender(account.getGender());
        List<String> roles = new ArrayList<>();
        account.getAuthorities().stream().forEach(authority -> {
            String role = authority.getRole().getId();
            roles.add(role);
        });
        loginDTO.setRoles(roles);
        return loginDTO;
    }

    @Override
    public Account FindAccountByIdAndRefreshToken(String username, String refreshToken) {
        return accountDAO.findByUsernameAndRefreshToken(username, refreshToken);
    }



}
