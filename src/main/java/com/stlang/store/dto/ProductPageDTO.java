package com.stlang.store.dto;

import com.stlang.store.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDTO {

    private Meta meta;
    private List<Product> products;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        private Integer currentPage;
        private Integer pageSize;
        private Integer totalPages;
        private Integer totalElements;
    }


}
