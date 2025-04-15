package com.stlang.store.controller.admin;

import com.stlang.store.domain.Authority;
import com.stlang.store.dto.AuthorityDTO;
import com.stlang.store.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.path}")
public class AuthorityAController {

    @Autowired
    private IAuthorityService authorityService;

    @GetMapping("/authorities")
    public ResponseEntity<List<AuthorityDTO>> authorities() {
        List<AuthorityDTO> authoritiesDTO = authorityService.findAll();
        return ResponseEntity.status(OK).body(authoritiesDTO);
    }

    @GetMapping("/authorities/{id}")
    public ResponseEntity<Authority> authorities(@PathVariable Integer id) {
        Authority authority = authorityService.findById(id);
        return ResponseEntity.status(OK).body(authority);
    }

    @GetMapping("/authorities/{accountId}/account")
    public ResponseEntity<List<AuthorityDTO>> getAuthoritiesByAccountId(@PathVariable("accountId") String accountId) {
        List<AuthorityDTO> authorities = authorityService.findByAccount(accountId);
        return ResponseEntity.status(OK).body(authorities);
    }

    @PostMapping("/authorities/{accountId}/account/{roleId}/role")
    public ResponseEntity<Authority> addAuthority(@PathVariable("accountId") String accountId,
                                                  @PathVariable("roleId") String roleId) {
        try {
            Authority saveAuthority = authorityService.create(accountId, roleId);
            return ResponseEntity.status(OK).body(saveAuthority);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/authorities/{id}")
    public ResponseEntity<Authority> updateAuthority(@PathVariable("id") Integer authorityId,
                                                     @RequestParam("userId") Optional<String> userId,
                                                     @RequestParam("roleId") Optional<String> roleId) {

        try {
            Authority updateAuthority = authorityService.update(authorityId, userId.orElse(null), roleId.orElse(null));
            return ResponseEntity.status(OK).body(updateAuthority);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/authorities/{id}")
    public ResponseEntity<Authority> deleteAuthority(@PathVariable("id") Integer id) {
        authorityService.delete(id);
        return ResponseEntity.status(NO_CONTENT).body(null);
    }

}
