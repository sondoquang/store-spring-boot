package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.AccountDAO;
import com.stlang.store.dao.AuthorityDAO;
import com.stlang.store.dao.RoleDAO;
import com.stlang.store.domain.Account;
import com.stlang.store.domain.Authority;
import com.stlang.store.domain.Role;
import com.stlang.store.dto.AccountDTO;
import com.stlang.store.dto.AuthorityDTO;
import com.stlang.store.exception.DataNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.stlang.store.service.IAuthorityService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService implements IAuthorityService {

    private final AuthorityDAO authorityDAO;
    private final AccountDAO accountDAO;
    private final RoleDAO roleDAO;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public AuthorityService(AuthorityDAO authorityDAO, AccountDAO accountDAO, RoleDAO roleDAO, AccountService accountService, ModelMapper modelMapper) {
        this.authorityDAO = authorityDAO;
        this.accountDAO = accountDAO;
        this.roleDAO = roleDAO;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AuthorityDTO> findAll() {
       List<Authority> authorities = authorityDAO.findAll();
       List<AuthorityDTO> authoritiesDTO = new ArrayList<AuthorityDTO>();
        authorities.forEach(authority -> {
            AuthorityDTO authorityDTO = AuthorityDTO.builder()
                    .id(authority.getId())
                    .accountDTO(accountService.accountToDTO(authority.getAccount()))
                    .role(authority.getRole())
                    .build();
            authoritiesDTO.add(authorityDTO);
        });
        return authoritiesDTO;
    }

    @Override
    public Authority findById(Integer id) {
        return authorityDAO.findById(id)
                .orElseThrow(() -> {throw new DataNotFoundException("Authority Not Found with id " + id);});
    }

    @Override
    public Authority create(String userId, String roleId) {
        Role role = roleDAO.findById(roleId)
                .orElseThrow(() -> {throw new DataNotFoundException("Role Not Found with id " + roleId);});
        Account account = accountDAO.findById(userId)
                .orElseThrow(() -> {throw new DataNotFoundException("Account Not Found with id " + userId);});
        Authority authority = Authority.builder()
                .account(account)
                .role(role)
                .build();
        return authorityDAO.save(authority);
    }

    @Override
    public Authority update(Integer id, String userId, String roleId) {
        Role role = null;
        Account account = null;
        Authority saveAuthority = authorityDAO.findById(id)
                .orElseThrow(() -> {throw new DataNotFoundException("Authority Not Found with id " + id);});
        if(userId != null) {
            role = roleDAO.findById(roleId)
                    .orElseThrow(() -> {throw new DataNotFoundException("Role Not Found with id " + roleId);});
            saveAuthority.setRole(role);
        }
        if (roleId != null) {
            account = accountDAO.findById(userId)
                    .orElseThrow(() -> {throw new DataNotFoundException("Account Not Found with id " + userId);});
            saveAuthority.setAccount(account);
        }
        return authorityDAO.save(saveAuthority);
    }

    @Override
    public void delete(Integer id) {
        authorityDAO.findById(id)
                .orElseThrow(() -> { throw new DataNotFoundException("Authority Not Found with id " + id);});
        authorityDAO.deleteById(id);
    }

    @Override
    public List<AuthorityDTO> findByAccount(String accountId) {
        Account account = accountDAO.findById(accountId).orElseThrow(() -> {throw new DataNotFoundException("Account Not Found with id " + accountId);});
        List<Authority> authorities = authorityDAO.findByAccount(account);
        List<AuthorityDTO> authorityDTOS = new ArrayList<>();
        authorities.forEach(authority -> {
            AuthorityDTO authorityDTO = AuthorityDTO.builder()
                    .id(authority.getId())
                    .accountDTO(accountService.accountToDTO(authority.getAccount()))
                    .role(authority.getRole())
                    .build();
            authorityDTOS.add(authorityDTO);
        });
        return authorityDTOS;
    }
}
