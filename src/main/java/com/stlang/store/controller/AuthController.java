package com.stlang.store.controller;


import com.stlang.store.domain.Account;
import com.stlang.store.dto.LoginDTO;
import com.stlang.store.service.IAccountService;
import com.stlang.store.service.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.path}")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/getAccount")
    public String account(){
        return "account";
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Account account = accountService.createAccount(user);
        return ResponseEntity.status(CREATED).body(account);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody Account user){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if(auth != null){

            Account account = accountService.findById(user.getUsername());

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setAccessToken(jwtService.generateToken(auth.getName()));
            LoginDTO.UserLogin userLogin = new LoginDTO.UserLogin();
            userLogin.setUsername(user.getUsername());
            userLogin.setFullName(account.getFullname());
            userLogin.setEmail(account.getEmail());
            userLogin.setGender(account.getGender());
            loginDTO.setUserLogin(userLogin);
            String refreshToken = jwtService.generateRefreshToken(user.getEmail(),loginDTO);
            accountService.updateToken(user.getUsername(), refreshToken);

            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(1000*60*60*24*100)
                    .build();
            return ResponseEntity.status(OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(loginDTO);
        }
        return ResponseEntity.status(BAD_REQUEST).body(null);
    }

}
