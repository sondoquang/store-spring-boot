package com.stlang.store.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class FileService {

    @Value("${stlang.upload_file.base_path}")
    private String basePath;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpFile = new File(path.toString());
        if( !tmpFile.exists() ) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("File already exists");
        }
    }

    public String upload(MultipartFile file, String folder) throws URISyntaxException {
        String name = System.currentTimeMillis() + file.getOriginalFilename();
        String fileName = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
        URI uri = new URI(basePath.concat(folder).concat("/").concat(fileName));
        Path path = Paths.get(uri);
        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

}
