package com.example.controller;

import com.example.model.Link;
import com.example.repository.InMemoryLinkDAO;
import com.example.service.ILinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/api/v1/link")
@RequiredArgsConstructor
public class ControllerLink {
    private final ILinkService SERVICE;

    private static final AtomicLong COUNTER = new AtomicLong(0);
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private String linkId;

    private static String encodeCounterToBase62(long id) {
        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            sb.append(BASE62_CHARS.charAt((int) (id % 62)));
            id /= 62;
        }

        return "https://link_shorter/" + sb.reverse();
    }

    public static String generateShortLink() {
        long currentId = COUNTER.incrementAndGet();
        return encodeCounterToBase62(currentId);
    }

    @GetMapping("/nn")
    public String showHomePage(Model model) {
        try {
            model.addAttribute("linkShortener", linkId);
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }

        return "index";
    }

    @PostMapping("/save")
    public String handleShortenRequest(@RequestParam URL link) {
        String generatedShortLink = generateShortLink();
        Link myLink = new Link(generatedShortLink, String.valueOf(link));

        SERVICE.saveLink(myLink);
        linkId = generatedShortLink;

        return "redirect:/api/v1/link/nn";
    }
}
