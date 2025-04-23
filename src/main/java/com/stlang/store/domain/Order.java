package com.stlang.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Builder.Default
    private Date date = new Date();

    @NotEmpty(message = "Address is not empty!")
    private String address;

    @NotEmpty(message = "FullName is not empty!")
    private String fullname;

    @NotEmpty(message = "Phone is not empty!")
    private String phone;

    @Builder.Default
    private String paymentMethod = "COD";

    @Min(value = 0, message = "Total price must be greater than 0!")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "username")
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

}
