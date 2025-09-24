package com.example.controller;

import com.example.model.Link;
import com.example.service.ILinkService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/link")
@AllArgsConstructor
public class ControllerLink {
    private final ILinkService service;

    @PostMapping("save_link")
    public String saveLink(@RequestBody Link link) {
        service.saveLink(link);
        return "Данные записаны";
    }

    @DeleteMapping("delete_link/{}")
    public void deleetLink(@PathVariable String id) {
        service.deleetLink(id);
    }
}


