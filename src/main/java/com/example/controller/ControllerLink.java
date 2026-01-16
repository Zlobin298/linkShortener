package com.example.controller;

import com.example.model.Link;
import com.example.service.ILinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ControllerLink {
    private final ILinkService SERVICE;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String linkId;
    private String originalLink;

    private static String encodeCounterToBase62() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return "http://localhost:8080/" + sb;
    }

    private String generateShortLink() {
        if (SERVICE.isRecordAbsent(originalLink)) return SERVICE.getEncodedLink(originalLink);
        return encodeCounterToBase62();
    }

    @GetMapping
    public String showHomePage(Model model) {
        try {
            model.addAttribute("linkShortener", linkId);
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }

        return "index";
    }

    @PostMapping("save")
    public String handleShortenRequest(@RequestParam String link) {
        originalLink = link;

        String generateShortLink = generateShortLink();
        Link myLink = new Link(generateShortLink, link);

        SERVICE.saveLink(myLink);
        linkId = generateShortLink;

        return "redirect:/";
    }

    @GetMapping("/{link}")
    public String transferNewLink(@PathVariable String link) {
        return "redirect:" + SERVICE.fetchOriginalLink(link);
    }
}
