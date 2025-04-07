package com.stlang.store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Category's name not empty !")
    @Column(columnDefinition = "NVARCHAR(200)")
    private String name;

    @OneToMany (mappedBy = "role")
    private List<Authority> authorities;
}
