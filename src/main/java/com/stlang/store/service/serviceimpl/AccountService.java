package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.AccountDAO;
import com.stlang.store.dao.AuthorityDAO;
import com.stlang.store.dao.RoleDAO;
import com.stlang.store.domain.Account;
import com.stlang.store.domain.Authority;
import com.stlang.store.domain.Role;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AccountService implements IAccountService {

    private final AccountDAO accountDAO;
    private final ModelMapper modelMapper;
    private final RoleDAO roleDAO;
    private final AuthorityDAO authorityDAO;

    public AccountService(AccountDAO accountDAO, ModelMapper modelMapper, RoleDAO roleDAO, AuthorityDAO authorityDAO) {
        this.accountDAO = accountDAO;
        this.modelMapper = modelMapper;
        this.roleDAO = roleDAO;
        this.authorityDAO = authorityDAO;
    }

    @Override
    public Page<Account> findAll(int pageNumber, int pageSize, Map<String,String> queries, Sort... sort) {
        Pageable pageable;
        if(sort.length > 0){
            pageable = PageRequest.of(pageNumber, pageSize, sort[0]);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        if(queries.size() < 2){
            if(queries.containsKey("email")){
                return accountDAO.findByEmailContaining(queries.get("email"), pageable);
            }
            if(queries.containsKey("fullname")){
                return accountDAO.findByFullnameContaining(queries.get("fullname"), pageable);
            }
        }else if(queries.size() == 2){
            String email = queries.get("email");
            String fullName = queries.get("fullname");
            return accountDAO.findByFullnameContainingAndEmailContaining(fullName, email, pageable);
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
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
    }

    @Override
    public Account createAccount(Account account) {
        Account existingAccount = accountDAO.findById(account.getUsername()).orElse(null);
        if(existingAccount != null){
            throw new DataExistingException("Username's Account already exists");
        }
        Account emailExisting  = accountDAO.findByEmail(account.getEmail()).orElse(null);
        if (emailExisting != null) {
            throw new DataExistingException("Email's Account already exists");
        }

        Account newAccount = accountDAO.save(account);

        // Tìm kiếm trả về quyền USER : return Role: USER//
        Role userRole = roleDAO.findById("USER").orElseThrow(() -> new DataNotFoundException("Role user not found"));
        // Tạo quyền USER cho người dùng: Tạo authority: USER //

        authorityDAO.save(Authority.builder()
                        .account(newAccount)
                        .role(userRole).build());
        return newAccount;
    }

    @Override
    public Account updateAccount(String username, Account account) {
        Account existingAccount = accountDAO.findById(username)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
        existingAccount.setEmail(account.getEmail());
        existingAccount.setFullname(account.getFullname());
        existingAccount.setGender(account.getGender());
        existingAccount.setUpdateAt(new Date());
        if(account.getPhoto() != null){
            existingAccount.setPhoto(account.getPhoto());
        }
        return accountDAO.save(existingAccount);
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
        AccountDTO accountDTO =  modelMapper.map(account, AccountDTO.class);
        List<String> roles = new ArrayList<>();
        if(account.getAuthorities() != null){
            account.getAuthorities().forEach(authority -> {
                String role = authority.getRole().getId();
                roles.add(role);
            });
            accountDTO.setRoles(roles);
        }
        return accountDTO;
    }

    @Override
    public LoginDTO.UserLogin accountToLoginDTO(Account account) {
        LoginDTO.UserLogin loginDTO = new LoginDTO.UserLogin();
        loginDTO.setUsername(account.getUsername());
        loginDTO.setEmail(account.getEmail());
        loginDTO.setFullName(account.getFullname());
        loginDTO.setGender(account.getGender());
        loginDTO.setPhoto(account.getPhoto());
        List<String> roles = new ArrayList<>();
        account.getAuthorities().forEach(authority -> {
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
