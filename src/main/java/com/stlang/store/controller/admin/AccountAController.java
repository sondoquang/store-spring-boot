package com.stlang.store.controller.admin;

import com.stlang.store.domain.Account;
import com.stlang.store.dto.AccountDTO;
import com.stlang.store.dto.AccountPaginateDTO;
import com.stlang.store.dto.ChangePasswordDTO;
import com.stlang.store.dto.LoginDTO;
import com.stlang.store.exception.ChangePasswordException;
import com.stlang.store.service.IAccountService;
import com.stlang.store.service.IFileManagerService;
import com.stlang.store.service.jwt.JWTService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<AccountPaginateDTO> getAccounts(@PathParam("pageNo") Optional<Integer> pageNo,
                                                          @PathParam("pageSize") Optional<Integer> pageSize,
                                                          @PathParam("fullname") Optional<String> fullname,
                                                          @PathParam("email") Optional<String> email
    ) {
        int pageNoValue = pageNo.isPresent() ? pageNo.get() - 1 : 0;
        Map<String, String> queryParams = new HashMap<>();
        if (fullname.isPresent()) {
            queryParams.put("fullname", fullname.get());
        }
        if (email.isPresent()) {
            queryParams.put("email", email.get());
        }
        Page<Account> page = accountService.findAll(pageNoValue, pageSize.orElse(5), queryParams);
        List<AccountDTO> accountDTOs = page.getContent().stream()
                .map(account -> accountService.accountToDTO(account))
                .toList();
        AccountPaginateDTO accountPaginateDTO = new AccountPaginateDTO();
        accountPaginateDTO.setAccounts(accountDTOs);
        AccountPaginateDTO.Meta meta = new AccountPaginateDTO.Meta();
        meta.setCurrentPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setTotalPages(page.getTotalPages());
        meta.setTotalElements(Integer.valueOf(page.getTotalElements() + ""));
        accountPaginateDTO.setMeta(meta);
        return ResponseEntity.status(OK).body(accountPaginateDTO);
    }

    @GetMapping("/accounts/{username}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String username) {
        Account account = accountService.findById(username);
        return ResponseEntity.status(OK).body(accountService.accountToDTO(account));
    }

    @PostMapping(path = "/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account createAccount = accountService.createAccount(account);
        return ResponseEntity.status(OK).body(accountService.accountToDTO(createAccount));
    }

    @PutMapping("/accounts/{username}")
    public ResponseEntity<LoginDTO.UserLogin> updateAccount(@PathVariable String username, @RequestBody Account account) {
        try {
            Account saveAccount = accountService.updateAccount(username, account);
            LoginDTO.UserLogin userLogin = accountService.accountToLoginDTO(saveAccount);
            return ResponseEntity.status(OK).body(userLogin);
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/accounts/{username}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String username) {
        accountService.deleteAccount(username);
        return ResponseEntity.status(OK).body(null);
    }

    @PostMapping("/change-password")
    public ResponseEntity<LoginDTO.UserLogin> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new ChangePasswordException("Password and confirm password do not match");
        }
        Account account = accountService.findById(changePasswordDTO.getUsername());
        Boolean isMatch = passwordEncoder.matches(changePasswordDTO.getPassword(), account.getPassword());
        if(isMatch) {
            String newPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
            account.setPassword(newPassword);
            account.setRefreshToken(null);
            account.setUpdateAt(new Date());
            Account updateAccount = accountService.updateAccount(account.getUsername(),account);
            LoginDTO.UserLogin userLogin = accountService.accountToLoginDTO(updateAccount);
            return ResponseEntity.status(OK).body(userLogin);
        }else{
            throw new ChangePasswordException("Passwords do not match");
        }
    }

}
