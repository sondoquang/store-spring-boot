package com.stlang.store.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface IFileManagerService {

    byte[] read(String folder, String file);
    List<String> save(String folder, MultipartFile[] files);
    void delete(String folder, String file);
    List<String>list(String folder);

}
