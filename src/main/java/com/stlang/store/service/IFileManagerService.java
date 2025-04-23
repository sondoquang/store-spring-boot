package com.stlang.store.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

public interface IFileManagerService {

    byte[] read(String folder, String file);

    List<String> save(String folder, MultipartFile[] files);

    String upload(String folder, MultipartFile file) throws URISyntaxException;

    void delete(String folder, String file);

    List<String> list(String folder);

}
