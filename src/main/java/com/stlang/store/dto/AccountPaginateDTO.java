package com.stlang.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountPaginateDTO {

    private Meta meta;
    private List<AccountDTO> accounts;

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
