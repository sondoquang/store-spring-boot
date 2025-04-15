package com.stlang.store.service.serviceimpl;

import com.stlang.store.service.IFileManagerService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileManagerService implements IFileManagerService {

    @Autowired
    ServletContext app;

    private Path getPath(String folder, String fileName) {
        File dir = Paths.get(app.getRealPath("/files/"), folder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), fileName);
    }

    @Override
    public byte[] read(String folder, String filename) {
        Path path = getPath(folder, filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> save(String folder, MultipartFile[] files) {
        List<String> fileNames = new ArrayList<String>();
        for (MultipartFile file : files) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String fileName = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = getPath(folder, fileName);
            try {
                file.transferTo(path);
                fileNames.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return fileNames;
    }

    @Override
    public String upload(String folder, MultipartFile file) {
        String name = System.currentTimeMillis() + file.getOriginalFilename();
        String fileName = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
        Path path = getPath(folder, fileName);
        try {
            file.transferTo(path);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String folder, String filename) {
        Path path = getPath(folder, filename);
        path.toFile().delete();
    }

    @Override
    public List<String> list(String folder) {
        List<String> list = new ArrayList<String>();
        File dir = Paths.get(app.getRealPath("/files/"), folder).toFile();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                list.add(file.getName());
            }
        }
        return list;
    }
}
