package com.example.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Link;
import com.example.service.ILinkService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/link")
@AllArgsConstructor
public class ControllerLink {
    private final ILinkService service;

    @PostMapping("save_link")
    public String saveLink(@RequestBody Link link) {
        service.saveLink(link);
        return "Данные записанны";
    }

    @DeleteMapping("delete_link/{}")
    public void deleetLink(@PathVariable String id) {
        service.deleetLink(id);
    }
}


