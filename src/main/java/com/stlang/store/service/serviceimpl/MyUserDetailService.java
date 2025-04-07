package com.stlang.store.service.serviceimpl;

import com.stlang.store.domain.Account;
import com.stlang.store.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private AccountService userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepo.findById(username);
        if(account == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return new com.stlang.store.domain.UserPrincipal(account);
    }
}
