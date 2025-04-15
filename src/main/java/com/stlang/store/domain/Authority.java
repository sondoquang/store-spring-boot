package com.stlang.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"username","role_id"})})
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "username", columnDefinition = ("VARCHAR(100)"))
    private Account account;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
