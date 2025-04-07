package com.stlang.store.dto;

import com.stlang.store.domain.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductDTO {

    private Integer id;
    private String name;
    private String image;
    private Double price;
    private Date createDate;
    private Boolean available;
    private Integer sold;
    private Integer inventory;
    private String shortDesc;
    private String detailDesc;
    private Category category;

}
