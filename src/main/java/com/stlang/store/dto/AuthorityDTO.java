package com.stlang.store.dto;

import com.stlang.store.domain.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO {

    private Integer id;
    private AccountDTO accountDTO;
    private Role role;
}
