package com.stlang.store.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileDTO {

    private String fileName;
    private Instant uploadedAt;
}
