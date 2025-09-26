package com.example.controller;

import com.example.repository.InMemoryLinkDAO;
import com.example.model.Link;
import com.example.service.ILinkService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/link")
@AllArgsConstructor
public class ControllerLink {
    private final ILinkService service;

    private static final AtomicLong counter = new AtomicLong(0);
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private String generatedShortLink;

    private static String encodeCounterToBase62(long id) {
        if (id == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            id = id / 62;
            sb.append(BASE62_CHARS.charAt((int)(id % 62)));
        }

        return sb.reverse().toString();
    }

    public static String generateShortLink() {
        long currentId = counter.incrementAndGet();
        return encodeCounterToBase62(currentId);
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        try {
            model.addAttribute("linkShortener", generatedShortLink);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "home";
    }

    @PostMapping()
    public String handleShortenRequest(@ModelAttribute("originalLink") String originalLink) {
        generatedShortLink = generateShortLink();

        Link link = new Link(generatedShortLink, originalLink);

        service.saveLink(link);
        return "redirect:/api/v1/link";
    }
}


