package com.stlang.store.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Product's name is not empty!")
    private String name;

    private String image;

    @Min(value = 0, message = "Product's price must be greater than or equals 0 !")
    private Double price;

    @Temporal(TemporalType.DATE)
    @Column(name="create_date")
    @Builder.Default
    private Date createDate = new Date();

    @Builder.Default
    private Boolean available = true;
    @Min(value = 0, message = "Product's sold must be greater than or equals 0 !")
    private Integer sold;
    @Min(value = 0, message = "Product's inventory must be greater than or equals 0 !")
    private Integer inventory;

    @Column(name="short_desc", columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "Short description for product is not empty!")
    private String shortDesc;

    @Column(name="detail_desc", columnDefinition = "NVARCHAR(1000)")
    @NotBlank(message = "Detail description for product is not empty!")
    private String detailDesc;

    @Builder.Default
    private Boolean active = true;

    @ManyToOne
    @JoinColumn (name ="category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

}
