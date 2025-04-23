package com.stlang.store.dto;

import com.stlang.store.domain.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductDTO {

    @NotBlank(message = "Product's name is not empty!")
    private String name;

    @NotBlank(message = "Product's author is not empty!")
    private String author;

    @NotBlank(message = "Product's publisher is not empty!")
    private String publisher;

    @NotBlank(message = "Product's image is not empty!")
    @NotNull(message = "Product's image is not null!")
    private String image;

    @Min(value = 0, message = "Product's price must be greater than 0!")
    private Double price;

    private Boolean available;

    @Min(value = 0, message = "Product's price must be greater than 0!")
    private Integer sold;

    @Min(value = 0, message = "Product's price must be greater than 0!")
    private Integer inventory;

    @NotBlank(message = "Product's short description is not empty!")
    private String shortDesc;

    @NotBlank(message = "Product's detail description is not empty!")
    private String detailDesc;

    @NotNull(message = "Category not null")
    private Integer categoryId;

}
