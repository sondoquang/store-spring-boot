package com.stlang.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Accounts")
public class Account  implements Serializable {

    @Id
    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String username;
    @NotNull(message = "Password is not null !")
    private String password;
    @Column(columnDefinition = "NVARCHAR(200)", nullable = true)
    private String fullname;
    @Column(columnDefinition = "NVARCHAR(200)", nullable = true)
    private String email;
    @Column(columnDefinition = "NVARCHAR(200)", nullable = true)
    private String photo;

    @Builder.Default
    private Boolean gender = true;

    @Column(columnDefinition = "NVARCHAR(1000)",name = "refresh_token")
    private String refreshToken;

    @JsonIgnore
    @OneToMany (mappedBy = "account", fetch = FetchType.EAGER)
    private List<Authority> authorities;

    @JsonIgnore
    @OneToMany (mappedBy = "account")
    private List<Order> orders;

}
