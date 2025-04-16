package com.stlang.store.controller;

import com.stlang.store.dto.UploadFileDTO;
import com.stlang.store.exception.CustomFileUploadException;
import com.stlang.store.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.path}")
public class FileController {
    @Autowired
    private FileService fileService;

    @Value("${stlang.upload_file.base_path}")
    private String basePath;

    @PostMapping("/upload/files")
    public ResponseEntity<UploadFileDTO> uploadFile(@RequestParam(name = "file", required = false ) MultipartFile file,
                                                    @RequestParam("folder") String folder) throws URISyntaxException {

        // validate //
        if(file.isEmpty() || file == null){
            throw new CustomFileUploadException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif","doc", "web","docx","pdf");
        Boolean isValid = allowedExtensions.stream().allMatch(item -> fileName.toLowerCase().endsWith(item))    ;
        if (!isValid){
            throw new CustomFileUploadException("Invalid file format");
        }
        // create  //
        fileService.createDirectory(basePath.concat(folder));
        String fileUpload = fileService.upload(file,folder);
        UploadFileDTO uploadFileDTO = UploadFileDTO.builder()
                .fileName(fileUpload)
                .uploadedAt(Instant.now())
                .build();
        return ResponseEntity.status(OK).body(uploadFileDTO);
    }

}
