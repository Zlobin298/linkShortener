package com.example.controller;

import com.example.model.Link;
import com.example.model.OriginalLink;
import com.example.service.ILinkService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/api/v1/link")
@AllArgsConstructor
public class ControllerLink {
    private final ILinkService service;

    private static final AtomicLong counter = new AtomicLong(0);
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String generatedShortLink = generateShortLink();

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

    @GetMapping("/nn")
    public String showHomePage(Model model) {
        try {
            if (counter == null) {
                model.addAttribute("originalLink", new OriginalLink());
            } else {
                model.addAttribute("linkShortener", generatedShortLink);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

    @PostMapping("/save")
    public String handleShortenRequest(@ModelAttribute("originalLink") OriginalLink originalLink, Model model) {
        Link link = new Link(generatedShortLink, originalLink.getLink());

        service.saveLink(link);

        try {
            model.addAttribute("linkShortener", generatedShortLink);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/api/v1/link/nn";
    }
}


