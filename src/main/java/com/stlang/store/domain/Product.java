package com.stlang.store.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private String name;
    private String image;
    private Double price;
    @Temporal(TemporalType.DATE)
    @Column(name="create_date")
    private Date createDate;
    private Boolean available;
    private Integer sold;
    private Integer inventory;

    @Column(name="short_desc", columnDefinition = "NVARCHAR(100)")
    private String shortDesc;
    @Column(name="detail_desc", columnDefinition = "NVARCHAR(1000)")
    private String detailDesc;

    @ManyToOne
    @JoinColumn (name ="category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

}
