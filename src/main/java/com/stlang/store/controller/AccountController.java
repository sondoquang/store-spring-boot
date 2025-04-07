package com.stlang.store.controller;

import com.stlang.store.domain.Account;
import com.stlang.store.exception.DataExistingException;
import com.stlang.store.exception.DataNotFoundException;
import com.stlang.store.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @GetMapping("/api/v01/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.findAll();
        return accounts.isEmpty() ? new ResponseEntity<>(NOT_FOUND) : new ResponseEntity<>(accounts, HttpStatus.OK);
    }


    @GetMapping("/api/v01/accounts/{username}")
    public ResponseEntity<Account> getAccount(@PathVariable String username) {
        Account account = accountService.findById(username);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/api/v01/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createAccount = null;
        try {
            createAccount = accountService.createAccount(account);
            return ResponseEntity.status(OK).body(createAccount);
        } catch (DataExistingException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/api/v01/accounts/{username}")
    public ResponseEntity<Account> updateAccount(@PathVariable String username, @RequestBody Account account) {
        try {
            Account saveAccount = accountService.updateAccount(account);
            return ResponseEntity.status(OK).body(saveAccount);
        } catch (DataExistingException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }


    }

    @DeleteMapping("/api/v01/accounts/{username}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String username) {
        try {
            accountService.deleteAccount(username);
            return ResponseEntity.status(NO_CONTENT).build();
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

}
