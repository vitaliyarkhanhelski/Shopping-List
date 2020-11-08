package com.example.wolskazakupy.service;

import com.example.wolskazakupy.model.Zakup;
import com.example.wolskazakupy.repository.ZakupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class ZakupService {

    private ZakupRepository zakupRepository;

    @Autowired
    public ZakupService(ZakupRepository zakupRepository) {
        this.zakupRepository = zakupRepository;
    }

    public void save(Zakup zakup, MultipartFile file) {

        if (!zakup.getImgLink().isEmpty()) {
            Path pathToDelete = Paths.get("images/" + zakup.getImgLink());
            try {
                Files.deleteIfExists(pathToDelete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path path = Paths.get("images/" + file.getOriginalFilename());
        if (!Files.exists(path)) {
            try {
                Files.copy(file.getInputStream(), path);
                zakup.setImgLink(file.getOriginalFilename());
                zakupRepository.save(zakup);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Resource resource = new UrlResource(path.toUri());
                File targetFile = resource.getFile();

                if (file.getSize() != targetFile.length()) {
                    String newFileName = file.getOriginalFilename();

                    if (file.getOriginalFilename().equals(targetFile.getName())) {
                        String[] s = file.getOriginalFilename().split("[.]");
                        if (s[0].matches(".*_-_[0-9]*$")) {
                            int number = Integer.parseInt(s[0].substring(s[0].indexOf("_-_") + 3));
                            s[0] = s[0].substring(0, s[0].indexOf("_-_") + 3);
                            path = Paths.get("images/" + s[0] + (number + 1) + "." + s[1]);
                            newFileName = s[0] + (number + 1) + "." + s[1];
                        } else {
                            s[0] += "_-_1";
                            path = Paths.get("images/" + s[0] + "." + s[1]);
                            newFileName = s[0] + "." + s[1];
                        }
                    }
                    try {
                        Files.copy(file.getInputStream(), path);
                        zakup.setImgLink(newFileName);
                        zakupRepository.save(zakup);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void save(Zakup zakup) {
        zakupRepository.save(zakup);
    }


    public List<Zakup> findAll() {
        return zakupRepository.findAll();
    }


    public Zakup findById(Long zakupId) {
        return zakupRepository.findById(zakupId).get();
    }


    public void delete(Long zakupId) {
        zakupRepository.deleteById(zakupId);
    }


    public Optional<Zakup> findFirstByNameAndComment(String name, String comment) {
        return zakupRepository.findFirstByNameAndComment(name, comment);
    }


    public ResponseEntity<?> getImagePath(String imagePath) {
        Resource resource = null;
        File file = null;
        String contentType = null;
        Path path = Paths.get("images/" + imagePath);
        try {
            resource = new UrlResource(path.toUri());
            file = resource.getFile();
            contentType = Files.probeContentType(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline") //attachent;filename=\"" + targetFile.getName() + "\"
                .body(resource);
    }
}
