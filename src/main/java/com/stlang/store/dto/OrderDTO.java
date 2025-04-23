package com.stlang.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String paymentMethod;
    @NotEmpty(message = "FullName is not empty!")
    private String fullname;
    @NotEmpty(message = "Phone is not empty!")
    private String phone;
    @NotEmpty(message = "Address is not empty!")
    private String address;
    @Min(value = 0, message = "Total price must be greater than 0!")
    private Double totalPrice;

    @NotEmpty(message = "Detail is not empty !")
    private List<Detail> details;

    @NotEmpty(message = "Id user is not empty!")
    private String username;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Integer productId;
        private Integer quantity;
        private String productName;
    }

}
