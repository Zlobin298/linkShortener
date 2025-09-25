package com.example.controller;

import com.example.impl.InMemoryLinkDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/api/v1/generator_shortener_link")
public class ControllerGeneratorLink {
    private final InMemoryLinkDAO memoryLink;

    private static final AtomicLong counter = new AtomicLong(0);
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public ControllerGeneratorLink(InMemoryLinkDAO memoryLink) {
        this.memoryLink = memoryLink;
    }

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

    @GetMapping("get_shortener_link")
    public String showHomePage(@ModelAttribute("originalLink") String originalLink, Model model) {
        try {
            model.addAttribute("linkShortener", memoryLink.getLINK().get(originalLink));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }

    @PostMapping("save_link")
    public String handleShortenRequest(@ModelAttribute("originalLink") String originalLink, Model model) {
        String generatedShortLink = generateShortLink();

        memoryLink.saveLink(generatedShortLink, originalLink);

        try {
            model.addAttribute("shortLinkGenerated", generatedShortLink);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "home";
    }
}
