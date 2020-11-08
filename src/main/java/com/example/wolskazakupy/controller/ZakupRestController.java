package com.example.wolskazakupy.controller;

import com.example.wolskazakupy.service.ZakupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZakupRestController {

    private ZakupService zakupService;

    @Autowired
    public ZakupRestController(ZakupService zakupService) {
        this.zakupService = zakupService;
    }

    @GetMapping("/images/{path}")
    public ResponseEntity<?> getPhoto(@PathVariable(value = "path") String path) {
        return zakupService.getImagePath(path);
    }

}
