package com.example.controller;

import com.example.dto.LinkDTO;
import com.example.model.Link;
import com.example.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LinkController {
    private final LinkService SERVICE;

    private String shorterLink;
    private String originalLink;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        try {
            if (SERVICE.isExistsLink(originalLink)) {
                model.addAttribute("linkShortener", shorterLink);
            } else {
                model.addAttribute("linkShortener", "Укажите существующую ссылку");
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }

        return "index";
    }

    @PostMapping("save")
    public String handleShortenRequest(@Valid LinkDTO dto) {
        originalLink = dto.getLink();

        if (SERVICE.isExistsLink(originalLink)) {
            String generateShortLink = SERVICE.generateShortLink(originalLink);
            Link myLink = new Link(generateShortLink, dto.getLink());

            SERVICE.saveLink(myLink);
            shorterLink = generateShortLink;
        }

        return "redirect:/home";
    }

    @GetMapping("/link/{link}")
    public String transferNewLink(@PathVariable String link) {
        return "redirect:" + SERVICE.fetchOriginalLink(link);
    }
}
