package com.stlang.store.controller.admin;

import com.stlang.store.domain.Account;
import com.stlang.store.dto.AccountDTO;
import com.stlang.store.service.IAccountService;
import com.stlang.store.service.IFileManagerService;
import com.stlang.store.service.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.path}")
public class AccountAController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IFileManagerService fileManagerService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        List<Account> accounts = accountService.findAll();
        List<AccountDTO> accountDTOs = accounts.stream()
                .map(account -> accountService.accountToDTO(account))
                .toList();
        return ResponseEntity.status(OK).body(accountDTOs);
    }

    @GetMapping("/accounts/{username}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String username) {
        Account account = accountService.findById(username);
        return ResponseEntity.status(OK).body(accountService.accountToDTO(account));
    }

    @PostMapping(path = "/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody Account account) {
        Account createAccount = null;
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        createAccount = accountService.createAccount(account);
        return ResponseEntity.status(OK).body(accountService.accountToDTO(createAccount));
    }

    @PutMapping("/accounts/{username}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable String username, @RequestBody Account account) {
        try {
            Account saveAccount = accountService.updateAccount(username, account);
            return ResponseEntity.status(OK).body(accountService.accountToDTO(saveAccount));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/accounts/{username}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String username) {
        try {
            accountService.deleteAccount(username);
            return ResponseEntity.status(NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

}
