package com.stlang.store.controller;

import com.stlang.store.service.IFileManagerService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.path}")
public class FileManagerController {

    @Autowired
    private IFileManagerService fileManagerService;


    @GetMapping( "/files/{folder}/{file}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("file") String file, @PathVariable("folder") String folder) {
        byte[] downloadFile = fileManagerService.read(folder, file);
        return ResponseEntity.status(OK)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(downloadFile);
    }

    @PostMapping("/files/{folder}")
    public ResponseEntity<List<String>> uploadFile(@PathVariable("folder") String folder,@PathParam("files") MultipartFile[] files) {
        List fileNames = fileManagerService.save(folder,files);
        return ResponseEntity.ok().body(fileNames);
    }

    @DeleteMapping("/files/{folder}/{file}")
    public ResponseEntity<Void> deleteFile(@PathVariable("folder") String folder, @PathVariable("file") String file) {
        fileManagerService.delete(folder, file);
        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping("/files/{folder}")
    public ResponseEntity<List<String>> getFiles(@PathVariable("folder") String folder) {
        List fileNames =  fileManagerService.list(folder);
        return ResponseEntity.status(OK).body(fileNames);
    }
}
