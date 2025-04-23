package com.stlang.store.controller;


import com.stlang.store.domain.Account;
import com.stlang.store.dto.AccountDTO;
import com.stlang.store.dto.LoginDTO;
import com.stlang.store.exception.IdInvalidException;
import com.stlang.store.service.IAccountService;
import com.stlang.store.service.jwt.JWTService;
import com.stlang.store.service.serviceimpl.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.path}")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;



    @PostMapping("/auth/register")
    public ResponseEntity<AccountDTO> registerUser(@RequestBody Account user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Account account = accountService.createAccount(user);
        AccountDTO accountDTO = accountService.accountToDTO(account);
        return ResponseEntity.status(CREATED).body(accountDTO);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginDTO> login(@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam("remember") Optional<Boolean> remember) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (auth != null) {
            Account account = accountService.findById(username);
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setAccessToken(jwtService.generateToken((UserDetails) auth.getPrincipal()));
            LoginDTO.UserLogin userLogin = accountService.accountToLoginDTO(account);
            loginDTO.setUserLogin(userLogin);
            String refreshToken = jwtService.generateRefreshToken((UserDetails) auth.getPrincipal(), loginDTO);
            accountService.updateToken(username, refreshToken);

            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(1000 * 60 * 60 * 24 * 100)
                    .build();
            return ResponseEntity.status(OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(loginDTO);
        }
        return ResponseEntity.status(BAD_REQUEST).body(null);
    }

    @GetMapping("/auth/refreshToken")
    public ResponseEntity<LoginDTO> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(jwtService.extractUsername(refreshToken));
            boolean isValidate = jwtService.validateToken(refreshToken, userDetails);
            if (isValidate) {
                Account existingAccount = accountService.FindAccountByIdAndRefreshToken(userDetails.getUsername(), refreshToken);
                if (existingAccount == null) {
                    throw new IdInvalidException("Refresh Token Invalid");
                }
                LoginDTO loginDTO = new LoginDTO();
                loginDTO.setAccessToken(jwtService.generateToken(userDetails));
                LoginDTO.UserLogin userLogin = accountService.accountToLoginDTO(existingAccount);
                loginDTO.setUserLogin(userLogin);
                String newRefreshToken = jwtService.generateRefreshToken(userDetails, loginDTO);
                accountService.updateToken(existingAccount.getUsername(), newRefreshToken);

                ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(1000 * 60 * 60 * 24 * 100)
                        .build();
                return ResponseEntity.status(OK)
                        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                        .body(loginDTO);
            } else
                throw new IdInvalidException("Refresh Token Invalid");
        }
        return ResponseEntity.status(UNAUTHORIZED).body(null);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if(username.equals("")){
                throw new IdInvalidException("Access Token Invalid");
            }
            accountService.updateToken(username, null);
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", null)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();
            return ResponseEntity.status(OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .build();
        }
        return ResponseEntity.status(UNAUTHORIZED).body(null);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<LoginDTO.UserLogin> getInfoAccount(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        if (username == null) {
            return ResponseEntity.status(UNAUTHORIZED).body(null);
        }else{
            Account existingAccount = accountService.findById(username);
            LoginDTO.UserLogin accountLogin = accountService.accountToLoginDTO(existingAccount);
            return ResponseEntity.status(OK).body(accountLogin);
        }
    }


}
