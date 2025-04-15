package com.stlang.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table (name = "Roles")
public class Role implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Category's name not empty !")
    @Column(columnDefinition = "NVARCHAR(200)")
    private String name;

    @JsonIgnore
    @OneToMany (mappedBy = "role",fetch = FetchType.EAGER)
    private List<Authority> authorities;
}
